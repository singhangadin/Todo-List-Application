package `in`.singhangad.shared_domain.contract

import `in`.singhangad.shared_domain.entity.Task

interface TaskRepositoryContract {

    suspend fun getTasks(forceUpdate: Boolean): Result<List<Task>>

    suspend fun getTaskById(taskId: Long): Result<Task>

    suspend fun createNewTask(task: Task): Result<Task?>

    suspend fun deleteTask(taskId: Long): Result<Unit>

    suspend fun updateTask(taskId: Long, task: Task): Result<Task?>

    suspend fun pinTask(taskId: Long): Result<Unit>

    suspend fun unPinTask(taskId: Long): Result<Unit>
}