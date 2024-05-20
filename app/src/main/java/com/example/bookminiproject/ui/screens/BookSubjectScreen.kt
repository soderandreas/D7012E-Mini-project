package com.example.bookminiproject.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.bookminiproject.model.Works
import com.example.bookminiproject.viewmodel.BooksDBViewModel
import com.example.bookminiproject.viewmodel.SelectedSubjectUiState

@Composable
fun BookSubjectScreen(
    booksDBViewModel: BooksDBViewModel,
    onWorkClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    when(val selectedSubjectUiState = booksDBViewModel.selectedSubjectUiState) {
        is SelectedSubjectUiState.Success -> {
            Column (modifier = modifier) {
                Text(
                    text = selectedSubjectUiState.subject,
                    style = MaterialTheme.typography.headlineSmall,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center)
                        .padding(top = 4.dp)
                )
                LazyColumn {
                    items(selectedSubjectUiState.works) { work ->
                        BookListItemCard(
                            work = Works(
                                key = work.key,
                                title = work.title,
                                coverImage = work.coverImage,
                                authorName = listOf(work.authors[0].name),
                                firstPublishYear = work.firstPublishYear
                            ),
                            onWorkClick,
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                        )
                    }
                }
            }
        }
        SelectedSubjectUiState.Loading -> {
            Text(
                text = "Loading...",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(16.dp)
            )
        }
        SelectedSubjectUiState.Error -> {
            Text(
                text = "Error: Something went wrong!",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}