package com.example.data.datasource.db

import com.example.data.datasource.base.TaskDataSource
import com.example.data.datasource.db.dao.TaskDao
import com.example.data.datasource.db.entity.fromDomainTask
import com.example.data.datasource.db.entity.toDomainTask
import com.example.domain.entity.Task
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DBTaskDataSource @Inject constructor(private val taskDao: TaskDao): TaskDataSource {
    override suspend fun insertTask(task: Task): Task? {
        TODO()
    }

    override suspend fun deleteTask(task: Task) = TODO()

    override suspend fun updateTask(task: Task): Task? {
        taskDao.updateTask(task.fromDomainTask())
        return taskDao.getTaskWithId(task.taskId!!.toLong())?.toDomainTask()
    }

    override suspend fun getTaskWithId(id: String): Task? = taskDao.getTaskWithId(id.toLong())?.toDomainTask()

    override suspend fun removeTaskWithId(id: String) = TODO()

    override suspend fun pinTask(id: String) = taskDao.pinTask(id.toLong())

    override suspend fun unPinTask(id: String) = TODO()

    override suspend fun getAllTasks(): List<Task> = taskDao.getAllTasks().map { it.toDomainTask() }

    override suspend fun deleteAllTasks() = TODO()
}