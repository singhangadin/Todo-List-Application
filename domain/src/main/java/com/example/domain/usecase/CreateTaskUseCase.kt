package com.example.domain.usecase

import com.example.domain.contract.TaskRepositoryContract
import com.example.domain.entity.Task
import java.util.*
import javax.inject.Inject

class CreateTaskUseCase @Inject constructor(private val taskRepositoryContract: TaskRepositoryContract) {
    suspend operator fun invoke(params: UseCaseParams): Result<Task?> {
        val task = Task(
            taskTitle = params.title,
            taskDescription = params.description,
            isPinned = false,
            endDate = params.endDate,
            createdAt = Date(),
            taskId = null
        )
        return taskRepositoryContract.createNewTask(task)
    }

    data class UseCaseParams(
        val title: String, val description: String?, val endDate: Date
    )
}