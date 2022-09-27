package `in`.singhangad.shared_domain.usecase


import `in`.singhangad.shared_domain.contract.TaskRepositoryContract
import `in`.singhangad.shared_domain.entity.Task
import `in`.singhangad.shared_domain.utils.TaskDateComparator

class GetDateSortedTaskUseCase constructor(private val repository: TaskRepositoryContract) {
    suspend operator fun invoke(forceUpdate: Boolean = false): Map<Boolean, List<Task>> {
        val allTasksResult = repository.getTasks(forceUpdate)
        val taskList = allTasksResult.getOrNull()
        return taskList?.sortedWith(TaskDateComparator())
            ?.groupBy { task -> task.isPinned }
//            ?.toSortedMap(Collections.reverseOrder())
            ?: mutableMapOf()
    }
}