package com.aditya.shoppinglist

import androidx.lifecycle.LiveData

class ItemRepository(private val itemDao: ItemDao) {

    val allItems :LiveData<List<Item>> = itemDao.getAllItems()

    fun search(text: String): LiveData<List<Item>> = itemDao.search(text)

    suspend fun insert(item: Item){ itemDao.insert(item) }
    suspend fun update(item: Item){ itemDao.updateItem(item)}
    suspend fun delete(item: Item){ itemDao.deleteItem(item)}

}