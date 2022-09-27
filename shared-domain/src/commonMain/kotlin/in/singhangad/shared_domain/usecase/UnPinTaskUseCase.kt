package `in`.singhangad.shared_domain.usecase

import `in`.singhangad.shared_domain.contract.TaskRepositoryContract

class UnPinTaskUseCase constructor(private val taskRepositoryContract: TaskRepositoryContract) {
    suspend operator fun invoke(params: UseCaseParams): Result<Unit> {
        return taskRepositoryContract.unPinTask(params.id)
    }

    data class UseCaseParams(
        val id: String
    )
}