package com.example.domain.entity

import java.util.*

data class Task (
    val taskId: String ?= null,
    val taskTitle: String = "",
    val taskDescription: String? = null,
    val isPinned: Boolean = false,
    val createdAt: Date,
    val endDate: Date
) {
    override fun equals(other: Any?): Boolean {
        return if (other !is Task) {
            false
        } else {
            other.taskId == this.taskId &&
            other.taskTitle == this.taskTitle &&
            other.taskDescription == this.taskDescription &&
            other.isPinned == this.isPinned &&
            other.createdAt.time == this.createdAt.time &&
            other.endDate.time == this.endDate.time
        }
    }

    override fun hashCode(): Int {
        return taskId.hashCode()
    }
}