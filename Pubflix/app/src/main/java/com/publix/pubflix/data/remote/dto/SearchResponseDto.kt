package com.publix.pubflix.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResponseDto(
    @SerialName("Response") val response: String,
    @SerialName("Search") val search: List<SearchItemDto>? = null,
    @SerialName("totalResults") val totalResults: String? = null,
    @SerialName("Error") val error: String? = null,
)

@Serializable
data class SearchItemDto(
    @SerialName("Title") val title: String,
    @SerialName("Year") val year: String,
    @SerialName("Poster") val poster: String,
)
