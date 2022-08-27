package com.example.domain.usecase

import com.example.domain.contract.TaskRepositoryContract
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(private val taskRepositoryContract: TaskRepositoryContract) {
    suspend operator fun invoke(params: UseCaseParams): Result<Unit> {
        return taskRepositoryContract.deleteTask(params.id)
    }

    data class UseCaseParams(
        val id: String
    )
}