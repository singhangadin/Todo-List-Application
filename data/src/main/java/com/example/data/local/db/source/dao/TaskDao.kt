package com.example.data.local.db.source.dao

import com.example.data.local.db.source.entity.Task
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert(onConflict = REPLACE)
    suspend fun insertTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Query("SELECT * FROM task_table WHERE task_id = :id")
    suspend fun getTaskWithId(id: Int): Task?

    @Query("DELETE FROM task_table WHERE task_id = :id")
    suspend fun removeTaskWithId(id: Int)

    @Query("UPDATE task_table SET task_pinned = 1 WHERE task_id = :id")
    suspend fun pinTask(id: Int)

    @Query("UPDATE task_table SET task_pinned = 0 WHERE task_id = :id")
    suspend fun unPinTask(id: Int)

    suspend fun taskExist(taskId: Int): Boolean = getTaskWithId(taskId) != null

    @Query("SELECT * FROM task_table")
    suspend fun getAllTasks(): List<Task>

    @Query("DELETE FROM task_table")
    suspend fun deleteAllTasks()

    @Query("SELECT * FROM task_table")
    fun getAllTasksFlow(): Flow<List<Task>>
}