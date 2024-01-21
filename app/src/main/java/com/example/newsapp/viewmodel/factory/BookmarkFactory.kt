package com.example.newsapp.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.newsapp.db.RoomDb
import com.example.newsapp.viewmodel.BookmarkViewModel

class BookmarkFactory(private val roomDb: RoomDb):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return BookmarkViewModel(roomDb) as T
    }
}