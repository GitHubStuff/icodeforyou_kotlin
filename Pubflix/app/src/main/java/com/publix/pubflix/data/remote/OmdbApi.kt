package com.publix.pubflix.data.remote

import com.publix.pubflix.data.remote.dto.SearchResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface OmdbApi {
    @GET("/")
    suspend fun search(
        @Query("s") title: String,
        @Query("apikey") apiKey: String,
    ): SearchResponseDto
}
