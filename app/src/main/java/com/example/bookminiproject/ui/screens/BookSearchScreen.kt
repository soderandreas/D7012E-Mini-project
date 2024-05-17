package com.example.bookminiproject.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
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
import com.example.bookminiproject.model.Book
import com.example.bookminiproject.model.Works
import com.example.bookminiproject.viewmodel.BooksDBViewModel
import com.example.bookminiproject.viewmodel.SearchResultUiState
import com.example.bookminiproject.viewmodel.WorkListUiState

@Composable
fun BookSearchScreen(
    booksDBViewModel: BooksDBViewModel,
    modifier: Modifier = Modifier
) {
    var status: SearchResultUiState = SearchResultUiState.Nothing
    var works: List<Works> = listOf()

    status = booksDBViewModel.searchResultUiState

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
        works = works,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearchWork: () -> Unit,
    works: List<Works>,
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
        LazyColumn (modifier) {
            items(works) { work ->
                BookListItemCard(
                    work = work,
                    { },
                    modifier = Modifier
                        .padding(8.dp)
                )
            }
        }
    }
}
