package com.example.todo_list_application.di

import androidx.room.Room
import com.example.common.*
import com.example.data.repository.AndroidLogService
import com.example.data.datasource.db.TodoDatabase
import com.example.data.datasource.remote.service.TodoService
import com.example.domain.contract.LogService
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer <Auth Token>") // TODO: ADD AUTH TOKEN HERE
                    .build()
                    .let(chain::proceed)
            }
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
            .build()
    }

    single { GsonBuilder().create() }

    single {
        Retrofit.Builder()
        .client(get())
        .baseUrl("https://api-nodejs-todolist.herokuapp.com")
        .addConverterFactory(GsonConverterFactory.create(get()))
        .build()
    }

    single<TodoService> {
        get<Retrofit>().create(TodoService::class.java)
    }

    single {
        Room.databaseBuilder(
            androidContext(),
            TodoDatabase::class.java,
            "todo_database"
        ).build()
    }
    single(qualifier = IODispatcher()) { Dispatchers.IO }

    single(qualifier = DefaultDispatcher()) { Dispatchers.Default }

    single<LogService> { AndroidLogService() }
}