package com.example.domain.usecase

import com.example.domain.contract.TaskRepositoryContract
import javax.inject.Inject

class PinTaskUseCase @Inject constructor(private val taskRepositoryContract: TaskRepositoryContract) {
    suspend operator fun invoke(params: UseCaseParams): Result<Unit> {
        TODO()
    }

    data class UseCaseParams(
        val id: String
    )
}