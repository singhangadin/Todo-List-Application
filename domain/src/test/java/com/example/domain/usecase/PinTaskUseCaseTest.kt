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


class PinTaskUseCaseTest {

    private lateinit var pinTaskUseCase: PinTaskUseCase
    private lateinit var fakeTaskRepository: FakeTaskRepository

    @Before
    fun init() {
        fakeTaskRepository = FakeTaskRepository()
        pinTaskUseCase = PinTaskUseCase(fakeTaskRepository)
    }

    @Test
    fun testPinWithSuccess() = runTest {
        val testTask = Task(
            "testId", "Title",
            "Description", false,
            Date(), Date()
        )

        val savedTask = fakeTaskRepository.createNewTask(task = testTask)
        Assert.assertTrue(fakeTaskRepository.getTasks(forceUpdate = false).getOrNull()?.size == 1)

        val result = pinTaskUseCase.invoke(
            PinTaskUseCase.UseCaseParams(savedTask.getOrNull()?.taskId!!)
        )

        Assert.assertTrue(result.isSuccess)
        Assert.assertTrue(fakeTaskRepository.getTaskById(savedTask.getOrNull()?.taskId!!).getOrNull()?.isPinned!!)
    }

    @Test
    fun testPinWithFailure() = runTest {
        val testTask = Task(
            "1", "Title",
            "Description", false,
            Date(), Date()
        )

        val savedTask = fakeTaskRepository.createNewTask(task = testTask)
        Assert.assertTrue(fakeTaskRepository.getTasks(forceUpdate = false).getOrNull()?.size == 1)

        val result = pinTaskUseCase.invoke(
            PinTaskUseCase.UseCaseParams(savedTask.getOrNull()?.taskId!! + "test")
        )
        Assert.assertTrue(result.isFailure)
        Assert.assertTrue(result.exceptionOrNull() is DataNotFoundException)
    }

    @After
    fun tearDown() {
        fakeTaskRepository.clear()
    }
}