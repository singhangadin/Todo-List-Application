package com.example.data.datasource.datastore

import com.example.domain.entity.Task
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
class DataStoreDataSourceTest {
    private lateinit var dataStoreDataStore: TaskDataStoreSource

    @Before
    fun init() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        dataStoreDataStore = TaskDataStoreSource(appContext)
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
        val savedTask = dataStoreDataStore.insertTask(testTask)
        
        // THEN

        Assert.assertEquals(testTask.taskId, savedTask!!.taskId)
        Assert.assertEquals(testTask.taskTitle, savedTask.taskTitle)
        Assert.assertEquals(testTask.isPinned, savedTask.isPinned)
        Assert.assertEquals(testTask.taskDescription, savedTask.taskDescription)
    }

    @Test
    fun testDeleteTaskSuccess() = runTest {
        // GIVEN
        dataStoreDataStore.insertTask(testTask)

        // WHEN
        dataStoreDataStore.deleteTask(testTask)

        // THEN
        val task = dataStoreDataStore.getTaskWithId(testTask.taskId!!)
        Assert.assertNull(task)
    }

    @Test
    fun testUpdateTaskSuccess() = runTest {
        // GIVEN
        dataStoreDataStore.insertTask(testTask)

        val savedTask = dataStoreDataStore.getTaskWithId(testTask.taskId!!)
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
        dataStoreDataStore.updateTask(updatedTask)

        // THEN
        val savedUpdatedTask = dataStoreDataStore.getTaskWithId(testTask.taskId!!)
        Assert.assertNotNull(savedUpdatedTask)

        Assert.assertEquals(savedUpdatedTask?.taskId, updatedTask.taskId)
        Assert.assertEquals(savedUpdatedTask?.taskTitle, updatedTask.taskTitle)
        Assert.assertEquals(savedUpdatedTask?.isPinned, updatedTask.isPinned)
        Assert.assertEquals(savedUpdatedTask?.taskDescription, updatedTask.taskDescription)
    }

    @Test
    fun testPinTaskSuccess() = runTest {
        // GIVEN
        dataStoreDataStore.insertTask(testTask)

        // WHEN
        dataStoreDataStore.pinTask(testTask.taskId!!)

        // THEN
        val savedTask = dataStoreDataStore.getTaskWithId(testTask.taskId!!)
        Assert.assertNotNull(savedTask)
        Assert.assertEquals(savedTask!!.isPinned, true)
    }

    @Test
    fun testUnpinTaskSuccess() = runTest {
        // GIVEN
        val newTestTask = testTask.copy(isPinned = true)
        dataStoreDataStore.insertTask(testTask)

        // WHEN
        dataStoreDataStore.unPinTask(newTestTask.taskId!!)

        // THEN
        val savedTask = dataStoreDataStore.getTaskWithId(newTestTask.taskId!!)
        Assert.assertNotNull(savedTask)

        Assert.assertEquals(savedTask!!.isPinned, false)
    }

    @After
    fun tearDown() {
        dataStoreDataStore.clear()
    }
}