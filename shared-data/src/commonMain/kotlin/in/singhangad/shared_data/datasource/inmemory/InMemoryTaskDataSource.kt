package `in`.singhangad.shared_data.datasource.inmemory

import `in`.singhangad.shared_data.base.TaskDataSource
import `in`.singhangad.shared_domain.entity.Task
import kotlinx.datetime.Clock

class InMemoryTaskDataSource : TaskDataSource {
    private var taskData = LinkedHashMap<String, Task>()

    override suspend fun insertTask(task: Task): Task? {
        return kotlin.runCatching {
            val newTask = if (task.taskId == null) {
                task.copy(Clock.System.now().toEpochMilliseconds().toString())
            } else {
                task
            }
            taskData[newTask.taskId!!] = newTask
            newTask
        }.onFailure {
            throw it
        }.getOrNull()
    }

    override suspend fun deleteTask(task: Task) {
        kotlin.runCatching {
            taskData.remove(task.taskId!!)
        }.onFailure {
            throw it
        }
    }

    override suspend fun updateTask(task: Task): Task? {
        return kotlin.runCatching {
            taskData[task.taskId!!] = task
            task
        }.onFailure {
            throw it
        }.getOrNull()
    }

    override suspend fun getTaskWithId(id: String): Task? {
        try {
            return taskData[id]
        } catch (exception: Exception) {
            throw exception
        }
    }

    override suspend fun removeTaskWithId(id: String) {
        try {
            taskData.remove(id)
        } catch (exception: Exception) {
            throw exception
        }
    }

    override suspend fun pinTask(id: String) {
        try {
            taskData[id] = taskData[id]?.copy(isPinned = true)!!
            taskData[id]
        } catch (exception: Exception) {
            throw exception
        }
    }

    override suspend fun unPinTask(id: String) {
        try {
            taskData[id] = taskData[id]?.copy(isPinned = false)!!
            taskData[id]
        } catch (exception: Exception) {
            throw exception
        }
    }

    override suspend fun getAllTasks(): List<Task> {
        try {
            return taskData.values.toList()
        } catch (exception: Exception) {
            throw exception
        }
    }

    override suspend fun deleteAllTasks() {
        taskData.clear()
    }

    fun clear() {
        taskData.clear()
    }
}