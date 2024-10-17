package com.example.news.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.data.remote.response.Article
import com.example.news.repository.FavouriteNewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouriteNewsViewModel @Inject constructor(
    private val repository: FavouriteNewsRepository
) : ViewModel() {

    val favouriteNewsList: StateFlow<List<Article>> = repository.getAllFavouriteArticle().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    fun  getFavouriteArticleByTitle(title: String) = repository.getFavouriteArticleByTitle(title)

    fun insertFavouriteArticle(article: Article) = viewModelScope.launch {
        repository.insertFavouriteArticle(article)
    }

    fun deleteFavouriteArticle(article: Article) = viewModelScope.launch {
        repository.deleteFavouriteArticle(article)
    }

}