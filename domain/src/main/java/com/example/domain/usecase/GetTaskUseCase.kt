package com.example.domain.usecase

import com.example.domain.contract.TaskRepositoryContract
import com.example.domain.entity.Task

class GetTaskUseCase constructor(private val repository: TaskRepositoryContract) {
    suspend operator fun invoke(taskId: String): Result<Task> {
        return repository.getTaskById(taskId)
    }
}