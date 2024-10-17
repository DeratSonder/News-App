package com.example.news.repository

import com.example.news.data.remote.response.Article
import com.example.news.db.NewsDao
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class FavouriteNewsRepository @Inject constructor(
    private val dao: NewsDao
) {

    suspend fun insertFavouriteArticle(article: Article) = dao.insertFavouriteArticle(article)

    suspend fun deleteFavouriteArticle(article: Article) = dao.deleteFavouriteArticle(article)

    fun getFavouriteArticleByTitle(title: String) = dao.getFavouriteArticleByTitle(title)

    fun getAllFavouriteArticle() = dao.getAllFavouriteArticle()

}