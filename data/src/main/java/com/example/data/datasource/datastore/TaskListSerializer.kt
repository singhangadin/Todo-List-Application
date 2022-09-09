package com.example.data.datasource.datastore

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.example.data.datasource.datastore.entity.Task
import com.example.data.datasource.datastore.entity.TaskList
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream
import java.util.*

object TaskListSerializer: Serializer<TaskList> {
    override val defaultValue: TaskList
        get() = TaskList.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): TaskList {
        try {
            return TaskList.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(
        t: TaskList,
        output: OutputStream) = t.writeTo(output)
}

val Context.taskListDataStore: DataStore<TaskList> by dataStore(
    fileName = "task_prefs.proto",
    serializer = TaskListSerializer
)

fun com.example.domain.entity.Task.fromDomainTask(): Task {
    return Task.newBuilder()
        .setTaskId(this.taskId?.toLong()?:0)
        .setTaskTitle(this.taskTitle)
        .setTaskDescription(this.taskDescription)
        .setCreatedAt(this.createdAt.time)
        .setEndDate(this.createdAt.time)
        .setIsPinned(this.isPinned)
        .build()
}

fun Task.toDomainTask(): com.example.domain.entity.Task {
    return com.example.domain.entity.Task(
        this.taskId.toString(),
        this.taskTitle,
        this.taskDescription,
        this.isPinned,
        Date(this.createdAt),
        Date(this.endDate)
    )
}