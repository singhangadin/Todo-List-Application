package `in`.singhangad.shared_data.utils

import `in`.singhangad.shared_domain.entity.Task
import `in`.singhangad.shareddata.database.Task_table

fun Task_table.toDomainTask(): Task {
    return Task(
        this.task_id.toString(),
        this.task_title,
        this.task_description,
        this.task_pinned,
        this.task_create_time,
        this.task_end_time
    )
}

fun Task.fromDomainTask(): Task_table {
    return Task_table(
        this.taskId!!.toLong(),
        this.taskTitle,
        this.taskDescription,
        this.isPinned,
        this.createdAt,
        this.endDate
    )
}