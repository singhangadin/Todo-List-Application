package `in`.singhangad.ui_common

import com.example.domain.contract.TaskRepositoryContract
import com.example.domain.entity.Task
import com.example.domain.exception.DataNotFoundException

class FakeTaskRepository: TaskRepositoryContract {

    var returnError = false
    var throwError = false
    private val taskList = ArrayList<Task>()

    override suspend fun getTasks(forceUpdate: Boolean): Result<List<Task>> {
        return if (returnError) {
            Result.failure(Exception())
        } else if (throwError) {
            throw Exception()
        }
        else {
            return Result.success(taskList)
        }
    }

    override suspend fun getTaskById(taskId: String): Result<Task> {
        val task = taskList.firstOrNull() { it.taskId == taskId }
        return if (returnError) {
            Result.failure(Exception())
        } else if (throwError) {
            throw Exception()
        }
        else {
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
        } else if (throwError) {
            throw Exception()
        } else {
            val newTask = if (task.taskId == null) {
                task.copy(System.currentTimeMillis().toString())
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
        } else if (throwError) {
            throw Exception()
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
        } else if (throwError) {
            throw Exception()
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
        } else if (throwError) {
            throw Exception()
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
        } else if (throwError) {
            throw Exception()
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
        returnError = false
        throwError = false
        taskList.clear()
    }
}