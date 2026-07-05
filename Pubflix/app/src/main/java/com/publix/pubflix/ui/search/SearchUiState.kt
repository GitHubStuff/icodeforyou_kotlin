package com.publix.pubflix.ui.search

import com.publix.pubflix.domain.model.DataModel

data class SearchUiState(
    val query: String = "",
    val results: List<DataModel> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val hasSearched: Boolean = false,
) {
    val isEmptyResult: Boolean
        get() = hasSearched && !isLoading && errorMessage == null && results.isEmpty()
}
