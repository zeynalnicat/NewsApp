package com.example.newsapp.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BookmarkDao {



    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(bookmarkEntity: BookmarkEntity):Long


    @Query("Select count(*) from bookmark where title=:title")
    suspend fun checkDb(title:String):Int


    @Query("Delete from bookmark where title=:title")
    suspend fun delete(title: String)

    @Query("Select * from bookmark")
    suspend fun getAll():List<BookmarkEntity>
}