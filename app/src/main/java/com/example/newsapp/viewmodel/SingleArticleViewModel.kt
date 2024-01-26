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
    private val _check = MutableLiveData<Resource<Boolean>>()
    val insertion: LiveData<Resource<Long>>
        get() = _insertion

    val check: LiveData<Resource<Boolean>>
        get() = _check


    fun insert(bookmarkEntity: BookmarkEntity) {
        checkDb(bookmarkEntity.title)
        val bookmarkDao = roomDb.bookmarkDao()
        if (_check.value == Resource.Success(false))
            viewModelScope.launch(Dispatchers.IO) {
                try {

                    val isSuccessful = bookmarkDao.insert(bookmarkEntity)
                    if (isSuccessful != -1L) {
                        _insertion.postValue(Resource.Success(isSuccessful))
                        _check.postValue(Resource.Success(true))
                    } else {
                        _insertion.postValue(Resource.Error(Exception("There was an error while insertion")))
                    }
                } catch (e: Exception) {
                    _insertion.postValue(Resource.Error(e))
                }
            }
        else {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    bookmarkDao.delete(bookmarkEntity.title)
                    _insertion.postValue(Resource.Success(0L))
                    _check.postValue(Resource.Success(false))
                } catch (e: Exception) {
                    _check.postValue(Resource.Error(e))
                }
            }
        }
    }


    fun checkDb(title: String) {
        _check.postValue(Resource.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val bookmarkDao = roomDb.bookmarkDao()
                val count = bookmarkDao.checkDb(title)
                if (count > 0) {
                    _check.postValue(Resource.Success(true))
                } else {
                    _check.postValue(Resource.Success(false))
                }

            } catch (e: Exception) {
                _check.postValue(Resource.Error(e))
            }
        }
    }

}