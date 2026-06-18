package com.tonyxlab.pagekeeper.domain.model

object BookMock {
    val books = listOf(
            Book(
                    id = "mock-book-1",
                    title = "The Fellowship of the Ring",
                    author = "J.R.R. Tolkien",
                    coverPath = null,
                    fileName = "the_fellowship_of_the_ring.fb2",
                    filePath = "/mock/books/the_fellowship_of_the_ring.fb2",
                    dateAdded = 1_718_928_000_000L,
                    isFavorite = true,
                    isFinished = false
            ),
            Book(
                    id = "mock-book-2",
                    title = "Pride and Prejudice",
                    author = "Jane Austen",
                    coverPath = null,
                    fileName = "pride_and_prejudice.fb2",
                    filePath = "/mock/books/pride_and_prejudice.fb2",
                    dateAdded = 1_718_841_600_000L,
                    isFavorite = false,
                    isFinished = true
            ),
            Book(
                    id = "mock-book-3",
                    title = "The Left Hand of Darkness",
                    author = "Ursula K. Le Guin",
                    coverPath = null,
                    fileName = "the_left_hand_of_darkness.fb2",
                    filePath = "/mock/books/the_left_hand_of_darkness.fb2",
                    dateAdded = 1_718_755_200_000L,
                    isFavorite = true,
                    isFinished = true
            ),
            Book(
                    id = "mock-book-4",
                    title = "Things Fall Apart",
                    author = "Chinua Achebe",
                    coverPath = null,
                    fileName = "things_fall_apart.fb2",
                    filePath = "/mock/books/things_fall_apart.fb2",
                    dateAdded = 1_718_668_800_000L,
                    isFavorite = false,
                    isFinished = false
            )
    )

    val featuredBook = books.first()
}
