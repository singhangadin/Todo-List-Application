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


class UnPinTaskUseCaseTest {

    private lateinit var unPinTaskUseCase: UnPinTaskUseCase
    private lateinit var fakeTaskRepository: FakeTaskRepository

    @Before
    fun init() {
        fakeTaskRepository = FakeTaskRepository()
        unPinTaskUseCase = UnPinTaskUseCase(fakeTaskRepository)
    }

    @Test
    fun testUnPinWithSuccess() = runTest {
        val testTask = Task(
            "testId", "Title",
            "Description", true,
            Date(), Date()
        )

        val savedTask = fakeTaskRepository.createNewTask(task = testTask)
        Assert.assertTrue(fakeTaskRepository.getTasks(forceUpdate = false).getOrNull()?.size == 1)

        val result = unPinTaskUseCase.invoke(
            UnPinTaskUseCase.UseCaseParams(savedTask.getOrNull()?.taskId!!)
        )

        Assert.assertTrue(result.isSuccess)
        Assert.assertFalse(fakeTaskRepository.getTaskById(savedTask.getOrNull()?.taskId!!).getOrNull()?.isPinned!!)
    }

    @Test
    fun testUnPinWithFailure() = runTest {
        val testTask = Task(
            "1", "Title",
            "Description", true,
            Date(), Date()
        )

        val savedTask = fakeTaskRepository.createNewTask(task = testTask)
        Assert.assertTrue(fakeTaskRepository.getTasks(forceUpdate = false).getOrNull()?.size == 1)

        val result = unPinTaskUseCase.invoke(
            UnPinTaskUseCase.UseCaseParams(savedTask.getOrNull()?.taskId!! + "test")
        )
        Assert.assertTrue(result.isFailure)
        Assert.assertTrue(result.exceptionOrNull() is DataNotFoundException)
    }

    @After
    fun tearDown() {
        fakeTaskRepository.clear()
    }
}