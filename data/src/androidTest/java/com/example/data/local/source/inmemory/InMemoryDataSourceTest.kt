package com.example.data.local.source.inmemory

import com.example.domain.entity.Task
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class InMemoryDataSourceTest {
    private lateinit var remoteDataSource: InMemoryTaskDataSource

    @Before
    fun init() {
        remoteDataSource = InMemoryTaskDataSource()
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
        val savedTask = remoteDataSource.insertTask(testTask)
        
        // THEN

        Assert.assertEquals(testTask.taskId, savedTask!!.taskId)
        Assert.assertEquals(testTask.taskTitle, savedTask.taskTitle)
        Assert.assertEquals(testTask.isPinned, savedTask.isPinned)
        Assert.assertEquals(testTask.taskDescription, savedTask.taskDescription)
    }

    @Test
    fun testDeleteTaskSuccess() = runTest {
        // GIVEN
        remoteDataSource.insertTask(testTask)

        // WHEN
        remoteDataSource.deleteTask(testTask)

        // THEN
        val task = remoteDataSource.getTaskWithId(testTask.taskId!!)
        Assert.assertNull(task)
    }

    @Test
    fun testUpdateTaskSuccess() = runTest {
        // GIVEN
        remoteDataSource.insertTask(testTask)

        val savedTask = remoteDataSource.getTaskWithId(testTask.taskId!!)
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
        remoteDataSource.updateTask(updatedTask)

        // THEN
        val savedUpdatedTask = remoteDataSource.getTaskWithId(testTask.taskId!!)
        Assert.assertNotNull(savedUpdatedTask)

        Assert.assertEquals(savedUpdatedTask?.taskId, updatedTask.taskId)
        Assert.assertEquals(savedUpdatedTask?.taskTitle, updatedTask.taskTitle)
        Assert.assertEquals(savedUpdatedTask?.isPinned, updatedTask.isPinned)
        Assert.assertEquals(savedUpdatedTask?.taskDescription, updatedTask.taskDescription)
    }

    @Test
    fun testPinTaskSuccess() = runTest {
        // GIVEN
        remoteDataSource.insertTask(testTask)

        // WHEN
        remoteDataSource.pinTask(testTask.taskId!!)

        // THEN
        val savedTask = remoteDataSource.getTaskWithId(testTask.taskId!!)
        Assert.assertNotNull(savedTask)
        Assert.assertEquals(savedTask!!.isPinned, true)
    }

    @Test
    fun testUnpinTaskSuccess() = runTest {
        // GIVEN
        val newTestTask = testTask.copy(isPinned = true)
        remoteDataSource.insertTask(testTask)

        // WHEN
        remoteDataSource.unPinTask(newTestTask.taskId!!)

        // THEN
        val savedTask = remoteDataSource.getTaskWithId(newTestTask.taskId!!)
        Assert.assertNotNull(savedTask)

        Assert.assertEquals(savedTask!!.isPinned, false)
    }

    @After
    fun tearDown() {
        remoteDataSource.clear()
    }
}