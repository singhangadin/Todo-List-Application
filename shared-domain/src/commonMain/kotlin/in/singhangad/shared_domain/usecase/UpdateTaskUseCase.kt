package `in`.singhangad.shared_domain.usecase

import `in`.singhangad.shared_domain.contract.TaskRepositoryContract
import `in`.singhangad.shared_domain.entity.Task

class UpdateTaskUseCase constructor(private val taskRepositoryContract: TaskRepositoryContract) {
    suspend operator fun invoke(params: UseCaseParams): Result<Task?> {
        return taskRepositoryContract.updateTask(params.id, params.task)
    }

    data class UseCaseParams(
        val id: String,
        val task: Task
    )
}