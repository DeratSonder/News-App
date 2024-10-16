package com.example.news.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.news.data.remote.response.Article

@Database(
    entities = [Article::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class NewsDatabase: RoomDatabase() {

    abstract fun newsDao(): NewsDao


}