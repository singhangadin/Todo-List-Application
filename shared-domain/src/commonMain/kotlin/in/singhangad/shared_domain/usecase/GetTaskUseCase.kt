package `in`.singhangad.shared_domain.usecase

import `in`.singhangad.shared_domain.contract.TaskRepositoryContract
import `in`.singhangad.shared_domain.entity.Task

class GetTaskUseCase constructor(private val repository: TaskRepositoryContract) {
    suspend operator fun invoke(taskId: Long): Result<Task> {
        return repository.getTaskById(taskId)
    }
}