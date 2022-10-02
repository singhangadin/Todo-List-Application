package `in`.singhangad.shared_data.datasource.remote.entity

import `in`.singhangad.shared_domain.entity.Task
import kotlinx.serialization.Serializable

@Serializable
class CreateTaskRequest (
    val taskTitle: String = "",
    val taskDescription: String? = null,
    val isPinned: Boolean = false,
    val endDate: Long
)

fun Task.toCreateTaskRequest(): CreateTaskRequest {
    return CreateTaskRequest(
        this.taskTitle,
        this.taskDescription,
        this.isPinned,
        this.endDate
    )
}

@Serializable
data class PinTaskRequest(
    val isPinned: Boolean
)

@Serializable
class UpdateTaskRequest (
    val taskId: Long? = null,
    val taskTitle: String = "",
    val taskDescription: String? = null,
    val isPinned: Boolean,
    val endDate: Long
)

fun Task.toUpdateTaskRequest(): UpdateTaskRequest {
    return UpdateTaskRequest(
        this.taskId,
        this.taskTitle,
        this.taskDescription,
        this.isPinned,
        this.endDate
    )
}