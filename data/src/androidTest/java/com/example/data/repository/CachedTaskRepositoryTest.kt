package com.example.data.repository

import com.example.data.datasource.db.DBTaskDataSource
import com.example.data.datasource.db.TodoDatabase
import com.example.data.datasource.file.FileTaskDataSource
import com.example.domain.contract.TaskRepositoryContract
import com.example.domain.entity.Task
import com.example.domain.exception.DataNotFoundException
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import kotlinx.coroutines.test.runTest
import org.junit.*
import org.junit.runner.RunWith
import java.io.File
import java.util.*

@RunWith(AndroidJUnit4::class)
class CachedTaskRepositoryTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: TodoDatabase
    private lateinit var repository: TaskRepositoryContract
    private lateinit var filePath: String

    @Before
    fun init() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(appContext, TodoDatabase::class.java)
            .build()
        val dao = database.getTaskDao()
        val dbTaskDataSource = DBTaskDataSource(dao)

        filePath = appContext.filesDir.path + "/tasks.txt"
        val fileDataSource = FileTaskDataSource(Gson(), filePath)

        val logService = AndroidLogService()
        repository = CachedTaskRepository(dbTaskDataSource, fileDataSource, logService)
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
        repository.createNewTask(testTask)

        // THEN
        val savedTaskResult = repository.getTaskById(testTask.taskId!!)
        Assert.assertTrue(savedTaskResult.isSuccess)
        Assert.assertNotNull(savedTaskResult.getOrNull())

        val savedTask = savedTaskResult.getOrNull()

        Assert.assertEquals(testTask.taskId, savedTask!!.taskId)
        Assert.assertEquals(testTask.taskTitle, savedTask.taskTitle)
        Assert.assertEquals(testTask.isPinned, savedTask.isPinned)
        Assert.assertEquals(testTask.taskDescription, savedTask.taskDescription)
    }

    @Test
    fun testDeleteTaskSuccess() = runTest {
        // GIVEN
        repository.createNewTask(testTask)

        // WHEN
        val savedTaskResult = repository.getTaskById(testTask.taskId!!)
        Assert.assertTrue(savedTaskResult.isSuccess)
        Assert.assertNotNull(savedTaskResult.getOrNull())

        val deleteResult = repository.deleteTask(testTask.taskId!!)

        // THEN
        Assert.assertTrue(deleteResult.isSuccess)

        val savedTaskResultAfterDelete = repository.getTaskById(testTask.taskId!!)
        Assert.assertTrue(savedTaskResultAfterDelete.isFailure)
        Assert.assertNotNull(savedTaskResultAfterDelete.exceptionOrNull())
        Assert.assertTrue(savedTaskResultAfterDelete.exceptionOrNull() is DataNotFoundException)
    }

    @Test
    fun testDeleteTaskFailure() = runTest {
        // WHEN
        val deleteResult = repository.deleteTask(testTask.taskId!!)

        // THEN
        Assert.assertTrue(deleteResult.isFailure)
        Assert.assertNotNull(deleteResult.exceptionOrNull())
        Assert.assertTrue(deleteResult.exceptionOrNull() is DataNotFoundException)
    }

    @Test
    fun testUpdateTaskSuccess() = runTest {
        // GIVEN
        repository.createNewTask(testTask)

        val savedTaskResult = repository.getTaskById(testTask.taskId!!)
        Assert.assertTrue(savedTaskResult.isSuccess)
        Assert.assertNotNull(savedTaskResult.getOrNull())
        Assert.assertTrue(savedTaskResult.getOrNull()!!.taskTitle == testTask.taskTitle)

        val updatedTask = Task(
            testTask.taskId,
            "Test Title 2",
            "Test Description 2",
            false,
            Date(),
            Date()
        )

        // WHEN
        repository.updateTask(testTask.taskId!!, updatedTask)

        // THEN
        val afterUpdateTaskResult = repository.getTaskById(testTask.taskId!!)
        Assert.assertTrue(afterUpdateTaskResult.isSuccess)
        Assert.assertNotNull(afterUpdateTaskResult.getOrNull())

        val savedTask = afterUpdateTaskResult.getOrNull()

        Assert.assertEquals(updatedTask.taskId, savedTask!!.taskId)
        Assert.assertEquals(updatedTask.taskTitle, savedTask.taskTitle)
        Assert.assertEquals(updatedTask.isPinned, savedTask.isPinned)
        Assert.assertEquals(updatedTask.taskDescription, savedTask.taskDescription)
    }

    @Test
    fun testUpdateTaskFailure() = runTest {
        // WHEN
        val updateTaskResult = repository.updateTask(testTask.taskId!!, testTask)

        // THEN
        Assert.assertTrue(updateTaskResult.isFailure)
        Assert.assertNotNull(updateTaskResult.exceptionOrNull())
        Assert.assertTrue(updateTaskResult.exceptionOrNull() is DataNotFoundException)
    }

    @Test
    fun testPinTaskSuccess() = runTest {
        // GIVEN
        repository.createNewTask(testTask)

        // WHEN
        val updateTaskResult = repository.pinTask(testTask.taskId!!)

        // THEN
        Assert.assertTrue(updateTaskResult.isSuccess)
        Assert.assertNotNull(updateTaskResult.getOrNull())

        val savedTaskResult = repository.getTaskById(testTask.taskId!!)
        Assert.assertTrue(savedTaskResult.isSuccess)
        Assert.assertNotNull(savedTaskResult.getOrNull())

        val savedTask = savedTaskResult.getOrNull()

        Assert.assertEquals(savedTask!!.isPinned, true)
    }

    @Test
    fun testPinTaskFailure() = runTest {
        // WHEN
        val updateTaskResult = repository.pinTask(testTask.taskId!!)

        // THEN
        Assert.assertTrue(updateTaskResult.isFailure)
        Assert.assertNotNull(updateTaskResult.exceptionOrNull())
        Assert.assertTrue(updateTaskResult.exceptionOrNull() is DataNotFoundException)
    }

    @Test
    fun testUnpinTaskSuccess() = runTest {
        // GIVEN
        val newTestTask = testTask.copy(isPinned = true)
        repository.createNewTask(newTestTask)

        // WHEN
        val updateTaskResult = repository.unPinTask(newTestTask.taskId!!)

        // THEN
        Assert.assertTrue(updateTaskResult.isSuccess)
        Assert.assertNotNull(updateTaskResult.getOrNull())

        val savedTaskResult = repository.getTaskById(newTestTask.taskId!!)
        Assert.assertTrue(savedTaskResult.isSuccess)
        Assert.assertNotNull(savedTaskResult.getOrNull())

        val savedTask = savedTaskResult.getOrNull()

        Assert.assertEquals(savedTask!!.isPinned, false)
    }

    @Test
    fun testUnPinTaskFailure() = runTest {
        // WHEN
        val updateTaskResult = repository.unPinTask(testTask.taskId!!)

        // THEN
        Assert.assertTrue(updateTaskResult.isFailure)
        Assert.assertNotNull(updateTaskResult.exceptionOrNull())
        Assert.assertTrue(updateTaskResult.exceptionOrNull() is DataNotFoundException)
    }

    @After
    fun teardown() {
        File(filePath).apply {
            if (exists()) {
                delete()
            }
        }
        database.clearAllTables()
        database.close()
    }
}