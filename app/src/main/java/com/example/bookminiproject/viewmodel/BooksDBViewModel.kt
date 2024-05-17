package com.example.bookminiproject.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.bookminiproject.BookDBApplication
import com.example.bookminiproject.database.WorksRepository
import com.example.bookminiproject.model.Author
import com.example.bookminiproject.model.AuthorKey
import com.example.bookminiproject.model.Description
import com.example.bookminiproject.model.Work
import com.example.bookminiproject.model.Works
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface WorkListUiState {
    data class Success(val works: List<Works>) : WorkListUiState
    object Error : WorkListUiState
    object Loading : WorkListUiState
}

sealed interface SelectedWorkUiState {
    data class Success(
        val work: Work,
        val author: String
    ) : SelectedWorkUiState
    object Error : SelectedWorkUiState
    object Loading : SelectedWorkUiState
}

sealed interface SelectedAuthorUiState {
    data class Success(
        val author: Author,
        val works: List<Work>
    ) : SelectedAuthorUiState
    object Error : SelectedAuthorUiState
    object Loading : SelectedAuthorUiState
}

sealed interface SearchResultUiState {
    data class Success(val works: List<Works>): SearchResultUiState
    object Error : SearchResultUiState
    object Loading : SearchResultUiState
    object Nothing : SearchResultUiState
}

class BooksDBViewModel(
    private val worksRepository: WorksRepository,
) : ViewModel() {
    var workListUiState: WorkListUiState by mutableStateOf(WorkListUiState.Loading)
        private set

    var selectedWorkUiState: SelectedWorkUiState by mutableStateOf(SelectedWorkUiState.Loading)
        private set

    var selectedAuthorUiState: SelectedAuthorUiState by mutableStateOf(SelectedAuthorUiState.Loading)
        private set

    var searchResultUiState: SearchResultUiState by mutableStateOf(SearchResultUiState.Nothing)
        private set

    var searchQuery by mutableStateOf("")
        private set

    init {
        getTrendingWorks()
    }


    fun getTrendingWorks() {
        viewModelScope.launch {
            workListUiState = WorkListUiState.Loading
            workListUiState = try {
                WorkListUiState.Success(worksRepository.getTrendingWorks().works)
            } catch (e: IOException) {
                WorkListUiState.Error
            } catch (e: HttpException) {
                WorkListUiState.Error
            }
        }
    }

    fun getWorksQuery() {
        viewModelScope.launch {
            searchResultUiState = SearchResultUiState.Loading
            searchResultUiState = try {
                val result = worksRepository.getWorksQuery(searchQuery).docs
                if (result != null) {
                    Log.d("Books result", result.toString())
                    SearchResultUiState.Success(result)
                } else {
                    SearchResultUiState.Error
                }
            } catch (e: IOException) {
                SearchResultUiState.Error
            } catch (e: HttpException) {
                SearchResultUiState.Error
            }
        }
    }

    fun setSelectedWork(workId: String) {
        viewModelScope.launch {
            selectedWorkUiState = SelectedWorkUiState.Loading
            selectedWorkUiState = try {
                val work = worksRepository.getWork(workId)
                val name = worksRepository.getAuthorName(work.authors[0].author.key)
                val descriptionValue = when (val description = work.description) {
                    is Description.Text -> description.value
                    is Description.Object -> description.value
                    null -> "Description of $name is not available."
                }
                Log.d("Description", "description of requested work: $descriptionValue")
                SelectedWorkUiState.Success(work, name)
            } catch (e: IOException) {
                SelectedWorkUiState.Error
            } catch (e: HttpException) {
                SelectedWorkUiState.Error
            }
        }
    }

    fun setSelectedAuthor(key: String) {
        viewModelScope.launch {
            selectedAuthorUiState = SelectedAuthorUiState.Loading
            selectedAuthorUiState = try {
                val author = worksRepository.getAuthor(key)
                val works = worksRepository.getAuthorWorks(key)
                SelectedAuthorUiState.Success(author, works.entries)
            } catch (e: IOException) {
                SelectedAuthorUiState.Error
            } catch (e: HttpException) {
                SelectedAuthorUiState.Error
            }
        }
    }

    fun onSearchQueryChange(newQuery: String) {
        searchQuery = newQuery
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as BookDBApplication)
                val booksRepository = application.container.booksRepository
                val worksRepository = application.container.worksRepository
                BooksDBViewModel(
                    worksRepository = worksRepository,
                )
            }
        }
    }
}