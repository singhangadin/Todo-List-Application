package `in`.singhangad.shared_data.datasource.remote.entity

import `in`.singhangad.shared_domain.entity.Task
import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable


@Serializable
sealed interface ApiResponse {
        @Serializable
        class Success<T>(@Serializable val data: T): ApiResponse
        @Serializable
        class Error(val message: String): ApiResponse
}

@Serializable
data class TaskData(
        val taskId: Long,
        val taskTitle: String,
        val taskDescription: String? = null,
        val isPinned: Boolean = false,
        val createdAt: Long,
        val endDate: Long
)

fun TaskData.toTask(): Task {
        return Task(
                this.taskId,
                this.taskTitle,
                this.taskDescription,
                this.isPinned,
                this.createdAt,
                this.endDate
        )
}