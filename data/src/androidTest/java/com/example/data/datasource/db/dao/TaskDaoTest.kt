package com.example.data.datasource.db.dao

import com.example.data.datasource.db.TodoDatabase
import com.example.data.datasource.db.entity.Task
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class TaskDaoTest {
    private lateinit var database: TodoDatabase
    private lateinit var dao: TaskDao

    @Before
    fun init() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(appContext, TodoDatabase::class.java)
            .build()
        dao = database.getTaskDao()
    }

    @Test
    fun testWriteAndReadList() = runTest {
        // GIVEN
        val task = getTask(
            1,
            "Task Title",
            "Task Description",
            false
        )

        // WHEN
        dao.insertTask(task)
        val addedTask = dao.getTaskWithId(1)

        // THEN
        Assert.assertEquals(task.taskId, addedTask!!.taskId)
        Assert.assertEquals(task.taskTitle, addedTask.taskTitle)
        Assert.assertEquals(task.isPinned, addedTask.isPinned)
        Assert.assertEquals(task.taskDescription, addedTask.taskDescription)
    }

    @Test
    fun testDeletionOfTask() = runTest {
        // GIVEN
        val task = getTask(
            1,
            "Task Title",
            "Task Description",
            false
        )
        dao.insertTask(task)

        Assert.assertTrue(dao.getAllTasks().isNotEmpty())

        // WHEN
        dao.deleteTask(task)

        // THEN
        Assert.assertTrue(dao.getAllTasks().isEmpty())
    }

    @Test
    fun testRemoveTaskById() = runTest {
        // GIVEN
        val task = getTask(
            1,
            "Task Title",
            "Task Description",
            false
        )
        dao.insertTask(task)

        Assert.assertTrue(dao.getAllTasks().isNotEmpty())

        // WHEN
        dao.removeTaskWithId(task.taskId!!)

        // THEN
        Assert.assertTrue(dao.getAllTasks().isEmpty())
    }

    @Test
    fun testMarkTaskAsComplete() = runTest {
        // GIVEN
        val task = getTask(
            1,
            "Task Title",
            "Task Description",
            false
        )

        dao.insertTask(task)

        // WHEN
        dao.pinTask(task.taskId!!)

        // THEN
        val updatedTask = dao.getTaskWithId(task.taskId!!)
        Assert.assertEquals(updatedTask!!.isPinned, true)
    }

    @Test
    fun testMarkTaskAsInComplete() = runTest {
        // GIVEN
        val task = getTask(
            1,
            "Task Title",
            "Task Description",
            true
        )

        dao.insertTask(task)

        // WHEN
        dao.unPinTask(task.taskId!!)

        // THEN
        val updatedTask = dao.getTaskWithId(task.taskId!!)
        Assert.assertEquals(updatedTask!!.isPinned, false)
    }

    private fun getTask(id: Int, title: String, description: String, isComplete: Boolean): Task = Task(
        id, title, description, isComplete, Date(), Date()
    )

    @After
    fun tearDown() {
        database.clearAllTables()
        database.close()
    }
}