package com.example.newsapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [BookmarkEntity::class], version = 1)
abstract class RoomDb : RoomDatabase() {

    abstract fun bookmarkDao():BookmarkDao

    companion object {
        private var INSTANCE: RoomDb? = null
        fun accessDb(context: Context):RoomDb? {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        RoomDb::class.java,
                        "NewsApp"
                    ).build()
                }

            }
            return INSTANCE
        }
    }

}