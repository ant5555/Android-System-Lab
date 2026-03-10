package com.ant.paging

interface Paginator<Key, Item> {
    suspend fun loadNextItems()
    fun reset()
}