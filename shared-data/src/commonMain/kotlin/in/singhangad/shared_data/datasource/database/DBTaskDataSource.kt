package `in`.singhangad.shared_data.datasource.database

import `in`.singhangad.shared_data.base.TaskDataSource
import `in`.singhangad.shared_data.datasource.database.dao.TaskDao
import `in`.singhangad.shared_domain.entity.Task

class DBTaskDataSource constructor(private val taskDao: TaskDao): TaskDataSource {
    override suspend fun insertTask(task: Task): Task? {
        val id = taskDao.insertTask(task)
        return taskDao.getTaskWithId(id)
    }

    override suspend fun deleteTask(task: Task) = taskDao.deleteTask(task)

    override suspend fun updateTask(task: Task): Task? {
        taskDao.updateTask(task)
        return taskDao.getTaskWithId(task.taskId!!.toLong())
    }

    override suspend fun getTaskWithId(id: String): Task? = taskDao.getTaskWithId(id.toLong())

    override suspend fun removeTaskWithId(id: String) = taskDao.removeTaskWithId(id.toLong())

    override suspend fun pinTask(id: String) = taskDao.pinTask(id.toLong())

    override suspend fun unPinTask(id: String) = taskDao.unPinTask(id.toLong())

    override suspend fun getAllTasks(): List<Task> = taskDao.getAllTasks()

    override suspend fun deleteAllTasks() = taskDao.deleteAllTasks()
}