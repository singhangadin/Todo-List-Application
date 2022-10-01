package `in`.singhangad.shared_domain.usecase

import `in`.singhangad.shared_domain.contract.TaskRepositoryContract
import `in`.singhangad.shared_domain.entity.Task
import kotlinx.datetime.Clock


class CreateTaskUseCase constructor(private val taskRepositoryContract: TaskRepositoryContract) {
    suspend operator fun invoke(params: UseCaseParams): Result<Task?> {
        val task = Task(
            taskTitle = params.title,
            taskDescription = params.description,
            isPinned = false,
            endDate = params.endDate,
            createdAt = Clock.System.now().toEpochMilliseconds(),
            taskId = null
        )
        return taskRepositoryContract.createNewTask(task)
    }

    data class UseCaseParams(
        val title: String, val description: String?, val endDate: Long
    )
}