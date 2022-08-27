package com.example.domain.utils

import com.example.domain.entity.Task

class TaskDateComparator: Comparator<Task> {
    override fun compare(task1: Task?, task2: Task?): Int {
        return when {
            task1 == null && task2 == null -> 0
            task1 == null -> -1
            task2 == null -> 1
            else -> task1.createdAt.time.compareTo(task2.createdAt.time)
        }
    }
}