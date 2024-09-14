package com.example.book.data.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.book.data.model.Book
import com.example.book.data.network.ApiService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

interface BookRepository {
    suspend fun fetchBooks(): List<Book>
    fun getBooks(): Flow<List<Book>>
    suspend fun updateFavoriteStatus(bookId: String, isFavorite: Boolean)
    fun convertTimestampToYear(timestamp: Long): Int
}

@Singleton
class BookRepositoryImpl @Inject constructor(
    @Named("BookListApiService") private val apiService: ApiService,
    @ApplicationContext private val context: Context
) : BookRepository {

    private val books = mutableListOf<Book>()


    private val masterKeyAlias: MasterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()


    private val favoritePrefs: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        "favorites_prefs",
        masterKeyAlias,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    override suspend fun fetchBooks(): List<Book> {
        val fetchedBooks = apiService.getBooks("b/CNGI")
        val processedBooks = fetchedBooks.map { book ->
            val publishedYear = convertTimestampToYear(book.publishedChapterDate)
            book.copy(publishedYear = publishedYear, isFavorite = isFavorite(book.id))
        }
        books.clear()
        books.addAll(processedBooks)
        return processedBooks
    }

    override fun getBooks(): Flow<List<Book>> {
        return flow {
            emit(books)
        }
    }

    override suspend fun updateFavoriteStatus(bookId: String, isFavorite: Boolean) {
        withContext(Dispatchers.IO) {
            val editor = favoritePrefs.edit()
            editor.putBoolean(bookId, isFavorite)
            editor.apply()


            val book = books.find { it.id == bookId }
            if (book != null) {
                book.isFavorite = isFavorite
            }
        }
    }

    private fun isFavorite(bookId: String): Boolean {
        return favoritePrefs.getBoolean(bookId, false)
    }

    override fun convertTimestampToYear(timestamp: Long): Int {
        val milliseconds = TimeUnit.SECONDS.toMillis(timestamp)
        val date = java.util.Date(milliseconds)
        val calendar = java.util.Calendar.getInstance()
        calendar.time = date
        return calendar.get(java.util.Calendar.YEAR)
    }
}