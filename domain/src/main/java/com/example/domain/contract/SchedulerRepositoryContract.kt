package com.example.domain.contract

import java.util.*

interface SchedulerRepositoryContract {

    fun createScheduler(taskId: String, time: Date)

    fun deleteScheduler(taskId: String)

    fun updateScheduler(taskId: String, time: Date)
}