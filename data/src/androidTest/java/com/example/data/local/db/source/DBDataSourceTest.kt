package com.example.data.local.db.source

import com.example.domain.entity.Task
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
class DBDataSourceTest {
    private lateinit var dbTaskDataSource: DBTaskDataSource
    private lateinit var database: TodoDatabase

    @Before
    fun init() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(appContext, TodoDatabase::class.java)
            .build()
        val dao = database.getTaskDao()
        dbTaskDataSource = DBTaskDataSource(dao)
    }

    private val testTask = Task(
        "1",
        "Test Task Title",
        "Test Task Description",
        false,
        Date(),
        Date()
    )

    @Test
    fun testInsertTask() = runTest {
        // WHEN
        val savedTask = dbTaskDataSource.insertTask(testTask)


        // THEN

        Assert.assertEquals(testTask.taskId, savedTask!!.taskId)
        Assert.assertEquals(testTask.taskTitle, savedTask.taskTitle)
        Assert.assertEquals(testTask.isPinned, savedTask.isPinned)
        Assert.assertEquals(testTask.taskDescription, savedTask.taskDescription)
    }

    @Test
    fun testDeleteTaskSuccess() = runTest {
        // GIVEN
        dbTaskDataSource.insertTask(testTask)

        // WHEN
        dbTaskDataSource.deleteTask(testTask)

        // THEN
        val task = dbTaskDataSource.getTaskWithId(testTask.taskId!!)
        Assert.assertNull(task)
    }

    @Test
    fun testUpdateTaskSuccess() = runTest {
        // GIVEN
        dbTaskDataSource.insertTask(testTask)

        val savedTask = dbTaskDataSource.getTaskWithId(testTask.taskId!!)
        Assert.assertNotNull(savedTask)
        Assert.assertTrue(savedTask?.taskTitle == testTask.taskTitle)

        val updatedTask = Task(
            testTask.taskId,
            "Test Title 2",
            "Test Description 2",
            false,
            Date(),
            Date()
        )

        // WHEN
        dbTaskDataSource.updateTask(updatedTask)

        // THEN
        val savedUpdatedTask = dbTaskDataSource.getTaskWithId(testTask.taskId!!)
        Assert.assertNotNull(savedUpdatedTask)

        Assert.assertEquals(savedUpdatedTask?.taskId, updatedTask.taskId)
        Assert.assertEquals(savedUpdatedTask?.taskTitle, updatedTask.taskTitle)
        Assert.assertEquals(savedUpdatedTask?.isPinned, updatedTask.isPinned)
        Assert.assertEquals(savedUpdatedTask?.taskDescription, updatedTask.taskDescription)
    }

    @Test
    fun testPinTaskSuccess() = runTest {
        // GIVEN
        dbTaskDataSource.insertTask(testTask)

        // WHEN
        dbTaskDataSource.pinTask(testTask.taskId!!)

        // THEN
        val savedTask = dbTaskDataSource.getTaskWithId(testTask.taskId!!)
        Assert.assertNotNull(savedTask)
        Assert.assertEquals(savedTask!!.isPinned, true)
    }

    @Test
    fun testUnpinTaskSuccess() = runTest {
        // GIVEN
        val newTestTask = testTask.copy(isPinned = true)
        dbTaskDataSource.insertTask(testTask)

        // WHEN
        dbTaskDataSource.unPinTask(newTestTask.taskId!!)

        // THEN
        val savedTask = dbTaskDataSource.getTaskWithId(newTestTask.taskId!!)
        Assert.assertNotNull(savedTask)

        Assert.assertEquals(savedTask!!.isPinned, false)
    }

    @After
    fun teardown() {
        database.clearAllTables()
        database.close()
    }
}