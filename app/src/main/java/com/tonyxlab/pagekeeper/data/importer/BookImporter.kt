package com.tonyxlab.pagekeeper.data.importer

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Base64
import com.tonyxlab.pagekeeper.domain.ImportBookResult
import com.tonyxlab.pagekeeper.domain.model.Book
import com.tonyxlab.pagekeeper.domain.repository.BookRepository
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.security.MessageDigest
import java.util.Locale

class BookImporter(
    private val context: Context,
    private val bookRepository: BookRepository
) {

    suspend fun importBook(uri: Uri): ImportBookResult {
        val fileName = context.displayName(uri) ?: uri.lastPathSegment.orEmpty()

        if (!fileName.endsWith(FB2_EXTENSION, ignoreCase = true)) {
            return ImportBookResult.UnsupportedFormat
        }

        val fileHash = context.contentResolver.openInputStream(uri)?.use(::calculateHash)
                ?: return ImportBookResult.Error("Unable to open selected file.")

        if (bookRepository.existsByHash(fileHash)) {
            return ImportBookResult.Duplicate
        }

        val bookFile = copyBookToInternalStorage(uri, fileHash)
                ?: return ImportBookResult.Error("Unable to copy selected file.")

        val metadata = extractMetadata(bookFile, fileHash)
        val book = Book(
                id = fileHash,
                title = metadata.title ?: fileName.removeSuffix(FB2_EXTENSION),
                author = metadata.author ?: UNKNOWN_AUTHOR,
                coverPath = metadata.coverPath,
                fileName = fileName,
                filePath = bookFile.absolutePath,
                dateAdded = System.currentTimeMillis()
        )

        bookRepository.insertBook(book)
        return ImportBookResult.Success
    }

    private fun calculateHash(inputStream: InputStream): String {
        val digest = MessageDigest.getInstance(HASH_ALGORITHM)
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)

        while (true) {
            val bytesRead = inputStream.read(buffer)
            if (bytesRead == -1) break
            digest.update(buffer, 0, bytesRead)
        }

        return digest.digest().joinToString(separator = "") { byte ->
            "%02x".format(byte)
        }
    }

    private fun copyBookToInternalStorage(uri: Uri, fileHash: String): File? {
        val booksDirectory = File(context.filesDir, BOOKS_DIRECTORY).apply { mkdirs() }
        val destination = File(booksDirectory, "$fileHash$FB2_EXTENSION")

        return context.contentResolver.openInputStream(uri)?.use { input ->
            destination.outputStream().use { output -> input.copyTo(output) }
            destination
        }
    }

    private fun extractMetadata(bookFile: File, fileHash: String): Fb2Metadata {
        val parser = XmlPullParserFactory.newInstance().apply {
            isNamespaceAware = true
        }.newPullParser()

        FileInputStream(bookFile).use { input ->
            parser.setInput(input, Charsets.UTF_8.name())
            return parseMetadata(parser, fileHash)
        }
    }

    private fun parseMetadata(parser: XmlPullParser, fileHash: String): Fb2Metadata {
        var title: String? = null
        var author: String? = null
        var coverBinaryId: String? = null
        var coverPath: String? = null
        var insideAuthor = false
        val authorParts = mutableListOf<String>()

        while (parser.eventType != XmlPullParser.END_DOCUMENT) {
            when (parser.eventType) {
                XmlPullParser.START_TAG -> {
                    when (parser.name) {
                        BOOK_TITLE_TAG -> if (title == null) {
                            title = parser.nextText().trim().ifBlank { null }
                        }

                        AUTHOR_TAG -> {
                            if (author == null) {
                                insideAuthor = true
                                authorParts.clear()
                            }
                        }

                        FIRST_NAME_TAG, MIDDLE_NAME_TAG, LAST_NAME_TAG, NICKNAME_TAG -> {
                            if (insideAuthor) {
                                parser.nextText().trim().takeIf(String::isNotBlank)?.let(authorParts::add)
                            }
                        }

                        IMAGE_TAG -> {
                            if (coverBinaryId == null) {
                                coverBinaryId = parser.getHrefAttribute()?.removePrefix("#")
                            }
                        }

                        BINARY_TAG -> {
                            val binaryId = parser.getAttributeValue(null, ID_ATTRIBUTE)
                            if (binaryId == coverBinaryId && coverPath == null) {
                                val contentType = parser.getAttributeValue(null, CONTENT_TYPE_ATTRIBUTE)
                                coverPath = saveCover(
                                        fileHash = fileHash,
                                        contentType = contentType,
                                        base64Content = parser.nextText()
                                )
                            }
                        }
                    }
                }

                XmlPullParser.END_TAG -> {
                    if (parser.name == AUTHOR_TAG && insideAuthor) {
                        author = authorParts.joinToString(separator = " ").ifBlank { null }
                        insideAuthor = false
                    }
                }
            }

            parser.next()
        }

        return Fb2Metadata(
                title = title,
                author = author,
                coverPath = coverPath
        )
    }

    private fun saveCover(fileHash: String, contentType: String?, base64Content: String): String? {
        val coverBytes = runCatching {
            Base64.decode(base64Content.filterNot(Char::isWhitespace), Base64.DEFAULT)
        }.getOrNull() ?: return null

        val extension = contentType?.substringAfter("/")
                ?.lowercase(Locale.US)
                ?.takeIf { it in SUPPORTED_COVER_EXTENSIONS }
                ?: DEFAULT_COVER_EXTENSION
        val coversDirectory = File(context.filesDir, COVERS_DIRECTORY).apply { mkdirs() }
        val coverFile = File(coversDirectory, "$fileHash.$extension")

        coverFile.writeBytes(coverBytes)
        return coverFile.absolutePath
    }

    private fun XmlPullParser.getHrefAttribute(): String? {
        return getAttributeValue(null, HREF_ATTRIBUTE)
                ?: getAttributeValue(XLINK_NAMESPACE, HREF_ATTRIBUTE)
                ?: getAttributeValue(null, XLINK_HREF_ATTRIBUTE)
    }

    private fun Context.displayName(uri: Uri): String? {
        return contentResolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)
                ?.use { cursor ->
                    if (!cursor.moveToFirst()) return@use null
                    val displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (displayNameIndex == -1) null else cursor.getString(displayNameIndex)
                }
    }

    private data class Fb2Metadata(
        val title: String?,
        val author: String?,
        val coverPath: String?
    )

    private companion object {
        const val FB2_EXTENSION = ".fb2"
        const val UNKNOWN_AUTHOR = "Unknown author"
        const val HASH_ALGORITHM = "SHA-256"
        const val BOOKS_DIRECTORY = "books"
        const val COVERS_DIRECTORY = "covers"
        const val DEFAULT_COVER_EXTENSION = "jpg"

        const val BOOK_TITLE_TAG = "book-title"
        const val AUTHOR_TAG = "author"
        const val FIRST_NAME_TAG = "first-name"
        const val MIDDLE_NAME_TAG = "middle-name"
        const val LAST_NAME_TAG = "last-name"
        const val NICKNAME_TAG = "nickname"
        const val IMAGE_TAG = "image"
        const val BINARY_TAG = "binary"
        const val ID_ATTRIBUTE = "id"
        const val CONTENT_TYPE_ATTRIBUTE = "content-type"
        const val HREF_ATTRIBUTE = "href"
        const val XLINK_HREF_ATTRIBUTE = "xlink:href"
        const val XLINK_NAMESPACE = "http://www.w3.org/1999/xlink"

        val SUPPORTED_COVER_EXTENSIONS = setOf("jpg", "jpeg", "png", "webp")
    }
}
