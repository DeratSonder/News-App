package com.example.news.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.data.remote.response.Article
import com.example.news.repository.NewsRepository
import com.example.news.util.Constants.PAGE_SIZE
import com.example.news.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject


@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class NewsListViewModel @Inject constructor(
    private val repository: NewsRepository
) : ViewModel() {

    private var curPage = 0

    var isLoading = mutableStateOf(false)
    var loadError = mutableStateOf("")
    var endReached = mutableStateOf(false)

    var stateOfTabRow = mutableIntStateOf(0)
    var categoryValue = mutableStateOf("general")
    var countryCode = mutableStateOf("us")

    private var _newsList = MutableStateFlow<List<Article>>(emptyList())
    val newsList = _newsList.asStateFlow()


    fun setCategory(value: String) {
        viewModelScope.launch {
            categoryValue.value = value
        }
    }

    fun setCountryCode(value: String) {
        viewModelScope.launch {
            countryCode.value = value
        }
    }

    fun setStateOfTabRow(value: Int) {
        viewModelScope.launch {
            stateOfTabRow.intValue = value
        }
    }

    fun setCurPage() {
        viewModelScope.launch {
            curPage = 1
        }
    }

    fun clearNewsList() {
        viewModelScope.launch {
            _newsList.value = emptyList()
        }
    }

    private fun isArticleAccepted(article: Article): Boolean {
        return with(article) {
            title != null &&
                    content != null &&
                    urlToImage != null &&
                    description != null &&
                    publishedAt != null &&
                    source?.name != null &&
                    url != null
        }
    }

    private fun getDateTime(time: String): LocalDateTime {

        val dateTime = LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME)

        return LocalDateTime.of(
            dateTime.year,
            dateTime.monthValue,
            dateTime.dayOfMonth,
            dateTime.hour,
            dateTime.minute,
            dateTime.second
        )
    }

    fun defineTimePublish(time: String): String {

        val timeBegin = getDateTime(time)

        val currentTime = LocalDateTime.now()

        val timeDifference = Duration.between(timeBegin, currentTime)

        return when (val daysDifference = timeDifference.toDays().toInt()) {
            0 -> "Today"
            1 -> "Yesterday"
            else -> "$daysDifference days ago"
        }
    }

    fun searchNewsList(q: String) {
        viewModelScope.launch(Dispatchers.Default) {
            repository.getSearchList(
                country = countryCode.value,
                q = q
            ).collectLatest { result ->
                when (result) {
                    is Resource.Success -> {

                        _newsList.value = result.data!!.articles.filter {
                            isArticleAccepted(it)
                        }

                        isLoading.value = false

                    }

                    is Resource.Error -> {
                        loadError.value = result.message!!
                        isLoading.value = false
                    }

                    is Resource.Loading -> {
                        isLoading.value = true
                    }

                }
            }
        }
    }

    fun loadCategoryNewsPaginated(category: String) {
        viewModelScope.launch {
            repository.getCategoryList(
                country = countryCode.value,
                category = category,
                pageSize = PAGE_SIZE,
                page = curPage
            ).collectLatest { result ->
                when (result) {
                    is Resource.Success -> {

                        endReached.value = curPage * PAGE_SIZE >= result.data!!.totalResults

                        val article = result.data.articles.filter {
                            isArticleAccepted(it)
                        }

                        curPage++

                        loadError.value = ""
                        isLoading.value = false
                        _newsList.value += article
                    }

                    is Resource.Error -> {
                        loadError.value = result.message!!
                        isLoading.value = false
                    }

                    is Resource.Loading -> {
                        isLoading.value = true
                    }
                }
            }
        }
    }


}