package com.example.data.datasource.file

import com.example.domain.entity.Task
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.util.*

@RunWith(AndroidJUnit4::class)
class FileDataSourceTest {
    private lateinit var fileDataSource: FileTaskDataSource
    private lateinit var filePath: String

    @Before
    fun init() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        filePath = appContext.filesDir.path + "/tasks.txt"
        fileDataSource = FileTaskDataSource(Gson(), filePath)
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
        val savedTask = fileDataSource.insertTask(testTask)
        
        // THEN

        Assert.assertEquals(testTask.taskId, savedTask!!.taskId)
        Assert.assertEquals(testTask.taskTitle, savedTask.taskTitle)
        Assert.assertEquals(testTask.isPinned, savedTask.isPinned)
        Assert.assertEquals(testTask.taskDescription, savedTask.taskDescription)
    }

    @Test
    fun testDeleteTaskSuccess() = runTest {
        // GIVEN
        fileDataSource.insertTask(testTask)

        // WHEN
        fileDataSource.deleteTask(testTask)

        // THEN
        val task = fileDataSource.getTaskWithId(testTask.taskId!!)
        Assert.assertNull(task)
    }

    @Test
    fun testUpdateTaskSuccess() = runTest {
        // GIVEN
        fileDataSource.insertTask(testTask)

        val savedTask = fileDataSource.getTaskWithId(testTask.taskId!!)
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
        fileDataSource.updateTask(updatedTask)

        // THEN
        val savedUpdatedTask = fileDataSource.getTaskWithId(testTask.taskId!!)
        Assert.assertNotNull(savedUpdatedTask)

        Assert.assertEquals(savedUpdatedTask?.taskId, updatedTask.taskId)
        Assert.assertEquals(savedUpdatedTask?.taskTitle, updatedTask.taskTitle)
        Assert.assertEquals(savedUpdatedTask?.isPinned, updatedTask.isPinned)
        Assert.assertEquals(savedUpdatedTask?.taskDescription, updatedTask.taskDescription)
    }

    @Test
    fun testPinTaskSuccess() = runTest {
        // GIVEN
        fileDataSource.insertTask(testTask)

        // WHEN
        fileDataSource.pinTask(testTask.taskId!!)

        // THEN
        val savedTask = fileDataSource.getTaskWithId(testTask.taskId!!)
        Assert.assertNotNull(savedTask)
        Assert.assertEquals(savedTask!!.isPinned, true)
    }

    @Test
    fun testUnpinTaskSuccess() = runTest {
        // GIVEN
        val newTestTask = testTask.copy(isPinned = true)
        fileDataSource.insertTask(testTask)

        // WHEN
        fileDataSource.unPinTask(newTestTask.taskId!!)

        // THEN
        val savedTask = fileDataSource.getTaskWithId(newTestTask.taskId!!)
        Assert.assertNotNull(savedTask)

        Assert.assertEquals(savedTask!!.isPinned, false)
    }

    @After
    fun tearDown() {
        File(filePath).apply {
            if (exists()) {
                delete()
            }
        }
    }
}