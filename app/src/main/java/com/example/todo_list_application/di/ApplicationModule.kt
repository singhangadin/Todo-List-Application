package com.example.todo_list_application.di

import android.content.Context
import androidx.room.Room
import com.example.data.repository.AndroidLogService
import com.example.data.repository.CachedTaskRepository
import com.example.data.repository.DefaultTaskRepository
import com.example.data.datasource.base.TaskDataSource
import com.example.data.datasource.db.DBTaskDataSource
import com.example.data.datasource.db.TodoDatabase
import com.example.data.datasource.db.dao.TaskDao
import com.example.data.datasource.file.FileTaskDataSource
import com.example.data.datasource.inmemory.InMemoryTaskDataSource
import com.example.data.datasource.remote.RemoteTaskDataSource
import com.example.data.datasource.remote.service.TodoService
import com.example.domain.contract.LogService
import com.example.domain.contract.TaskRepositoryContract
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Singleton
    @Provides
    fun provideRoomDatabase(@ApplicationContext context: Context): TodoDatabase {
        return Room.databaseBuilder(
            context,
            TodoDatabase::class.java,
            "todo_database"
        ).build()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer <Auth Token>") // TODO: ADD AUTH TOKEN HERE
                    .build()
                    .let(chain::proceed)
            }
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://api-nodejs-todolist.herokuapp.com")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Singleton
    @Provides
    fun provideRemoteTaskService(retrofit: Retrofit): TodoService {
        return retrofit.create(TodoService::class.java)
    }

    @Singleton
    @Provides
    fun provideIODispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    @Singleton
    @Provides
    fun provideTaskDBDao(todoDatabase: TodoDatabase): TaskDao {
        return todoDatabase.getTaskDao()
    }

    @Provides
    fun provideTaskRepository(ioDispatcher: CoroutineDispatcher, taskDataSource: InMemoryTaskDataSource, logService: LogService): DefaultTaskRepository {
        return DefaultTaskRepository(
            taskDataSource,
            logService,
            ioDispatcher
        )
    }

    @Singleton
    @Provides
    fun provideLogService(): LogService {
        return AndroidLogService()
    }

    @Singleton
    @Provides
    fun provideDBTaskDataSource(taskDao: TaskDao): DBTaskDataSource {
        return DBTaskDataSource(taskDao)
    }

    @Singleton
    @Provides
    fun provideFileTaskDataSource(gson: Gson, @ApplicationContext context: Context): FileTaskDataSource {
        return FileTaskDataSource(gson, context.filesDir.path + "/tasks.txt")
    }

    @Singleton
    @Provides
    fun provideCacheRepository(
        tasksLocalDataSource: DBTaskDataSource,
        tasksRemoteDataSource: FileTaskDataSource,
        logService: LogService,
        ioDispatcher: CoroutineDispatcher
    ): TaskRepositoryContract {
        return CachedTaskRepository(tasksLocalDataSource, tasksRemoteDataSource, logService, ioDispatcher)
    }

    @Singleton
    @Provides
    fun provideRemoteDataSource(todoService: TodoService): TaskDataSource {
        return RemoteTaskDataSource(todoService)
    }

    @Singleton
    @Provides
    fun provideInMemoryDataSource(): TaskDataSource {
        return InMemoryTaskDataSource()
    }

    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder().create()
}