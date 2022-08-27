package com.example.data.datasource.base

import com.example.domain.entity.Task

interface TaskDataSource {
    suspend fun insertTask(task: Task): Task?

    suspend fun deleteTask(task: Task)

    suspend fun updateTask(task: Task): Task?

    suspend fun getTaskWithId(id: String): Task?

    suspend fun removeTaskWithId(id: String)

    suspend fun pinTask(id: String)

    suspend fun unPinTask(id: String)

    suspend fun getAllTasks(): List<Task>

    suspend fun deleteAllTasks()
}