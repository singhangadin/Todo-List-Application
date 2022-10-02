package `in`.singhangad.shared_data.datasource.database

import `in`.singhangad.shared_data.datasource.base.TaskDataSource
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

    override suspend fun getTaskWithId(id: Long): Task? = taskDao.getTaskWithId(id)

    override suspend fun removeTaskWithId(id: Long) = taskDao.removeTaskWithId(id)

    override suspend fun pinTask(id: Long) = taskDao.pinTask(id)

    override suspend fun unPinTask(id: Long) = taskDao.unPinTask(id)

    override suspend fun getAllTasks(): List<Task> = taskDao.getAllTasks()

    override suspend fun deleteAllTasks() = taskDao.deleteAllTasks()
}