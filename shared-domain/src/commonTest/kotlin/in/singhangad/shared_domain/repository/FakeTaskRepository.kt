package `in`.singhangad.shared_domain.repository

import `in`.singhangad.shared_domain.contract.TaskRepositoryContract
import `in`.singhangad.shared_domain.entity.Task
import `in`.singhangad.shared_domain.exception.DataNotFoundException
import kotlinx.datetime.Clock

class FakeTaskRepository: TaskRepositoryContract {

    var returnError = false
    private val taskList = ArrayList<Task>()

    override suspend fun getTasks(forceUpdate: Boolean): Result<List<Task>> {
        return Result.success(taskList)
    }

    override suspend fun getTaskById(taskId: String): Result<Task> {
        val task = taskList.firstOrNull() { it.taskId == taskId }
        return if (returnError) {
            Result.failure(Exception())
        } else {
            if (task == null) {
                Result.failure(DataNotFoundException())
            } else {
                Result.success(task)
            }
        }
    }

    override suspend fun createNewTask(task: Task): Result<Task> {
        return if (returnError) {
            Result.failure(Exception())
        } else {
            val newTask = if (task.taskId == null) {
                task.copy(Clock.System.now().toEpochMilliseconds().toString())
            } else {
                task
            }
            taskList.add(newTask)
            Result.success(newTask)
        }
    }

    override suspend fun deleteTask(taskId: String): Result<Unit> {
        return if (returnError) {
            Result.failure(Exception())
        } else {
            val index = taskList.indexOfFirst { it.taskId == taskId }
            if (index != -1) {
                taskList.removeAt(index)
                Result.success(Unit)
            } else {
                Result.failure(DataNotFoundException())
            }
        }
    }

    override suspend fun updateTask(taskId: String, task: Task): Result<Task?> {
        return if (returnError) {
            Result.failure(Exception())
        } else {
            val index = taskList.indexOfFirst { it.taskId == taskId }
            if (index != -1 && index < taskList.size) {
                taskList[index] = task
                Result.success(task)
            } else {
                Result.failure(DataNotFoundException())
            }
        }
    }

    override suspend fun pinTask(taskId: String): Result<Unit> {
        return if (returnError) {
            Result.failure(Exception())
        } else {
            val index = taskList.indexOfFirst { it.taskId == taskId }
            if (index != -1) {
                val newTask = Task(
                    taskList[index].taskId,
                    taskList[index].taskTitle,
                    taskList[index].taskDescription,
                    true,
                    taskList[index].createdAt,
                    taskList[index].endDate
                )
                taskList[index] = newTask
                Result.success(Unit)
            } else {
                Result.failure(DataNotFoundException())
            }
        }
    }

    override suspend fun unPinTask(taskId: String): Result<Unit> {
        return if (returnError) {
            Result.failure(Exception())
        } else {
            val index = taskList.indexOfFirst { it.taskId == taskId }
            if (index != -1) {
                val newTask = Task(
                    taskList[index].taskId,
                    taskList[index].taskTitle,
                    taskList[index].taskDescription,
                    false,
                    taskList[index].createdAt,
                    taskList[index].endDate
                )
                taskList[index] = newTask
                Result.success(Unit)
            } else {
                Result.failure(DataNotFoundException())
            }
        }
    }

    fun clear() {
        taskList.clear()
    }
}