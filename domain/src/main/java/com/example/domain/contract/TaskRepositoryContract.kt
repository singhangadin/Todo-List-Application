package com.example.domain.contract

import com.example.domain.entity.Task

interface TaskRepositoryContract {

    suspend fun getTasks(forceUpdate: Boolean): Result<List<Task>>

    suspend fun getTaskById(taskId: String): Result<Task>

    suspend fun createNewTask(task: Task): Result<Task?>

    suspend fun deleteTask(taskId: String): Result<Unit>

    suspend fun updateTask(taskId: String, task: Task): Result<Task?>

    suspend fun pinTask(taskId: String): Result<Unit>

    suspend fun unPinTask(taskId: String): Result<Unit>
}