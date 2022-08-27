package com.example.data.remote.network.source.service

import com.example.data.remote.network.source.entity.AllTaskResponse
import com.example.data.remote.network.source.entity.TaskRequest
import com.example.data.remote.network.source.entity.TaskResponse
import retrofit2.Response
import retrofit2.http.*

interface TodoService {
    @POST("/task")
    suspend fun addTask(@Body task: TaskRequest): Response<TaskResponse>

    @DELETE("/task/{taskId}")
    suspend fun deleteTask(@Path("taskId") taskId: String)

    @PUT("/task/{taskId}")
    suspend fun updateTask(@Path("taskId") taskId: String, @Body task: TaskRequest): Response<TaskResponse>

    @GET("/task/{taskId}")
    suspend fun getTaskWithId(@Path("taskId") taskId: String): Response<TaskResponse>

    @DELETE("/task/{taskId}")
    suspend fun removeTaskWithId(@Path("taskId") taskId: String)

    @PUT("/task/{taskId}")
    suspend fun pinTask(@Path("taskId") taskId: String, @Body task: TaskRequest): Response<TaskResponse>

    @PUT("/task/{taskId}")
    suspend fun unPinTask(@Path("taskId") taskId: String, @Body task: TaskRequest): Response<TaskResponse>

    @GET("/task")
    suspend fun getAllTasks(): AllTaskResponse
}