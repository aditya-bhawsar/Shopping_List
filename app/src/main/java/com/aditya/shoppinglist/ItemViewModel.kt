package com.aditya.shoppinglist

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ItemViewModel(application: Application) : AndroidViewModel(application) {

    private val itemRepository:ItemRepository

    val listedItems = MediatorLiveData<List<Item>>()
    private var currentSource:LiveData<List<Item>>

    init {
        val itemDao = ItemDatabase.getDatabase(application).ItemDao()
        itemRepository = ItemRepository(itemDao)
        currentSource = itemRepository.allItems
        listedItems.addSource(currentSource){result-> result?.let { listedItems.value = it } }
    }

    fun queryIt(text:String?){
        if(text.isNullOrEmpty()){ if(currentSource!=itemRepository.allItems){ switchSource(itemRepository.allItems) } }
        else{ switchSource(itemRepository.search(text)) }
    }

    private fun switchSource(newSource :LiveData<List<Item>>){
        val toRemoveSource = currentSource
        currentSource = newSource
        listedItems.addSource(currentSource){result-> result?.let { listedItems.value = it } }
        listedItems.removeSource(toRemoveSource)
    }

    /*private var allItems : MutableLiveData<List<Item>> = MutableLiveData()
    val _allItems: LiveData<List<Item>>
        get() = allItems

    fun queryIt(text:String){ allItems = if(text!="") itemRepository.search(text) else itemRepository.allItems }

    fun queryIt(text:String)= viewModelScope.launch{
        if(text!="")
            allItems.postValue(itemRepository.search(text).value)
        else
            allItems.postValue(itemRepository.allItems.value)
    }*/

    fun insert(item: Item) = viewModelScope.launch(Dispatchers.IO) { itemRepository.insert(item) }
    fun delete(item: Item) = viewModelScope.launch(Dispatchers.IO) { itemRepository.delete(item) }
    fun update(item: Item) = viewModelScope.launch(Dispatchers.IO) { itemRepository.update(item) }
}