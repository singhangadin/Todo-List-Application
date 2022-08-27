package com.example.data.remote.network.source

import com.example.data.common.TaskDataSource
import com.example.data.remote.network.source.entity.TaskRequest
import com.example.data.remote.network.source.entity.toTask
import com.example.data.remote.network.source.entity.toTaskRequest
import com.example.data.remote.network.source.service.TodoService
import com.example.domain.entity.Task
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteTaskDataSource @Inject constructor(private val todoService: TodoService):
    TaskDataSource {
    override suspend fun insertTask(task: Task): Task? {
        return kotlin.runCatching {
            todoService.addTask(task.toTaskRequest())
        }.onFailure {
            throw it
        }.getOrNull()?.body()?.toTask()
    }

    override suspend fun deleteTask(task: Task) {
        kotlin.runCatching {
            todoService.deleteTask(task.taskId!!)
        }.onFailure {
            throw it
        }
    }

    override suspend fun updateTask(task: Task): Task? {
        return kotlin.runCatching {
            todoService.updateTask(task.taskId!!, task.toTaskRequest())
        }.onFailure {
            throw it
        }.getOrNull()?.body()?.toTask()
    }

    override suspend fun getTaskWithId(id: String): Task? {
        try {
            return todoService.getTaskWithId(id).body()?.toTask()
        } catch (exception: Exception) {
            throw exception
        }
    }

    override suspend fun removeTaskWithId(id: String) {
        try {
            return todoService.removeTaskWithId(id)
        } catch (exception: Exception) {
            throw exception
        }
    }

    override suspend fun pinTask(id: String) {
        try {
            todoService.pinTask(id, TaskRequest(completed = true))
        } catch (exception: Exception) {
            throw exception
        }
    }

    override suspend fun unPinTask(id: String) {
        try {
            todoService.pinTask(id, TaskRequest(completed = false))
        } catch (exception: Exception) {
            throw exception
        }
    }

    override suspend fun getAllTasks(): List<Task> {
        try {
            return todoService.getAllTasks().data.map {
                it.toTask()
            }
        } catch (exception: Exception) {
            throw exception
        }
    }

    override suspend fun deleteAllTasks() {

    }
}