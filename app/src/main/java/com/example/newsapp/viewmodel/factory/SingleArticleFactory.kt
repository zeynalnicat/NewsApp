package com.example.newsapp.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.newsapp.db.RoomDb
import com.example.newsapp.viewmodel.SingleArticleViewModel

class SingleArticleFactory(private val roomDb: RoomDb):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SingleArticleViewModel(roomDb) as T
    }
}