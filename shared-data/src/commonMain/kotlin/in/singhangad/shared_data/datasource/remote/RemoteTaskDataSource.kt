package `in`.singhangad.shared_data.datasource.remote

import `in`.singhangad.shared_data.datasource.base.TaskDataSource
import `in`.singhangad.shared_data.datasource.remote.entity.*
import `in`.singhangad.shared_data.datasource.remote.service.TodoService
import `in`.singhangad.shared_domain.entity.Task
import `in`.singhangad.shared_domain.exception.DataNotFoundException
import org.koin.core.error.InstanceCreationException


class RemoteTaskDataSource constructor(private val todoService: TodoService):
    TaskDataSource {
    override suspend fun insertTask(task: Task): Task? {
        return kotlin.runCatching {
            val response = todoService.addTask(task.toCreateTaskRequest())
            if (response is ApiResponse.Success<*>) {
                if (response.data is TaskData) {
                    return response.data.toTask()
                } else {
                    throw IllegalStateException()
                }
            } else if (response is ApiResponse.Error){
                throw InstanceCreationException(response.message, Exception())
            } else {
                throw IllegalStateException()
            }
        }.onFailure {
            throw it
        }.getOrNull()
    }

    override suspend fun deleteTask(task: Task) {
        kotlin.runCatching {
            val response = todoService.deleteTask(task.taskId!!)
            if (response is ApiResponse.Success<*>) {
                return
            } else {
                throw DataNotFoundException()
            }
        }.onFailure {
            throw it
        }
    }

    override suspend fun updateTask(task: Task): Task? {
        return kotlin.runCatching {
            val response = todoService.updateTask(task.taskId!!, task.toUpdateTaskRequest())
            if (response is ApiResponse.Success<*>) {
                if (response.data is TaskData) {
                    return response.data.toTask()
                } else {
                    throw IllegalStateException()
                }
            } else if (response is ApiResponse.Error){
                throw InstanceCreationException(response.message, Exception())
            } else {
                throw IllegalStateException()
            }
        }.onFailure {
            throw it
        }.getOrNull()
    }

    override suspend fun getTaskWithId(id: Long): Task {
        try {
            val response = todoService.getTaskWithId(id)
            if (response is ApiResponse.Success<*>) {
                if (response.data is TaskData) {
                    return response.data.toTask()
                } else {
                    throw IllegalStateException()
                }
            } else if (response is ApiResponse.Error){
                throw InstanceCreationException(response.message, Exception())
            } else {
                throw IllegalStateException()
            }
        } catch (exception: Exception) {
            throw exception
        }
    }

    override suspend fun removeTaskWithId(id: Long) {
        kotlin.runCatching {
            val response = todoService.removeTaskWithId(id)
            if (response is ApiResponse.Success<*>) {
                return
            } else {
                throw DataNotFoundException()
            }
        }.onFailure {
            throw it
        }
    }

    override suspend fun pinTask(id: Long) {
        try {
            val response = todoService.pinTask(id, true)
            if (response is ApiResponse.Success<*>) {
                if (response.data is TaskData) {
                    return
                } else {
                    throw IllegalStateException()
                }
            } else if (response is ApiResponse.Error){
                throw InstanceCreationException(response.message, Exception())
            } else {
                throw IllegalStateException()
            }
        } catch (exception: Exception) {
            throw exception
        }
    }

    override suspend fun unPinTask(id: Long) {
        try {
            val response = todoService.pinTask(id, false)
            if (response is ApiResponse.Success<*>) {
                if (response.data is TaskData) {
                    return
                } else {
                    throw IllegalStateException()
                }
            } else if (response is ApiResponse.Error){
                throw InstanceCreationException(response.message, Exception())
            } else {
                throw IllegalStateException()
            }
        } catch (exception: Exception) {
            throw exception
        }
    }

    override suspend fun getAllTasks(): List<Task> {
        return try {
            val response = todoService.getAllTasks()
            if (response is ApiResponse.Success<*>) {
                if (response.data is List<*> && response.data.any { it is TaskData }) {
                    (response.data as List<TaskData>).map { it.toTask() }
                } else {
                    throw IllegalStateException()
                }
            } else {
                emptyList()
            }
        } catch (exception: Exception) {
            throw exception
        }
    }

    override suspend fun deleteAllTasks() {

    }
}