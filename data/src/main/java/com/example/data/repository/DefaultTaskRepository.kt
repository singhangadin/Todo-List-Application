package com.example.data.repository

import com.example.common.DBDataSource
import com.example.common.InMemoryDataSource
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
    @DBDataSource private val taskDataSource: TaskDataSource,
    private val logService: LogService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): TaskRepositoryContract {


    override suspend fun getTasks(forceUpdate: Boolean): Result<List<Task>> = withContext(ioDispatcher) {
        TODO()
    }

    override suspend fun getTaskById(taskId: String): Result<Task> = withContext(ioDispatcher) {
        return@withContext kotlin.runCatching {
            val task = taskDataSource.getTaskWithId(taskId)
            task ?: throw DataNotFoundException()
        }.onSuccess {
            Result.success(it)
        }.onFailure {
            logService.logException(this@DefaultTaskRepository.javaClass.name, it)
            Result.failure<Throwable>(it)
        }
    }

    override suspend fun createNewTask(task: Task): Result<Task?> = withContext(ioDispatcher) {
        TODO()
    }

    override suspend fun deleteTask(taskId: String): Result<Unit> = withContext(ioDispatcher) {
        TODO()
    }

    override suspend fun updateTask(taskId: String, task: Task): Result<Task?> = withContext(ioDispatcher) {
        return@withContext kotlin.runCatching {
            val savedTask = taskDataSource.getTaskWithId(taskId)
            if (savedTask != null) {
                taskDataSource.updateTask(task)
            } else {
                throw DataNotFoundException()
            }
        }.onSuccess {
            Result.success(it)
        }.onFailure {
            logService.logException(this@DefaultTaskRepository.javaClass.name, it)
            Result.failure<Throwable>(it)
        }
    }

    override suspend fun pinTask(taskId: String): Result<Unit> = withContext(ioDispatcher) {
        return@withContext kotlin.runCatching {
            val savedTask = taskDataSource.getTaskWithId(taskId)
            if (savedTask != null) {
                taskDataSource.pinTask(taskId)
            } else {
                throw DataNotFoundException()
            }
        }.onSuccess {
            Result.success(it)
        }.onFailure {
            logService.logException(this@DefaultTaskRepository.javaClass.name, it)
            Result.failure<Throwable>(it)
        }
    }

    override suspend fun unPinTask(taskId: String): Result<Unit> = withContext(ioDispatcher) {
        TODO()
    }
}