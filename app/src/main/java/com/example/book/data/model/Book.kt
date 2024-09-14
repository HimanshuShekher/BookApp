package com.example.book.data.model

data class Book(
    val id: String,
    val image: String,
    val score: Double,
    val popularity: Int,
    val title: String,
    val publishedChapterDate: Long,
    val publishedYear: Int ,
    var isFavorite: Boolean = false
)
