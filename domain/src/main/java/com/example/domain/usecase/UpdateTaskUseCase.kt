package com.example.domain.usecase

import com.example.common.DefaultRepository
import com.example.common.WorkManagerScheduler
import com.example.domain.contract.SchedulerRepositoryContract
import com.example.domain.contract.TaskRepositoryContract
import com.example.domain.entity.Task
import javax.inject.Inject

class UpdateTaskUseCase @Inject constructor(
    @DefaultRepository private val taskRepositoryContract: TaskRepositoryContract,
    @WorkManagerScheduler private val schedulerRepository: SchedulerRepositoryContract
) {
    suspend operator fun invoke(params: UseCaseParams): Result<Task?> {
        val result = taskRepositoryContract.updateTask(params.id, params.task)
        if (result.isSuccess) {
            schedulerRepository.updateScheduler(params.id, params.task.endDate)
        }
        return result
    }

    data class UseCaseParams(
        val id: String,
        val task: Task
    )
}