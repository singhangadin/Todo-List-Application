package `in`.singhangad.shared_data.datasource.remote.service

import `in`.singhangad.shared_data.datasource.remote.entity.AllTaskResponse
import `in`.singhangad.shared_data.datasource.remote.entity.TaskRequest
import `in`.singhangad.shared_data.datasource.remote.entity.TaskResponse


interface TodoService {
//    @POST("/task")
    suspend fun addTask(task: TaskRequest): TaskResponse

//    @DELETE("/task/{taskId}")
    suspend fun deleteTask(taskId: String)

//    @PUT("/task/{taskId}")
    suspend fun updateTask(taskId: String, task: TaskRequest): TaskResponse

//    @GET("/task/{taskId}")
    suspend fun getTaskWithId(taskId: String): TaskResponse

//    @DELETE("/task/{taskId}")
    suspend fun removeTaskWithId(taskId: String)

//    @PUT("/task/{taskId}")
    suspend fun pinTask(taskId: String, task: TaskRequest): TaskResponse

//    @PUT("/task/{taskId}")
    suspend fun unPinTask(taskId: String, task: TaskRequest): TaskResponse

//    @GET("/task")
    suspend fun getAllTasks(): AllTaskResponse
}