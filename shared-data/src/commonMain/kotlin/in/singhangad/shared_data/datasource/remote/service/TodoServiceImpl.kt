package `in`.singhangad.shared_data.datasource.remote.service

import `in`.singhangad.shared_data.datasource.remote.entity.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class TodoServiceImpl(private val client: HttpClient, private val json: Json): TodoService {
    override suspend fun addTask(task: CreateTaskRequest): ApiResponse {
        val response = client.post("https://api.singhangad.in/todo/task") {
            contentType(ContentType.Application.Json)
            setBody(task)
        }
        return if (response.status == HttpStatusCode.OK) {
            json.decodeFromString<ApiResponse.Success<TaskData>>(response.body())
        } else {
            json.decodeFromString<ApiResponse.Error>(response.body())
        }
    }

    override suspend fun deleteTask(taskId: Long): ApiResponse {
        val response = client.delete("https://api.singhangad.in/todo/task/$taskId")
        return if (response.status == HttpStatusCode.OK) {
            json.decodeFromString<ApiResponse.Success<String>>(response.body())
        } else {
            json.decodeFromString<ApiResponse.Error>(response.body())
        }
    }

    override suspend fun updateTask(taskId: Long, task: UpdateTaskRequest): ApiResponse {
        val response = client.put("https://api.singhangad.in/todo/task/$taskId") {
            contentType(ContentType.Application.Json)
            setBody(task)
        }
        return if (response.status == HttpStatusCode.OK) {
            json.decodeFromString<ApiResponse.Success<TaskData>>(response.body())
        } else {
            json.decodeFromString<ApiResponse.Error>(response.body())
        }
    }

    override suspend fun getTaskWithId(taskId: Long): ApiResponse {
        val response = client.get("https://api.singhangad.in/todo/task/$taskId") {
            contentType(ContentType.Application.Json)
        }
        return if (response.status == HttpStatusCode.OK) {
            json.decodeFromString<ApiResponse.Success<TaskData>>(response.body())
        } else {
            json.decodeFromString<ApiResponse.Error>(response.body())
        }
    }

    override suspend fun removeTaskWithId(taskId: Long): ApiResponse {
        val response = client.delete("https://api.singhangad.in/todo/task/$taskId")
        return if (response.status == HttpStatusCode.OK) {
            json.decodeFromString<ApiResponse.Success<String>>(response.body())
        } else {
            json.decodeFromString<ApiResponse.Error>(response.body())
        }
    }

    override suspend fun pinTask(taskId: Long, isPinned: Boolean): ApiResponse {
        val response = client.put("https://api.singhangad.in/todo/task/pin/$taskId") {
            contentType(ContentType.Application.Json)
            setBody(PinTaskRequest(isPinned))
        }
        return if (response.status == HttpStatusCode.OK) {
            json.decodeFromString<ApiResponse.Success<TaskData>>(response.body())
        } else {
            json.decodeFromString<ApiResponse.Error>(response.body())
        }
    }

    override suspend fun unPinTask(taskId: Long, isPinned: Boolean): ApiResponse {
        val response = client.put("https://api.singhangad.in/todo/task/pin/$taskId") {
            contentType(ContentType.Application.Json)
            setBody(PinTaskRequest(isPinned))
        }
        return if (response.status == HttpStatusCode.OK) {
            json.decodeFromString<ApiResponse.Success<TaskData>>(response.body())
        } else {
            json.decodeFromString<ApiResponse.Error>(response.body())
        }
    }

    override suspend fun getAllTasks(): ApiResponse {
        val response = client.get("https://api.singhangad.in/todo/task/all") {
            contentType(ContentType.Application.Json)
        }
        return if (response.status == HttpStatusCode.OK) {
            json.decodeFromString<ApiResponse.Success<List<TaskData>>>(response.body())
        } else {
            json.decodeFromString<ApiResponse.Error>(response.body())
        }
    }
}