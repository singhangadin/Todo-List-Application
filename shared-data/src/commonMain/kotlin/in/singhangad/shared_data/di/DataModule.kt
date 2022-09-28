package `in`.singhangad.shared_data.di

import `in`.singhangad.shared_data.database.TaskDao
import `in`.singhangad.shared_data.database.TaskDaoImpl
import `in`.singhangad.shared_data.database.TodoDatabase
import org.koin.dsl.module

val sharedDataModule = module {

    single { get<TodoDatabase>().todoDatabaseQueries }

    single<TaskDao> { TaskDaoImpl(get()) }
}