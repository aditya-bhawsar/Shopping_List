package com.aditya.shoppinglist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ItemViewModel(application: Application) : AndroidViewModel(application) {

    private val itemRepository:ItemRepository
    var allItems :LiveData<List<Item>>

    init {
        val itemDao = ItemDatabase.getDatabase(application).ItemDao()
        itemRepository = ItemRepository(itemDao)
        allItems = itemRepository.allItems
    }

    fun queryIt(text:String){ allItems = if(text!="") itemRepository.search(text) else itemRepository.allItems }
    fun insert(item: Item) = viewModelScope.launch(Dispatchers.IO) { itemRepository.insert(item) }
    fun delete(item: Item) = viewModelScope.launch(Dispatchers.IO) { itemRepository.delete(item) }
    fun update(item: Item) = viewModelScope.launch(Dispatchers.IO) { itemRepository.update(item) }
}