package `in`.singhangad.shared_data.datasource.remote.service

import `in`.singhangad.shared_data.datasource.remote.entity.ApiResponse
import `in`.singhangad.shared_data.datasource.remote.entity.CreateTaskRequest
import `in`.singhangad.shared_data.datasource.remote.entity.UpdateTaskRequest


interface TodoService {
    suspend fun addTask(task: CreateTaskRequest): ApiResponse

    suspend fun deleteTask(taskId: Long): ApiResponse

    suspend fun updateTask(taskId: Long, task: UpdateTaskRequest): ApiResponse

    suspend fun getTaskWithId(taskId: Long): ApiResponse

    suspend fun removeTaskWithId(taskId: Long): ApiResponse

    suspend fun pinTask(taskId: Long, isPinned: Boolean): ApiResponse

    suspend fun unPinTask(taskId: Long, isPinned: Boolean): ApiResponse

    suspend fun getAllTasks(): ApiResponse
}