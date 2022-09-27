package com.example.data.datasource.remote.entity

import `in`.singhangad.shared_domain.entity.Task
import com.example.data.Utils
import com.google.gson.annotations.SerializedName
import java.util.*

data class TaskResponse (
        val success: Boolean,
        val data: TaskData
)

data class TaskData(
        val completed: Boolean,
        @SerializedName("_id")
        val taskId: String,
        @SerializedName("description")
        val taskDescription: String,
        val createdAt: String
)

data class AllTaskResponse (
        val count: Int,
        val data: List<TaskData>
)

fun TaskResponse.toTask(): Task {
        return Task(
                this.data.taskId,
                "No Title",
                this.data.taskDescription,
                this.data.completed,
                Utils.getConvertedDate(this.data.createdAt)?.time?:Date().time,
                Date().time
        )
}

fun TaskData.toTask(): Task {
        return Task(
                this.taskId,
                "No Title",
                this.taskDescription,
                this.completed,
                Utils.getConvertedDate(this.createdAt)?.time?:Date().time,
                Date().time
        )
}