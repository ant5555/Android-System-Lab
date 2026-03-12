package com.ant.caching.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CharacterEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val species: String,
    val status: String,
    val image: String,
)
