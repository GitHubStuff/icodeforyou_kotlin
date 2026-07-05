package com.publix.pubflix.data.repository

import com.publix.pubflix.data.remote.OmdbApi
import com.publix.pubflix.data.remote.dto.SearchItemDto
import com.publix.pubflix.domain.model.DataModel
import kotlinx.coroutines.CancellationException

interface MovieRepository {
    suspend fun search(title: String): Result<List<DataModel>>
}

class MovieRepositoryImpl(
    private val api: OmdbApi,
) : MovieRepository {

    private companion object {
        const val API_KEY = "2094b96"
        const val RESPONSE_FALSE = "False"
    }

    override suspend fun search(title: String): Result<List<DataModel>> =
        try {
            val dto = api.search(title = title, apiKey = API_KEY)
            val movies = if (dto.response.equals(RESPONSE_FALSE, ignoreCase = true)) {
                emptyList()
            } else {
                dto.search.orEmpty().map(SearchItemDto::toDataModel)
            }
            Result.success(movies)
        } catch (cancellation: CancellationException) {
            // Cancellation must propagate so a cancelled coroutine stays cancelled.
            throw cancellation
        } catch (error: Exception) {
            Result.failure(error)
        }
}

private fun SearchItemDto.toDataModel(): DataModel =
    DataModel(title = title, year = year, poster = poster)
