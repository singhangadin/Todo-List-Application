package `in`.singhangad.shared_data.datasource.remote.entity

import `in`.singhangad.shared_domain.entity.Task
import kotlinx.datetime.Clock
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TaskResponse (
        val success: Boolean,
        val data: TaskData
)

@Serializable
data class TaskData(
        val completed: Boolean,
        @SerialName("_id")
        val taskId: String,
        @SerialName("description")
        val taskDescription: String,
        val createdAt: String
)

@Serializable
data class AllTaskResponse (
        val count: Int,
        val data: List<TaskData>
)

fun TaskResponse.toTask(): Task {
        return Task(
                this.data.taskId,
                "No Title",
                this.data.taskDescription,
                this.data.completed,
                this.data.createdAt.toLong(),
                Clock.System.now().toEpochMilliseconds()
        )
}

fun TaskData.toTask(): Task {
        return Task(
                this.taskId,
                "No Title",
                this.taskDescription,
                this.completed,
                this.createdAt.toLong(),
                Clock.System.now().toEpochMilliseconds()
        )
}