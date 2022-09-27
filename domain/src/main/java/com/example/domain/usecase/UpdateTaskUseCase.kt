package com.example.domain.usecase

import com.example.domain.contract.TaskRepositoryContract
import com.example.domain.entity.Task

class UpdateTaskUseCase constructor(private val taskRepositoryContract: TaskRepositoryContract) {
    suspend operator fun invoke(params: UseCaseParams): Result<Task?> {
        return taskRepositoryContract.updateTask(params.id, params.task)
    }

    data class UseCaseParams(
        val id: String,
        val task: Task
    )
}