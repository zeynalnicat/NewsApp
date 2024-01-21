package com.example.newsapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.db.BookmarkEntity
import com.example.newsapp.db.RoomDb
import com.example.newsapp.resource.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookmarkViewModel(private val roomDb: RoomDb) : ViewModel() {

    private val _bookmarks = MutableLiveData<Resource<List<BookmarkEntity>>>()

    val bookmarks: LiveData<Resource<List<BookmarkEntity>>>
        get() = _bookmarks


    fun getBookmarks() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val bookmarkDao = roomDb.bookmarkDao()
                val selectAll = bookmarkDao.getAll()
                if (!selectAll.isEmpty()) {
                    _bookmarks.postValue(Resource.Success(selectAll))
                }

            } catch (e: Exception) {
                _bookmarks.postValue(Resource.Error(e))
            }

        }
    }
}