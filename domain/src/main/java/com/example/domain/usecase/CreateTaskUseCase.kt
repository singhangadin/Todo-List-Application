package com.example.domain.usecase

import com.example.common.DefaultRepository
import com.example.common.WorkManagerScheduler
import com.example.domain.contract.SchedulerRepositoryContract
import com.example.domain.contract.TaskRepositoryContract
import com.example.domain.entity.Task
import java.util.*
import javax.inject.Inject

class CreateTaskUseCase @Inject constructor(
    @DefaultRepository private val taskRepositoryContract: TaskRepositoryContract,
    @WorkManagerScheduler private val schedulerRepository: SchedulerRepositoryContract
) {
    suspend operator fun invoke(params: UseCaseParams): Result<Task?> {
        val task = Task(
            taskTitle = params.title,
            taskDescription = params.description,
            isPinned = false,
            endDate = params.endDate,
            createdAt = Date(),
            taskId = null
        )
        val result = taskRepositoryContract.createNewTask(task)
        if (result.isSuccess) {
            val newTask = result.getOrNull()!!
            schedulerRepository.createScheduler(newTask.taskId!!, task.endDate)
        }
        return result
    }

    data class UseCaseParams(
        val title: String, val description: String?, val endDate: Date
    )
}