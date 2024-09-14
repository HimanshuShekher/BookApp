package com.example.book.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.book.data.model.Book
import com.example.book.data.repository.BookRepository

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookListViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {

    private val _bookList = MutableStateFlow<List<Book>>(emptyList())
    val bookList: StateFlow<List<Book>> get() = _bookList

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _yearTabs = MutableStateFlow<List<Int>>(emptyList())
    val yearTabs: StateFlow<List<Int>> get() = _yearTabs

    private val _selectedYear = MutableStateFlow<Int?>(null)
    val selectedYear: StateFlow<Int?> get() = _selectedYear

    init {
        fetchBooks()
    }

    private fun fetchBooks() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val books = bookRepository.fetchBooks()
                _bookList.value = books
                updateYearTabs(books)
            } catch (e: Exception) {

            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun updateYearTabs(books: List<Book>) {
        val years = books
            .map { book -> bookRepository.convertTimestampToYear(book.publishedChapterDate) }
            .distinct()
            .filterNotNull()
            .sortedDescending()

        _yearTabs.value = years

        if (_selectedYear.value == null && years.isNotEmpty()) {
            _selectedYear.value = years.first()
        }
    }

    fun onScrollPositionChanged(visibleBooks: List<Book>) {
        val year = visibleBooks.firstOrNull()?.let { book ->
            bookRepository.convertTimestampToYear(book.publishedChapterDate)
        }
        if (year != null) {
            _selectedYear.value = year
        }
    }

    fun updateFavoriteStatus(bookId: String, isFavorite: Boolean) {
        viewModelScope.launch {
            bookRepository.updateFavoriteStatus(bookId, isFavorite)
            _bookList.value = _bookList.value.map { book ->
                if (book.id == bookId) book.copy(isFavorite = isFavorite) else book
            }
        }
    }
}
