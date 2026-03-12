package com.ant.caching.data.mappers

import com.ant.caching.data.local.CharacterEntity
import com.ant.caching.data.remote.CharacterDto
import com.ant.caching.domain.Character

fun CharacterDto.toCharacterEntity(): CharacterEntity {
    return CharacterEntity(
        id = id,
        name = name,
        species = species,
        status = status,
        image = image
    )
}

fun CharacterEntity.toCharacter(): Character {
    return Character(
        id = id,
        name = name,
        species = species,
        status = status,
        image = image
    )
}