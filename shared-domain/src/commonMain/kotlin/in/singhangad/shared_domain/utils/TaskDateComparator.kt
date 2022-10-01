package `in`.singhangad.shared_domain.utils

import `in`.singhangad.shared_domain.entity.Task

class TaskDateComparator: Comparator<Task> {
    override fun compare(task1: Task, task2: Task): Int {
        return when {
            else -> task1.createdAt.compareTo(task2.createdAt)
        }
    }
}