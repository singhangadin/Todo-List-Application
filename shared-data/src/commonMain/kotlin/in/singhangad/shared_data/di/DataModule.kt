package `in`.singhangad.shared_data.di

import `in`.singhangad.shared_common.*
import `in`.singhangad.shared_data.datasource.base.TaskDataSource
import `in`.singhangad.shared_data.datasource.database.DBTaskDataSource
import `in`.singhangad.shared_data.datasource.database.dao.TaskDao
import `in`.singhangad.shared_data.datasource.database.dao.TaskDaoImpl
import `in`.singhangad.shared_data.database.TodoDatabase
import `in`.singhangad.shared_data.datasource.inmemory.InMemoryTaskDataSource
import `in`.singhangad.shared_data.datasource.remote.RemoteTaskDataSource
import `in`.singhangad.shared_data.datasource.remote.httpClient
import `in`.singhangad.shared_data.datasource.remote.json
import `in`.singhangad.shared_data.datasource.remote.service.TodoService
import `in`.singhangad.shared_data.datasource.remote.service.TodoServiceImpl
import `in`.singhangad.shared_data.repository.CachedTaskRepository
import `in`.singhangad.shared_data.repository.DefaultTaskRepository
import `in`.singhangad.shared_domain.contract.TaskRepositoryContract
import org.koin.dsl.module

val sharedDataModule = module {

    single { get<TodoDatabase>().todoDatabaseQueries }

    single<TaskDao> { TaskDaoImpl(get()) }

    single { json }

    single { httpClient }

    single<TodoService> { TodoServiceImpl(get(), get()) }

    single<TaskDataSource>(qualifier = DBDataSource()) { DBTaskDataSource(get()) }

    single<TaskDataSource>(qualifier = InMemoryDataSource()) { InMemoryTaskDataSource() }

    single<TaskDataSource>(qualifier = RemoteDataSource()) { RemoteTaskDataSource(get()) }

    single<TaskRepositoryContract>(qualifier = DefaultRepository()) {
        DefaultTaskRepository(
            taskDataSource = get(qualifier = RemoteDataSource()),
            logService = get(),
            ioDispatcher = get(qualifier = IODispatcher())
        )
    }

    single<TaskRepositoryContract>(qualifier = CacheRepository()) {
        CachedTaskRepository(
            tasksLocalDataSource = get(qualifier = DBDataSource()),
            tasksRemoteDataSource = get(qualifier = InMemoryDataSource()),
            logService = get(),
            ioDispatcher = get(qualifier = IODispatcher())
        )
    }
}