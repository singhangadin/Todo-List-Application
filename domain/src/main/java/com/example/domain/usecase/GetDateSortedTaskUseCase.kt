package com.example.domain.usecase

import com.example.common.DefaultRepository
import com.example.domain.contract.TaskRepositoryContract
import com.example.domain.entity.Task
import com.example.domain.utils.TaskDateComparator
import java.util.*
import javax.inject.Inject

class GetDateSortedTaskUseCase @Inject constructor(@DefaultRepository private val repository: TaskRepositoryContract) {
    suspend operator fun invoke(forceUpdate: Boolean = false): Map<Boolean, List<Task>> {
        val result = repository.getTasks(forceUpdate)
        if (result.isSuccess) {
            val data = result.getOrNull()
            return data?.sortedWith(TaskDateComparator())
                ?.groupBy { it.isPinned }
                ?.toSortedMap(Collections.reverseOrder())?: mutableMapOf()
        } else {
            return emptyMap()
        }
    }
}