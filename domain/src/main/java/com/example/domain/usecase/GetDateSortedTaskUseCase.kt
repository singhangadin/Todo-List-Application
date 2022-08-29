package com.example.domain.usecase

import com.example.common.DefaultRepository
import com.example.domain.contract.TaskRepositoryContract
import com.example.domain.entity.Task
import com.example.domain.utils.TaskDateComparator
import java.util.*
import javax.inject.Inject

class GetDateSortedTaskUseCase @Inject constructor(@DefaultRepository private val repository: TaskRepositoryContract) {
    suspend operator fun invoke(forceUpdate: Boolean = false): Map<Boolean, List<Task>> {
        TODO()
    }
}