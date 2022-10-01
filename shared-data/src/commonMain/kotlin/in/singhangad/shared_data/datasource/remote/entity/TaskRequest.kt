package `in`.singhangad.shared_data.datasource.remote.entity

import `in`.singhangad.shared_domain.entity.Task
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TaskRequest (
    @SerialName("description")
    val taskDescription: String ?= null,
    val completed: Boolean
)

fun Task.toTaskRequest(): TaskRequest {
    return TaskRequest(
        this.taskDescription?:"",
        this.isPinned
    )
}