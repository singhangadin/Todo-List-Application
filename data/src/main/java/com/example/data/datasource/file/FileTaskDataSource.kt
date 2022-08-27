package com.example.data.datasource.file

import com.example.data.datasource.base.TaskDataSource
import com.example.domain.entity.Task
import android.text.TextUtils
import com.google.gson.Gson
import kotlinx.coroutines.delay
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class FileTaskDataSource @Inject constructor(private val gson: Gson, private val filePath: String): TaskDataSource {
    private val SERVICE_LATENCY_IN_MILLIS = 2000L

    override suspend fun insertTask(task: Task): Task? {
        val file = File(filePath)
        if (!file.exists()) {
            file.createNewFile()
        }
        val reader = FileReader(file)
        val content = reader.readText()
        var taskWrapper = gson.fromJson(content, TaskFileWrapper::class.java)

        if (taskWrapper == null) {
            taskWrapper = TaskFileWrapper(mutableListOf())
        }

        val newTask = if (task.taskId == null) {
            val newTask = task.copy(taskId = Random.nextInt(Int.MAX_VALUE).toString())
            taskWrapper.list.add(newTask)
            newTask
        } else {
            taskWrapper.list.add(task)
            task
        }

        val fileWriter = FileWriter(file)
        fileWriter.write(gson.toJson(taskWrapper))
        fileWriter.close()

        delay(SERVICE_LATENCY_IN_MILLIS)
        return newTask
    }

    override suspend fun deleteTask(task: Task) {
        val file = File(filePath)
        val reader = FileReader(file)
        val content = reader.readText()
        val taskWrapper = gson.fromJson(content, TaskFileWrapper::class.java)

        if (task.taskId != null) {
            val item = taskWrapper.list.find { it.taskId == task.taskId }
            taskWrapper.list.remove(item)
        }

        val fileWriter = FileWriter(file)
        fileWriter.write(gson.toJson(taskWrapper))
        fileWriter.close()

        delay(SERVICE_LATENCY_IN_MILLIS)
    }

    override suspend fun updateTask(task: Task): Task? {
        val file = File(filePath)
        val reader = FileReader(file)
        val content = reader.readText()
        val taskWrapper = gson.fromJson(content, TaskFileWrapper::class.java)

        if (task.taskId != null) {
            val item = taskWrapper.list.find { it.taskId == task.taskId }
            taskWrapper.list[taskWrapper.list.indexOf(item)] = task
        }

        val fileWriter = FileWriter(file)
        fileWriter.write(gson.toJson(taskWrapper))
        fileWriter.close()

        delay(SERVICE_LATENCY_IN_MILLIS)
        return task
    }

    override suspend fun getTaskWithId(id: String): Task? {
        val file = File(filePath)
        return if (file.exists()) {
            val reader = FileReader(file)
            val content = reader.readText()
            val taskWrapper = gson.fromJson(content, TaskFileWrapper::class.java)

            delay(SERVICE_LATENCY_IN_MILLIS)
            taskWrapper.list.find { TextUtils.equals(it.taskId, id) }
        } else {
            delay(SERVICE_LATENCY_IN_MILLIS)
            null
        }
    }

    override suspend fun removeTaskWithId(id: String) {
        val file = File(filePath)
        val reader = FileReader(file)
        val content = reader.readText()
        val taskWrapper = gson.fromJson(content, TaskFileWrapper::class.java)

        val item = taskWrapper.list.find { TextUtils.equals(it.taskId, id) }
        taskWrapper.list.remove(item)

        val fileWriter = FileWriter(file)
        fileWriter.write(gson.toJson(taskWrapper))
        fileWriter.close()
        delay(SERVICE_LATENCY_IN_MILLIS)
    }

    override suspend fun pinTask(id: String) {
        val file = File(filePath)
        val reader = FileReader(file)
        val content = reader.readText()
        val taskWrapper = gson.fromJson(content, TaskFileWrapper::class.java)

        val item = taskWrapper.list.find { TextUtils.equals(it.taskId, id) }
        val newTask = item?.copy(isPinned = true)
        taskWrapper.list[taskWrapper.list.indexOf(item)] = newTask!!

        val fileWriter = FileWriter(file)
        fileWriter.write(gson.toJson(taskWrapper))
        fileWriter.close()
        delay(SERVICE_LATENCY_IN_MILLIS)
    }

    override suspend fun unPinTask(id: String) {
        val file = File(filePath)
        val reader = FileReader(file)
        val content = reader.readText()
        val taskWrapper = gson.fromJson(content, TaskFileWrapper::class.java)

        val item = taskWrapper.list.find { TextUtils.equals(it.taskId, id) }
        val newTask = item?.copy(isPinned = false)
        taskWrapper.list[taskWrapper.list.indexOf(item)] = newTask!!

        val fileWriter = FileWriter(file)
        fileWriter.write(gson.toJson(taskWrapper))
        fileWriter.close()
        delay(SERVICE_LATENCY_IN_MILLIS)
    }

    override suspend fun getAllTasks(): List<Task> {
        val file = File(filePath)
        if (file.exists()) {
            val reader = FileReader(file)
            val content = reader.readText()
            val taskWrapper = gson.fromJson(content, TaskFileWrapper::class.java)
            delay(SERVICE_LATENCY_IN_MILLIS)
            return taskWrapper.list
        } else {
            delay(SERVICE_LATENCY_IN_MILLIS)
            return mutableListOf()
        }
    }

    override suspend fun deleteAllTasks() {
        val file = File(filePath)
        if (!file.exists()) {
            file.createNewFile()
            delay(SERVICE_LATENCY_IN_MILLIS)
            return
        }
        delay(SERVICE_LATENCY_IN_MILLIS)
        val fileWriter = FileWriter(file)
        fileWriter.write(gson.toJson(TaskFileWrapper(mutableListOf())))
        fileWriter.close()
    }
}

data class TaskFileWrapper(
    val list: MutableList<Task>
)