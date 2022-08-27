package com.example.data.remote.network.source.entity

import com.example.data.common.Utils
import com.example.domain.entity.Task
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
                Utils.getConvertedDate(this.data.createdAt)?:Date(),
                Date()
        )
}

fun TaskData.toTask(): Task {
        return Task(
                this.taskId,
                "No Title",
                this.taskDescription,
                this.completed,
                Utils.getConvertedDate(this.createdAt)?:Date(),
                Date()
        )
}