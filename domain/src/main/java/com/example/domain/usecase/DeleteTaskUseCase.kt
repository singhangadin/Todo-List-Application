package com.example.domain.usecase

import com.example.common.DefaultRepository
import com.example.common.WorkManagerScheduler
import com.example.domain.contract.SchedulerRepositoryContract
import com.example.domain.contract.TaskRepositoryContract
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(
    @DefaultRepository private val taskRepositoryContract: TaskRepositoryContract,
    @WorkManagerScheduler private val schedulerRepository: SchedulerRepositoryContract
) {
    suspend operator fun invoke(params: UseCaseParams): Result<Unit> {
        val result = taskRepositoryContract.deleteTask(params.id)
        if (result.isSuccess) {
            schedulerRepository.deleteScheduler(params.id)
        }
        return result
    }

    data class UseCaseParams(
        val id: String
    )
}