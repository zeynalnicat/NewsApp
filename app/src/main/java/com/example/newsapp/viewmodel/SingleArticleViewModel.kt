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

class SingleArticleViewModel(private val roomDb: RoomDb) : ViewModel() {
    private val _insertion = MutableLiveData<Resource<Long>>()
    private val _check = MutableLiveData<Resource<Int>>()
    val insertion: LiveData<Resource<Long>>
        get() = _insertion

    val check: LiveData<Resource<Int>>
        get() = _check


    fun insert(bookmarkEntity: BookmarkEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val bookmarkDao = roomDb.bookmarkDao()

                val count = bookmarkDao.checkDb(bookmarkEntity.title)

                if (count == 0) {
                    val isSuccessful = bookmarkDao.insert(bookmarkEntity)
                    if (isSuccessful != -1L) {
                        _insertion.postValue(Resource.Success(isSuccessful))
                    } else {
                        _insertion.postValue(Resource.Error(Exception("There was an error while insertion")))
                    }
                } else {
                    bookmarkDao.delete(bookmarkEntity)
                    _insertion.postValue(Resource.Success(-1))
                }
            } catch (e: Exception) {
                _insertion.postValue(Resource.Error(e))
            }
        }
    }

    fun checkDb(title: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val bookmarkDao = roomDb.bookmarkDao()
                val count = bookmarkDao.checkDb(title)
                _check.postValue(Resource.Success(count))
            } catch (e: Exception) {
                _check.postValue(Resource.Error(e))
            }
        }
    }
}