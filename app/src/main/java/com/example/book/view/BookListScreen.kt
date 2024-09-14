package com.example.book.view

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.book.data.model.Book
import com.example.book.viewmodel.BookListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookListScreen(
    viewModel: BookListViewModel = hiltViewModel(),
    onBookClick: (Book) -> Unit,
    onLogout: () -> Unit
) {
    val books by viewModel.bookList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val yearTabs by viewModel.yearTabs.collectAsState()
    val selectedYear by viewModel.selectedYear.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Book Shelf") },
                actions = {
                    IconButton(onClick = { onLogout() }) {
                        Icon(
                            imageVector = Icons.Filled.ExitToApp,
                            contentDescription = "Logout",
                            tint = Color.Red.copy(alpha = 0.9f)
                        )
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
            ) {
                if (isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    ScrollableTabRow(
                        selectedTabIndex = yearTabs.indexOf(selectedYear ?: yearTabs.firstOrNull() ?: -1),
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        yearTabs.forEach { year ->
                            Tab(
                                text = { Text(text = year.toString()) },
                                selected = year == selectedYear,
                                onClick = { viewModel.onScrollPositionChanged(books.filter { it.publishedYear == year }) }
                            )
                        }
                    }

                    LazyColumn {
                        items(books.filter { it.publishedYear == selectedYear }) { book ->
                            BookListItem(book = book, onBookClick = onBookClick) { isFavorite ->
                                viewModel.updateFavoriteStatus(book.id, isFavorite)
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun BookListItem(
    book: Book,
    onBookClick: (Book) -> Unit,
    onFavoriteClick: (Boolean) -> Unit
) {
    val isFavorite by rememberUpdatedState(book.isFavorite)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.White)
            .clickable { onBookClick(book) }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(book.image),
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .background(Color.Gray, shape = RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp))
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        ) {
            Text(
                text = book.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Score: ${book.score}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Popularity: ${book.popularity}",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        IconButton(onClick = {
            val newFavoriteStatus = !isFavorite
            onFavoriteClick(newFavoriteStatus)
        }) {
            Icon(
                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = null,
                tint = if (isFavorite) Color.Red else Color.Gray
            )
        }
    }
}
