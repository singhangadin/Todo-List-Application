package com.example.data.datasource.db

import `in`.singhangad.shared_domain.entity.Task
import com.example.data.datasource.base.TaskDataSource
import com.example.data.datasource.db.dao.TaskDao
import com.example.data.datasource.db.entity.fromDomainTask
import com.example.data.datasource.db.entity.toDomainTask

class DBTaskDataSource constructor(private val taskDao: TaskDao): TaskDataSource {
    override suspend fun insertTask(task: Task): Task? {
        val id = taskDao.insertTask(task.fromDomainTask())
        return taskDao.getTaskWithId(id)?.toDomainTask()
    }

    override suspend fun deleteTask(task: Task) = taskDao.deleteTask(task.fromDomainTask())

    override suspend fun updateTask(task: Task): Task? {
        taskDao.updateTask(task.fromDomainTask())
        return taskDao.getTaskWithId(task.taskId!!.toLong())?.toDomainTask()
    }

    override suspend fun getTaskWithId(id: String): Task? = taskDao.getTaskWithId(id.toLong())?.toDomainTask()

    override suspend fun removeTaskWithId(id: String) = taskDao.removeTaskWithId(id.toLong())

    override suspend fun pinTask(id: String) = taskDao.pinTask(id.toLong())

    override suspend fun unPinTask(id: String) = taskDao.unPinTask(id.toLong())

    override suspend fun getAllTasks(): List<Task> = taskDao.getAllTasks().map { it.toDomainTask() }

    override suspend fun deleteAllTasks() = taskDao.deleteAllTasks()
}