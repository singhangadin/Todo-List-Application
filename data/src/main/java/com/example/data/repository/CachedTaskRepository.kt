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
        TODO()
    }

    override suspend fun getTaskById(taskId: String): Result<Task> = withContext(ioDispatcher) {
        TODO()
    }

    override suspend fun createNewTask(task: Task): Result<Task?> = withContext(ioDispatcher) {
        TODO()
    }

    override suspend fun deleteTask(taskId: String): Result<Unit> = withContext(ioDispatcher){
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