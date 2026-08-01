package com.tonyxlab.pagekeeper.di

import androidx.room.Room
import com.tonyxlab.pagekeeper.data.importer.BookImporter
import com.tonyxlab.pagekeeper.data.local.database.PageKeeperDatabase
import com.tonyxlab.pagekeeper.data.parser.Fb2Parser
import com.tonyxlab.pagekeeper.data.parser.Fb2PullParser
import com.tonyxlab.pagekeeper.data.local.datastore.FontDataStore
import com.tonyxlab.pagekeeper.data.repository.BookRepositoryImpl
import com.tonyxlab.pagekeeper.domain.repository.BookRepository
import com.tonyxlab.pagekeeper.presentation.screens.library.LibraryViewModel
import com.tonyxlab.pagekeeper.presentation.screens.reader.ReadViewModel
import com.tonyxlab.pagekeeper.utils.AppDefaults
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::LibraryViewModel)
    viewModelOf(::ReadViewModel)
}

val databaseModule = module {
    single {
        Room.databaseBuilder(
                context = androidContext(),
                klass = PageKeeperDatabase::class.java,
                name = AppDefaults.DATABASE_NAME
        )
                .build()
    }

    single { get<PageKeeperDatabase>().bookDao }

}

val repositoryModule = module {
    single<BookRepository> { BookRepositoryImpl(get()) }
}

val importerModule = module {
    single { BookImporter(androidContext(), get()) }
}

val parserModule = module {
    single<Fb2Parser> { Fb2PullParser() }
}

val dataStoreModule = module{
    singleOf(::FontDataStore)
}

val appModule = listOf(
        viewModelModule,
        databaseModule,
        dataStoreModule,
        repositoryModule,
        importerModule,
        parserModule
)

