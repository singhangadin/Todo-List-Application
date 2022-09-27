package com.example.data.di

import com.example.common.*
import com.example.data.datasource.base.TaskDataSource
import com.example.data.datasource.datastore.TaskDataStoreSource
import com.example.data.datasource.db.DBTaskDataSource
import com.example.data.datasource.db.TodoDatabase
import com.example.data.datasource.db.dao.TaskDao
import com.example.data.datasource.file.FileTaskDataSource
import com.example.data.datasource.inmemory.InMemoryTaskDataSource
import com.example.data.datasource.remote.RemoteTaskDataSource
import com.example.data.repository.CachedTaskRepository
import com.example.data.repository.DefaultTaskRepository
import com.example.domain.contract.TaskRepositoryContract
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single { get<TodoDatabase>().getTaskDao() }

    single<TaskDataSource>(qualifier = DBDataSource()) { DBTaskDataSource(get()) }

    single<TaskDataSource>(qualifier = FileDataSource()) {
        FileTaskDataSource(
            get(), androidContext().filesDir.path + "/tasks.txt"
        )
    }

    single<TaskDataSource>(qualifier = DataStoreDataSource()) {
        TaskDataStoreSource(androidContext())
    }

    single<TaskDataSource>(qualifier = InMemoryDataSource()) {
        InMemoryTaskDataSource()
    }

    single<TaskDataSource>(qualifier = RemoteDataSource()) {
        RemoteTaskDataSource(get())
    }

    single<TaskRepositoryContract>(qualifier = DefaultRepository()) {
        DefaultTaskRepository(
            taskDataSource = get(qualifier = DBDataSource()),
            logService = get(),
            ioDispatcher = get(qualifier = IODispatcher())
        )
    }

    single<TaskRepositoryContract>(qualifier = CacheRepository()) {
        CachedTaskRepository(
            tasksLocalDataSource = get(qualifier = DBDataSource()),
            tasksRemoteDataSource = get(qualifier = FileDataSource()),
            logService = get(),
            ioDispatcher = get(qualifier = IODispatcher())
        )
    }
}