package com.example.domain.usecase

import com.example.common.DefaultRepository
import com.example.domain.contract.TaskRepositoryContract
import com.example.domain.entity.Task
import java.util.*
import javax.inject.Inject

class CreateTaskUseCase @Inject constructor(@DefaultRepository private val taskRepositoryContract: TaskRepositoryContract) {
    suspend operator fun invoke(params: UseCaseParams): Result<Task?> {
        TODO()
    }

    data class UseCaseParams(
        val title: String, val description: String?, val endDate: Date
    )
}