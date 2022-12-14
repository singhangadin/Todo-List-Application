package com.example.todo_list_application.di

import `in`.singhangad.shared_common.DefaultDispatcher
import `in`.singhangad.shared_common.IODispatcher
import `in`.singhangad.shared_data.datasource.database.factory.DatabaseDriverFactory
import `in`.singhangad.shared_domain.contract.LogService
import `in`.singhangad.shared_data.service.CommonLogService
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import `in`.singhangad.shared_data.database.TodoDatabase as SharedTodoDatabase

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

    single {
        val dbDriver = DatabaseDriverFactory(
            androidContext()
        ).createDriver()
        SharedTodoDatabase(dbDriver)
    }

    single(qualifier = IODispatcher()) { Dispatchers.IO }

    single(qualifier = DefaultDispatcher()) { Dispatchers.Default }

    single<LogService> { CommonLogService() }
}