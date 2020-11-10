package com.aditya.shoppinglist

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "item_table")
class Item(
    @PrimaryKey(autoGenerate = true) val id :Int,
    @ColumnInfo(name = "name") val name :String,
    @ColumnInfo(name = "amount") val amount : String
)