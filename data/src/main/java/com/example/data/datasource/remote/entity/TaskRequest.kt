package com.example.data.datasource.remote.entity

import `in`.singhangad.shared_domain.entity.Task
import com.google.gson.annotations.SerializedName

data class TaskRequest (
    @SerializedName("description")
    val taskDescription: String ?= null,
    val completed: Boolean
)

fun Task.toTaskRequest(): TaskRequest {
    return TaskRequest(
        this.taskDescription?:"",
        this.isPinned
    )
}