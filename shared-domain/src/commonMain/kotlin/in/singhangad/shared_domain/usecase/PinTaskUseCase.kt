package `in`.singhangad.shared_domain.usecase

import `in`.singhangad.shared_domain.contract.TaskRepositoryContract

class PinTaskUseCase constructor(private val taskRepositoryContract: TaskRepositoryContract) {
    suspend operator fun invoke(params: UseCaseParams): Result<Unit> {
        return taskRepositoryContract.pinTask(params.id)
    }

    data class UseCaseParams(
        val id: Long
    )
}