package com.example.domain.usecase

import com.example.domain.entity.Task

class UpsertTaskUseCase constructor(
    private val createTaskUseCase: CreateTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase
) {
    suspend operator fun invoke(params: UseCaseParams): Result<Task?> {
        val task = params.task
        return if (task.taskId == null) {
            createTaskUseCase.invoke(
                CreateTaskUseCase.UseCaseParams(
                task.taskTitle, task.taskDescription, task.endDate
            ))
        } else {
            updateTaskUseCase.invoke(
                UpdateTaskUseCase.UseCaseParams(
                task.taskId, task
            ))
        }
    }

    data class UseCaseParams(
        val task: Task
    )
}