package com.example.newsapp.db

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity("Bookmark")
data class BookmarkEntity(
    @PrimaryKey(autoGenerate = true)
    val id :Int =0,
    val source:String ,
    val title: String,
    val date:String ,
    val author:String,
    val imgUrl:String,
    val description:String,
    val content: String,
    val url:String,
)
