package com.publix.pubflix.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.publix.pubflix.data.remote.NetworkModule
import com.publix.pubflix.data.repository.MovieRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@OptIn(FlowPreview::class)
class SearchViewModel(
    private val repository: MovieRepository,
    private val debounceDuration: Duration = DEBOUNCE,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val _events = Channel<SearchEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private var searchJob: Job? = null
    private var lastExecutedQuery: String? = null

    init {
        _uiState
            .map { it.query.trim() }
            .distinctUntilChanged()
            .debounce(debounceDuration)
            .filter { it.isNotEmpty() }
            .onEach { query -> executeSearch(query, force = false) }
            .launchIn(viewModelScope)
    }

    fun onQueryChange(value: String) {
        _uiState.update { it.copy(query = value) }
    }

    fun onSearchClick() {
        executeSearch(_uiState.value.query.trim(), force = true)
    }

    fun onClearClick() {
        searchJob?.cancel()
        searchJob = null
        lastExecutedQuery = null
        _uiState.value = SearchUiState()
    }

    private fun executeSearch(rawQuery: String, force: Boolean) {
        val query = rawQuery.trim()
        if (query.isEmpty()) return
        if (!force && query == lastExecutedQuery) return
        lastExecutedQuery = query

        _events.trySend(SearchEvent.SearchStarted)

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            repository.search(query)
                .onSuccess { movies ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            results = movies,
                            errorMessage = null,
                            hasSearched = true,
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            results = emptyList(),
                            errorMessage = error.message ?: ERROR,
                            hasSearched = true,
                        )
                    }
                }
        }
    }

    class Factory(
        private val debounceDuration: Duration = DEBOUNCE,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            SearchViewModel(
                repository = NetworkModule.movieRepository,
                debounceDuration = debounceDuration,
            ) as T
    }

    private companion object {
        val DEBOUNCE: Duration = 5.seconds
        const val ERROR = "Something went wrong. Please try again."
    }
}