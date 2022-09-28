package `in`.singhangad.shared_data.datasource.remote

import `in`.singhangad.shared_data.datasource.base.TaskDataSource
import `in`.singhangad.shared_data.datasource.remote.entity.TaskRequest
import `in`.singhangad.shared_data.datasource.remote.entity.toTask
import `in`.singhangad.shared_data.datasource.remote.entity.toTaskRequest
import `in`.singhangad.shared_data.datasource.remote.service.TodoService
import `in`.singhangad.shared_domain.entity.Task


class RemoteTaskDataSource constructor(private val todoService: TodoService):
    TaskDataSource {
    override suspend fun insertTask(task: Task): Task? {
        return kotlin.runCatching {
            todoService.addTask(task.toTaskRequest())
        }.onFailure {
            throw it
        }.getOrNull()?.toTask()
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
        }.getOrNull()?.toTask()
    }

    override suspend fun getTaskWithId(id: String): Task {
        try {
            return todoService.getTaskWithId(id).toTask()
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