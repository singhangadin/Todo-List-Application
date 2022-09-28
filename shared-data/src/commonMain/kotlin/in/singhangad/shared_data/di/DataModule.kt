package `in`.singhangad.shared_data.di

import `in`.singhangad.shared_data.database.dao.TaskDao
import `in`.singhangad.shared_data.database.dao.TaskDaoImpl
import `in`.singhangad.shared_data.database.TodoDatabase
import org.koin.dsl.module

val sharedDataModule = module {

    single { get<TodoDatabase>().todoDatabaseQueries }

    single<TaskDao> { TaskDaoImpl(get()) }
}