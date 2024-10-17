package com.example.news.repository

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import com.example.news.data.remote.NewsApi
import com.example.news.data.remote.response.News
import com.example.news.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

@ActivityScoped
class NewsRepository @Inject constructor(
    private val api: NewsApi
) {

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    suspend fun getCategoryList(
        country: String,
        category: String,
        pageSize: Int,
        page: Int
    ): Flow<Resource<News>> {
        return flow {
            val response = try {
                api.getCategoryList(
                    country = country,
                    category = category,
                    pageSize = pageSize,
                    page = page
                )
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error("Error loading News"))
                return@flow
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Error loading News"))
                return@flow
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error("Error loading News"))
                return@flow
            }
            emit(Resource.Success(response))
        }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    suspend fun getSearchList(
        country: String,
        q: String
    ): Flow<Resource<News>> {
        return flow {
            val response = try {
                api.getSearchList(country, q)
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error("Error loading News"))
                return@flow
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Error loading News"))
                return@flow
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error("Error loading News"))
                return@flow
            }
            emit(Resource.Success(response))
        }
    }

}
