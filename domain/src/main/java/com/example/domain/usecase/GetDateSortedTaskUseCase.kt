package com.example.domain.usecase

import com.example.domain.contract.TaskRepositoryContract
import com.example.domain.entity.Task
import com.example.domain.utils.TaskDateComparator
import java.util.*
import javax.inject.Inject

class GetDateSortedTaskUseCase @Inject constructor(private val repository: TaskRepositoryContract) {
    suspend operator fun invoke(forceUpdate: Boolean = false): Map<Boolean, List<Task>> {
        val allTasksResult = repository.getTasks(forceUpdate)
        val taskList = allTasksResult.getOrNull()
        return taskList?.sortedWith(TaskDateComparator())
            ?.groupBy { task -> task.isPinned }
            ?.toSortedMap(Collections.reverseOrder()) ?: mutableMapOf()
    }
}