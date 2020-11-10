package com.aditya.shoppinglist

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface ItemDao {

    @Query("SELECT * from item_table ORDER BY id")
    fun getAllItems(): LiveData<List<Item>>

    @Query("Select * from item_table where name LIKE :text")
    fun search(text: String) : LiveData<List<Item>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Item)

    @Update
    suspend fun updateItem(item: Item): Int

    @Delete
    suspend fun deleteItem(item: Item)
}