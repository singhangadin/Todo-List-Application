package com.example.data.repository

import com.example.data.datasource.db.DBTaskDataSource
import com.example.data.datasource.file.FileTaskDataSource
import com.example.domain.contract.LogService
import com.example.domain.contract.TaskRepositoryContract
import com.example.domain.entity.Task
import com.example.domain.exception.DataNotFoundException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CachedTaskRepository @Inject constructor(
    private val tasksLocalDataSource: DBTaskDataSource,
    private val tasksRemoteDataSource: FileTaskDataSource,   // MockRemote Source
    private val logService: LogService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): TaskRepositoryContract {

    private var cachedTasks: ConcurrentMap<String, Task>? = null

    override suspend fun getTasks(forceUpdate: Boolean): Result<List<Task>> = withContext(ioDispatcher) {
        if (forceUpdate) {
            return@withContext try {
                val tasks = tasksRemoteDataSource.getAllTasks()
                refreshCache(tasks)
                Result.success(tasks)
            } catch (exception: Exception) {
                Result.failure(exception)
            }
        } else {
            cachedTasks?.let { cachedTasks ->
                return@withContext Result.success(cachedTasks.values.toList())
            }
        }
        return@withContext Result.failure(IllegalStateException())
    }

    override suspend fun getTaskById(taskId: String): Result<Task> = withContext(ioDispatcher) {
        return@withContext kotlin.runCatching {
            if (cachedTasks?.get(taskId)!= null) {
                cachedTasks?.get(taskId)!!
            } else {
                val task = tasksLocalDataSource.getTaskWithId(taskId)
                task ?: throw DataNotFoundException()
            }
        }.onSuccess {
            Result.success(it)
        }.onFailure {
            logService.logException(this@CachedTaskRepository.javaClass.name, it)
            Result.failure<Throwable>(it)
        }
    }

    override suspend fun createNewTask(task: Task): Result<Task?> = withContext(ioDispatcher) {
        return@withContext kotlin.runCatching {
            tasksRemoteDataSource.insertTask(task)?.let {
                tasksLocalDataSource.insertTask(it)
                cacheTask(it)
            }
        }.onSuccess {
            Result.success(it)
        }.onFailure {
            logService.logException(this@CachedTaskRepository.javaClass.name, it)
            Result.failure<Throwable>(it)
        }
    }

    override suspend fun deleteTask(taskId: String): Result<Unit> = withContext(ioDispatcher){
        return@withContext kotlin.runCatching {
            val task = tasksRemoteDataSource.getTaskWithId(taskId)
            if (task != null) {
                cachedTasks?.remove(taskId)
                tasksLocalDataSource.removeTaskWithId(taskId)
                tasksRemoteDataSource.removeTaskWithId(taskId)
            } else {
                throw DataNotFoundException()
            }
        }.onSuccess {
            Result.success(it)
        }.onFailure {
            logService.logException(this@CachedTaskRepository.javaClass.name, it)
            Result.failure<Throwable>(it)
        }
    }

    override suspend fun updateTask(taskId: String, task: Task): Result<Task?> = withContext(ioDispatcher) {
        return@withContext kotlin.runCatching {
            val savedTask = tasksRemoteDataSource.getTaskWithId(taskId)
            if (savedTask != null) {
                tasksRemoteDataSource.updateTask(task)
                tasksLocalDataSource.updateTask(task)
                cacheTask(task)
            } else {
                throw DataNotFoundException()
            }
        }.onSuccess {
            Result.success(it)
        }.onFailure {
            logService.logException(this@CachedTaskRepository.javaClass.name, it)
            Result.failure<Throwable>(it)
        }
    }

    override suspend fun pinTask(taskId: String): Result<Unit> = withContext(ioDispatcher) {
        return@withContext kotlin.runCatching {
            val savedTask = tasksRemoteDataSource.getTaskWithId(taskId)
            if (savedTask != null) {
                tasksRemoteDataSource.pinTask(taskId)
                tasksLocalDataSource.pinTask(taskId)
                cacheTask(savedTask.copy(isPinned = true))
                Unit
            } else {
                throw DataNotFoundException()
            }
        }.onSuccess {
            Result.success(it)
        }.onFailure {
            logService.logException(this@CachedTaskRepository.javaClass.name, it)
            Result.failure<Throwable>(it)
        }
    }

    override suspend fun unPinTask(taskId: String): Result<Unit> = withContext(ioDispatcher) {
        return@withContext kotlin.runCatching {
            val savedTask = tasksRemoteDataSource.getTaskWithId(taskId)
            if (savedTask != null) {
                cacheTask(savedTask.copy(isPinned = false))
                tasksRemoteDataSource.unPinTask(taskId)
                tasksLocalDataSource.unPinTask(taskId)
            } else {
                throw DataNotFoundException()
            }
        }.onSuccess {
            Result.success(it)
        }.onFailure {
            logService.logException(this@CachedTaskRepository.javaClass.name, it)
            Result.failure<Throwable>(it)
        }
    }

    private fun refreshCache(tasks: List<Task>) {
        cachedTasks?.clear()
        tasks.forEach {
            cacheAndPerform(it) {}
        }
    }

    private fun cacheTask(cachedTask: Task): Task {
        // Create if it doesn't exist.
        if (cachedTasks == null) {
            cachedTasks = ConcurrentHashMap()
        }
        cachedTasks?.put(cachedTask.taskId, cachedTask)
        return cachedTask
    }

    private inline fun cacheAndPerform(task: Task, perform: (Task) -> Unit) {
        val cachedTask = cacheTask(task)
        perform(cachedTask)
    }
}