package `in`.singhangad.shared_data.database

import `in`.singhangad.shared_domain.entity.Task

interface TaskDao {
    suspend fun insertTask(task: Task): Long

    suspend fun deleteTask(task: Task)

    suspend fun updateTask(task: Task)

    suspend fun getTaskWithId(id: Long): Task?

    suspend fun removeTaskWithId(id: Long)

    suspend fun pinTask(id: Long)

    suspend fun unPinTask(id: Long)

    suspend fun taskExist(taskId: Long): Boolean = getTaskWithId(taskId) != null

    suspend fun getAllTasks(): List<Task>

    suspend fun deleteAllTasks()
}