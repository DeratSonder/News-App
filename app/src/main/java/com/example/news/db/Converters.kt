package com.example.news.db

import androidx.room.TypeConverter
import com.example.news.data.remote.response.Source


class Converters {

    @TypeConverter
    fun fromSource(source: Source?): String {
        return source?.name ?: ""
    }

    @TypeConverter
    fun toSource(name: String?): Source? {
        return if (name.isNullOrEmpty()) {
            null
        } else {
            Source(name, name)
        }
    }
}