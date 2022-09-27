package com.example.domain.usecase

import com.example.domain.contract.TaskRepositoryContract

class PinTaskUseCase constructor(private val taskRepositoryContract: TaskRepositoryContract) {
    suspend operator fun invoke(params: UseCaseParams): Result<Unit> {
        return taskRepositoryContract.pinTask(params.id)
    }

    data class UseCaseParams(
        val id: String
    )
}