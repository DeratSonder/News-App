package com.example.news.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.news.data.remote.response.Article
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavouriteArticle(article: Article)

    @Query("SELECT * FROM articles")
    fun getAllFavouriteArticle(): Flow<List<Article>>

    @Query("SELECT * FROM articles WHERE title = :title")
    fun getFavouriteArticleByTitle(title: String):Flow<List<Article>>

    @Delete
    suspend fun deleteFavouriteArticle(article: Article)

}