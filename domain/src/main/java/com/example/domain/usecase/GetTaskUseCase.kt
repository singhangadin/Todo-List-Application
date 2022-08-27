package com.example.domain.usecase

import com.example.domain.contract.TaskRepositoryContract
import com.example.domain.entity.Task
import javax.inject.Inject

class GetTaskUseCase @Inject constructor(private val repository: TaskRepositoryContract) {
    suspend operator fun invoke(taskId: String): Result<Task> {
        TODO()
    }
}