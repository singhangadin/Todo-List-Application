package `in`.singhangad.shared_domain.entity


data class Task (
    val taskId: Long ?= null,
    val taskTitle: String = "",
    val taskDescription: String? = null,
    val isPinned: Boolean = false,
    val createdAt: Long,
    val endDate: Long
) {
    override fun equals(other: Any?): Boolean {
        return if (other !is Task) {
            false
        } else {
            other.taskId == this.taskId &&
            other.taskTitle == this.taskTitle &&
            other.taskDescription == this.taskDescription &&
            other.isPinned == this.isPinned &&
            other.createdAt == this.createdAt &&
            other.endDate == this.endDate
        }
    }

    override fun hashCode(): Int {
        return taskId.hashCode()
    }
}