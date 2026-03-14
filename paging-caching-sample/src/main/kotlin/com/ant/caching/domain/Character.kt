package com.ant.caching.domain

data class Character(
    val id: Int,
    val name: String,
    val species: String,
    val status: String,
    val image: String,
) {
    companion object {
        val sample = Character(
            id = 1,
            name = "Rick Sanchez",
            species = "Human",
            status = "Alive",
            image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
        )

        val sampleList = listOf(
            sample,
            sample.copy(id = 2, name = "Morty Smith"),
            sample.copy(id = 3, name = "Summer Smith"),
            sample.copy(id = 4, name = "Beth Smith", status = "Dead"),
            sample.copy(id = 5, name = "Jerry Smith")
        )
    }
}