package com.example.news.di

import android.content.Context
import androidx.room.Room
import com.example.news.data.remote.NewsApi
import com.example.news.db.NewsDao
import com.example.news.db.NewsDatabase
import com.example.news.repository.FavouriteNewsRepository
import com.example.news.repository.NewsRepository
import com.example.news.util.Constants.API_URL
import com.example.news.util.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideNewsRepository(
        api: NewsApi
    ) = NewsRepository(api)

    @Singleton
    @Provides
    fun provideNewsApi(): NewsApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(API_URL)
            .build()
            .create(NewsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context)
    = Room.databaseBuilder(context, NewsDatabase::class.java, DATABASE_NAME).build()

    @Provides
    @Singleton
    fun provideRepository(database: NewsDatabase): FavouriteNewsRepository {
        return FavouriteNewsRepository(database.newsDao())
    }

    @Provides
    @Singleton
    fun provideNewsDao(database: NewsDatabase): NewsDao {
        return database.newsDao()
    }

}