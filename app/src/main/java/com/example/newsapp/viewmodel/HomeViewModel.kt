package com.example.newsapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.api.NewsApi
import com.example.newsapp.api.RetrofitInstance
import com.example.newsapp.model.Article
import com.example.newsapp.resource.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _topHeadlines = MutableLiveData<Resource<List<Article>>>()
    private val newsApi = RetrofitInstance.getInstance()?.create(NewsApi::class.java)!!

    val topHeadlines: LiveData<Resource<List<Article>>>
        get() = _topHeadlines


    fun getTopHeadlines() {
        _topHeadlines.postValue(Resource.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = newsApi.getTop("us")
                if (response.isSuccessful) {
                    val articles = response.body()?.articles
                    articles?.let {
                        _topHeadlines.postValue(Resource.Success(it))
                    }

                } else {
                    _topHeadlines.postValue(Resource.Error(Exception("There was an error while handling the data")))
                }
            } catch (e: Exception) {
                _topHeadlines.postValue(Resource.Error(e))
            }
        }
    }
}