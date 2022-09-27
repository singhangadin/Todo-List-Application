package com.example.data.datasource.datastore

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.example.data.datasource.base.TaskDataSource
import com.example.data.datasource.datastore.entity.TaskList
import com.example.data.datasource.datastore.entity.Task as DSTask

import com.example.domain.entity.Task
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class TaskDataStoreSource constructor(val context: Context): TaskDataSource {
    override suspend fun insertTask(task: Task): Task {
        val newTask = if (task.taskId == null) {
            task.copy(System.currentTimeMillis().toString())
        } else {
            task
        }
        val tasks = context.taskListDataStore.data.first()
        val newMap = mutableMapOf<String, DSTask>().apply {
            putAll(tasks.tasksMap)
        }.also {
            it[newTask.taskId!!] = newTask.fromDomainTask()
        }

        val newTasks = TaskList.newBuilder()
            .putAllTasks(
                newMap
            ).build()

        context.taskListDataStore.updateData { newTasks }
        return newTask
    }

    override suspend fun deleteTask(task: Task) {
        val tasks = context.taskListDataStore.data.first()
        val newMap = mutableMapOf<String, DSTask>().apply {
            putAll(tasks.tasksMap)
        }.also {
            it.remove(task.taskId)
        }

        val newTasks = TaskList.newBuilder()
            .putAllTasks(
                newMap
            ).build()

        context.taskListDataStore.updateData { newTasks }
    }

    override suspend fun updateTask(task: Task): Task {
        val tasks = context.taskListDataStore.data.first()
        val newMap = mutableMapOf<String, DSTask>().apply {
            putAll(tasks.tasksMap)
        }.also {
            it[task.taskId!!] = task.fromDomainTask()
        }

        val newTasks = TaskList.newBuilder()
            .putAllTasks(
                newMap
            ).build()

        context.taskListDataStore.updateData { newTasks }
        return task
    }

    override suspend fun getTaskWithId(id: String): Task? {
        val tasks = context.taskListDataStore.data.first()
        return tasks.tasksMap[id]?.toDomainTask()
    }

    override suspend fun removeTaskWithId(id: String) {
        val tasks = context.taskListDataStore.data.first()
        tasks.tasksMap.remove(id)
        context.taskListDataStore.updateData { tasks }
    }

    override suspend fun pinTask(id: String) {
        val tasks = context.taskListDataStore.data.first()
        val oldTask = tasks.tasksMap[id]
        val newTask = oldTask?.toDomainTask()?.copy(isPinned = true)

        val newMap = mutableMapOf<String, DSTask>().apply {
            putAll(tasks.tasksMap)
        }.also {
            it[newTask?.taskId!!] = newTask.fromDomainTask()
        }

        val newTasks = TaskList.newBuilder()
            .putAllTasks(
                newMap
            ).build()

        context.taskListDataStore.updateData { newTasks }
    }

    override suspend fun unPinTask(id: String) {
        val tasks = context.taskListDataStore.data.first()
        val oldTask = tasks.tasksMap[id]
        val newTask = oldTask?.toDomainTask()?.copy(isPinned = false)

        val newMap = mutableMapOf<String, DSTask>().apply {
            putAll(tasks.tasksMap)
        }.also {
            it[newTask?.taskId!!] = newTask.fromDomainTask()
        }

        val newTasks = TaskList.newBuilder()
            .putAllTasks(
                newMap
            ).build()

        context.taskListDataStore.updateData { newTasks }
    }

    override suspend fun getAllTasks(): List<Task> {
        val tasks = context.taskListDataStore.data.first()
        return tasks.tasksMap.values.map { it.toDomainTask() }
    }

    override suspend fun deleteAllTasks() {
        context.taskListDataStore.updateData {
            TaskList.getDefaultInstance()
        }
    }

    @VisibleForTesting
    fun clear() = runBlocking {
        deleteAllTasks()
    }
}