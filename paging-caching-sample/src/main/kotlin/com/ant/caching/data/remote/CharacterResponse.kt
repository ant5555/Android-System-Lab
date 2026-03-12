package com.ant.caching.data.remote

data class CharacterResponse(
    val results: List<CharacterDto>
)

data class CharacterDto(
    val id: Int,
    val name: String,
    val species: String,
    val status: String,
    val image: String
)