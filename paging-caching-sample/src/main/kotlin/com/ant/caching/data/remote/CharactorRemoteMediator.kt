package com.ant.caching.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.ant.caching.data.local.CharacterDatabase
import com.ant.caching.data.local.CharacterEntity
import com.ant.caching.data.mappers.toCharacterEntity
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class CharacterRemoteMediator (
    private val characterDb: CharacterDatabase,
    private val characterApi: CharacterApi
): RemoteMediator<Int, CharacterEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CharacterEntity>
    ): MediatorResult {
        return try {
            val loadKey = when(loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(
                    endOfPaginationReached = true
                )
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if(lastItem == null) {
                        1
                    } else {
                        (state.pages.sumOf { it.data.size } / 20) + 1
                    }
                }
            }

            val response = characterApi.getCharacters(page = loadKey)
            val items = response.results

            characterDb.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    characterDb.dao.clearAll()
                }
                val characterEntities = items.map { it.toCharacterEntity() }
                characterDb.dao.upsertAll(characterEntities)
            }

            MediatorResult.Success(
                endOfPaginationReached = items.isEmpty()
            )
        } catch(e: IOException) {
            MediatorResult.Error(e)
        } catch(e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}