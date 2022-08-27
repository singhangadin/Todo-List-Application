package com.example.data.datasource.db.entity

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Keep
@Entity(tableName = "task_table")
data class Task (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "task_id")
    val taskId: Long ?= null,

    @ColumnInfo(name = "task_title")
    val taskTitle: String = "",

    @ColumnInfo(name = "task_description")
    val taskDescription: String? = null,

    @ColumnInfo(name = "task_pinned")
    val isPinned: Boolean = false,

    @ColumnInfo(name = "task_create_time")
    val createdAt: Date,

    @ColumnInfo(name = "task_end_time")
    val endDate: Date
)

fun com.example.domain.entity.Task.fromDomainTask(): Task = Task(
    this.taskId?.toLong(),
    this.taskTitle,
    this.taskDescription,
    this.isPinned,
    this.createdAt,
    this.endDate
)

fun Task.toDomainTask(): com.example.domain.entity.Task = com.example.domain.entity.Task(
    this.taskId.toString(),
    this.taskTitle,
    this.taskDescription,
    this.isPinned,
    this.createdAt,
    this.endDate
)
