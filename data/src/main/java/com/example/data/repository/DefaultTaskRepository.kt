package com.example.data.repository

import com.example.data.datasource.base.TaskDataSource
import com.example.domain.contract.LogService
import com.example.domain.contract.TaskRepositoryContract
import com.example.domain.entity.Task
import com.example.domain.exception.DataNotFoundException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultTaskRepository @Inject constructor(
    private val taskDataSource: TaskDataSource,
    private val logService: LogService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): TaskRepositoryContract {


    override suspend fun getTasks(forceUpdate: Boolean): Result<List<Task>> = withContext(ioDispatcher) {
        TODO()
    }

    override suspend fun getTaskById(taskId: String): Result<Task> = withContext(ioDispatcher) {
        TODO()
    }

    override suspend fun createNewTask(task: Task): Result<Task?> = withContext(ioDispatcher) {
        TODO()
    }

    override suspend fun deleteTask(taskId: String): Result<Unit> = withContext(ioDispatcher) {
        TODO()
    }

    override suspend fun updateTask(taskId: String, task: Task): Result<Task?> = withContext(ioDispatcher) {
        TODO()
    }

    override suspend fun pinTask(taskId: String): Result<Unit> = withContext(ioDispatcher) {
        TODO()
    }

    override suspend fun unPinTask(taskId: String): Result<Unit> = withContext(ioDispatcher) {
        TODO()
    }
}