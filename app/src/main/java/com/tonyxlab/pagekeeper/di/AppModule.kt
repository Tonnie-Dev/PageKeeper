package com.tonyxlab.pagekeeper.di

import androidx.room.Room
import com.tonyxlab.pagekeeper.data.local.PageKeeperDatabase
import com.tonyxlab.pagekeeper.data.repository.BookRepositoryImpl
import com.tonyxlab.pagekeeper.domain.repository.BookRepository
import com.tonyxlab.pagekeeper.presentation.screens.library.LibraryViewModel
import com.tonyxlab.pagekeeper.utils.AppDefaults
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module { viewModelOf(::LibraryViewModel) }

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

val appModule = listOf(
        viewModelModule,
        databaseModule,
        repositoryModule
)
