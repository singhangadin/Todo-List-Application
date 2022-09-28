package `in`.singhangad.shared_data.database.dao

import `in`.singhangad.shared_data.utils.toDomainTask
import `in`.singhangad.shared_domain.entity.Task
import `in`.singhangad.shareddata.database.TodoDatabaseQueries

class TaskDaoImpl(private val todoQueries: TodoDatabaseQueries): TaskDao {

    override suspend fun insertTask(task: Task): Long {
        todoQueries.insertTask(
            task.taskTitle,
            task.taskDescription,
            task.isPinned,
            task.createdAt,
            task.endDate
        )
        return todoQueries.selectLastInsertedRowId().executeAsOne()
    }

    override suspend fun deleteTask(task: Task) {
        todoQueries.removeTaskWithId(task.taskId!!.toLong())
    }

    override suspend fun updateTask(task: Task) {
        todoQueries.upsertTask(
            task.taskId?.toLong(),
            task.taskTitle,
            task.taskDescription,
            task.isPinned,
            task.createdAt,
            task.endDate
        )
    }

    override suspend fun getTaskWithId(id: Long): Task {
        return todoQueries.getTaskWithId(id).executeAsOne().toDomainTask()
    }

    override suspend fun removeTaskWithId(id: Long) {
        todoQueries.removeTaskWithId(id)
    }

    override suspend fun pinTask(id: Long) {
        todoQueries.pinTask(id)
    }

    override suspend fun unPinTask(id: Long) {
        todoQueries.unPinTask(id)
    }

    override suspend fun getAllTasks(): List<Task> {
        return todoQueries.getAllTasks().executeAsList().map {
            it.toDomainTask()
        }
    }

    override suspend fun deleteAllTasks() {
        todoQueries.deleteAllTasks()
    }
}