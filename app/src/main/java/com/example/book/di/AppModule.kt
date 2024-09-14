package com.example.book.di

import android.content.Context
import com.example.book.data.network.ApiService
import com.example.book.data.repository.BookRepository
import com.example.book.data.repository.BookRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideBookRepository(
        @Named("BookListApiService") apiService: ApiService,
        @ApplicationContext context: Context
    ): BookRepository {
        return BookRepositoryImpl(apiService, context)
    }
}
