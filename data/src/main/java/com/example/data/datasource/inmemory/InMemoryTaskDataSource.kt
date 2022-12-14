package com.example.data.datasource.inmemory

import `in`.singhangad.shared_domain.entity.Task
import com.example.data.datasource.base.TaskDataSource
import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.delay

class InMemoryTaskDataSource : TaskDataSource {
    private val SERVICE_LATENCY_IN_MILLIS = 2000L

    private var taskData = LinkedHashMap<String, Task>()

    override suspend fun insertTask(task: Task): Task? {
        return kotlin.runCatching {
            val newTask = if (task.taskId == null) {
                task.copy(System.currentTimeMillis().toString())
            } else {
                task
            }
            taskData[newTask.taskId!!] = newTask
            delay(SERVICE_LATENCY_IN_MILLIS)
            newTask
        }.onFailure {
            throw it
        }.getOrNull()
    }

    override suspend fun deleteTask(task: Task) {
        kotlin.runCatching {
            delay(SERVICE_LATENCY_IN_MILLIS)
            taskData.remove(task.taskId!!)
        }.onFailure {
            throw it
        }
    }

    override suspend fun updateTask(task: Task): Task? {
        return kotlin.runCatching {
            delay(SERVICE_LATENCY_IN_MILLIS)
            taskData[task.taskId!!] = task
            task
        }.onFailure {
            throw it
        }.getOrNull()
    }

    override suspend fun getTaskWithId(id: String): Task? {
        try {
            delay(SERVICE_LATENCY_IN_MILLIS)
            return taskData[id]
        } catch (exception: Exception) {
            throw exception
        }
    }

    override suspend fun removeTaskWithId(id: String) {
        try {
            delay(SERVICE_LATENCY_IN_MILLIS)
            taskData.remove(id)
        } catch (exception: Exception) {
            throw exception
        }
    }

    override suspend fun pinTask(id: String) {
        try {
            delay(SERVICE_LATENCY_IN_MILLIS)
            taskData[id] = taskData[id]?.copy(isPinned = true)!!
            taskData[id]
        } catch (exception: Exception) {
            throw exception
        }
    }

    override suspend fun unPinTask(id: String) {
        try {
            taskData[id] = taskData[id]?.copy(isPinned = false)!!
            delay(SERVICE_LATENCY_IN_MILLIS)
            taskData[id]
        } catch (exception: Exception) {
            throw exception
        }
    }

    override suspend fun getAllTasks(): List<Task> {
        try {
            delay(SERVICE_LATENCY_IN_MILLIS)
            return taskData.values.toList()
        } catch (exception: Exception) {
            throw exception
        }
    }

    override suspend fun deleteAllTasks() {
        taskData.clear()
    }

    @VisibleForTesting
    fun clear() {
        taskData.clear()
    }
}