package `in`.singhangad.shared_data.utils

import `in`.singhangad.shared_domain.entity.Task
import `in`.singhangad.shareddata.database.Task_table

fun Task_table.toDomain(): Task {
    return Task(
        this.task_id.toString(),
        this.task_title,
        this.task_description,
        this.task_pinned,
        this.task_create_time,
        this.task_end_time
    )
}