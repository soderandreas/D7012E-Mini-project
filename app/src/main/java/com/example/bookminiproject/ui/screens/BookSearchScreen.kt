package com.example.bookminiproject.ui.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.example.bookminiproject.model.Works
import com.example.bookminiproject.viewmodel.BooksDBViewModel
import com.example.bookminiproject.viewmodel.SearchResultUiState

@Composable
fun BookSearchScreen(
    booksDBViewModel: BooksDBViewModel,
    onBookListItemClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // var status: SearchResultUiState = SearchResultUiState.Nothing
    var works: List<Works> = listOf()

    val status = booksDBViewModel.searchResultUiState

    when(status) {
        is SearchResultUiState.Success -> {
            works = status.works
        }
        else -> {}
    }

    SearchScreen(
        searchQuery = booksDBViewModel.searchQuery,
        onSearchQueryChange = { booksDBViewModel.onSearchQueryChange(it) },
        onSearchWork = { booksDBViewModel.getWorksQuery() },
        onBookListItemClicked,
        works = works,
        status = status,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearchWork: () -> Unit,
    onBookListItemClicked: (String) -> Unit,
    works: List<Works>,
    status: SearchResultUiState,
    modifier: Modifier = Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    SearchBar(
        query = searchQuery,
        onQueryChange = onSearchQueryChange,
        onSearch = {
            keyboardController?.hide()
            onSearchWork()
        },
        placeholder = {
            Text(text = "Search for books")
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = null
            )
        },
        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                IconButton(onClick = { onSearchQueryChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        tint = MaterialTheme.colorScheme.onSurface,
                        contentDescription = "Clear search"
                    )
                }
            }
        },
        active = true,
        onActiveChange = {}
    ) {
        when (status) {
            is SearchResultUiState.Success -> {
                LazyColumn (modifier) {
                    items(works) { work ->
                        BookListItemCard(
                            work = work,
                            onBookListItemClicked,
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                        )
                    }
                }
            }
            is SearchResultUiState.Loading -> {
                Text(
                    text = "Loading...",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(16.dp)
                )
            }
            is SearchResultUiState.Error -> {
                Text(
                    text = "Error: Something went wrong!",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(16.dp)
                )
            }
            is SearchResultUiState.Nothing -> {}
        }
    }
}
