package `in`.singhangad.shared_domain.contract

import `in`.singhangad.shared_domain.entity.Task

interface TaskRepositoryContract {

    suspend fun getTasks(forceUpdate: Boolean): Result<List<Task>>

    suspend fun getTaskById(taskId: String): Result<Task>

    suspend fun createNewTask(task: Task): Result<Task?>

    suspend fun deleteTask(taskId: String): Result<Unit>

    suspend fun updateTask(taskId: String, task: Task): Result<Task?>

    suspend fun pinTask(taskId: String): Result<Unit>

    suspend fun unPinTask(taskId: String): Result<Unit>
}