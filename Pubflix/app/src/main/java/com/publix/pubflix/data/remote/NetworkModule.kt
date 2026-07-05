package com.publix.pubflix.data.remote

import com.publix.pubflix.data.repository.MovieRepository
import com.publix.pubflix.data.repository.MovieRepositoryImpl
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

object NetworkModule {

    private const val BASE_URL = "https://www.omdbapi.com/"
    private const val CONTENT_TYPE = "application/json"

    private val _json: Json = Json {
        ignoreUnknownKeys = true
    }

    private val _okHttpClient: OkHttpClient by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    private val _retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(_okHttpClient)
            .addConverterFactory(_json.asConverterFactory(CONTENT_TYPE.toMediaType()))
            .build()
    }

    val omdbApi: OmdbApi by lazy { _retrofit.create(OmdbApi::class.java) }

    val movieRepository: MovieRepository by lazy { MovieRepositoryImpl(omdbApi) }
}
