package com.example.domain.usecase

import com.example.domain.entity.Task
import com.example.domain.exception.DataNotFoundException
import com.example.domain.repository.FakeTaskRepository
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.*


class GetTaskUseCaseTest {

    private lateinit var getTaskUseCase: GetTaskUseCase
    private lateinit var fakeTaskRepository: FakeTaskRepository

    @Before
    fun init() {
        fakeTaskRepository = FakeTaskRepository()
        getTaskUseCase = GetTaskUseCase(fakeTaskRepository)
    }

    @Test
    fun testGetWithSuccess() = runTest {
        val testTask = Task(
            "testId", "Title",
            "Description", false,
            Date(), Date()
        )

        val savedTask = fakeTaskRepository.createNewTask(task = testTask)
        Assert.assertTrue(fakeTaskRepository.getTasks(forceUpdate = false).getOrNull()?.size == 1)

        val result = getTaskUseCase.invoke(savedTask.getOrNull()?.taskId!!)

        Assert.assertTrue(result.isSuccess)
        Assert.assertTrue(testTask == result.getOrNull())
    }

    @Test
    fun testGetWithFailure() = runTest {
        val testTask = Task(
            "1", "Title",
            "Description", false,
            Date(), Date()
        )

        fakeTaskRepository.createNewTask(task = testTask)
        Assert.assertTrue(fakeTaskRepository.getTasks(forceUpdate = false).getOrNull()?.size == 1)

        val result = getTaskUseCase.invoke(testTask.taskId+"test")
        Assert.assertTrue(result.isFailure)
        Assert.assertTrue(result.exceptionOrNull() is DataNotFoundException)
    }

    @After
    fun tearDown() {
        fakeTaskRepository.clear()
    }
}