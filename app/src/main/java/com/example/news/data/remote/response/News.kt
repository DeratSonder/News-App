package com.example.news.data.remote.response

data class News(
    var articles: List<Article>,
    var status: String,
    var totalResults: Int
)
