package com.ant.caching.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface CharacterApi {
    @GET("character")
    suspend fun getCharacters(
        @Query("page") page: Int,
    ): CharacterResponse

    companion object {
        const val BASE_URL = "https://rickandmortyapi.com/api/"
    }
}