package `in`.singhangad.shared_data.datasource.base

import `in`.singhangad.shared_domain.entity.Task


interface TaskDataSource {
    suspend fun insertTask(task: Task): Task?

    suspend fun deleteTask(task: Task)

    suspend fun updateTask(task: Task): Task?

    suspend fun getTaskWithId(id: Long): Task?

    suspend fun removeTaskWithId(id: Long)

    suspend fun pinTask(id: Long)

    suspend fun unPinTask(id: Long)

    suspend fun getAllTasks(): List<Task>

    suspend fun deleteAllTasks()
}