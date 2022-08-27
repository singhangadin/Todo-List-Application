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


class UpdateTaskUseCaseTest {

    private lateinit var updateTaskUseCase: UpdateTaskUseCase
    private lateinit var fakeTaskRepository: FakeTaskRepository

    @Before
    fun init() {
        fakeTaskRepository = FakeTaskRepository()
        updateTaskUseCase = UpdateTaskUseCase(fakeTaskRepository)
    }

    @Test
    fun testUpdateSuccess() = runTest {
        val testTask = Task(
            "testId", "Title",
            "Description", true,
            Date(), Date()
        )

        val savedTask = fakeTaskRepository.createNewTask(task = testTask)
        Assert.assertTrue(fakeTaskRepository.getTasks(forceUpdate = false).getOrNull()?.size == 1)

        val result = updateTaskUseCase.invoke(
            UpdateTaskUseCase.UseCaseParams(
                savedTask.getOrNull()?.taskId!!,
                savedTask.getOrNull()?.copy(taskDescription = "Description 2")!!
            )
        )

        Assert.assertTrue(result.isSuccess)
        Assert.assertTrue(
            fakeTaskRepository.getTaskById(
                savedTask.getOrNull()?.taskId!!
            ).getOrNull()?.taskDescription == "Description 2"
        )
    }

    @Test
    fun testUpdateFailure() = runTest {
        val testTask = Task(
            "1", "Title",
            "Description", true,
            Date(), Date()
        )

        val savedTask = fakeTaskRepository.createNewTask(task = testTask)
        Assert.assertTrue(fakeTaskRepository.getTasks(forceUpdate = false).getOrNull()?.size == 1)

        val result = updateTaskUseCase.invoke(
            UpdateTaskUseCase.UseCaseParams(savedTask.getOrNull()?.taskId!! + "test", testTask)
        )
        Assert.assertTrue(result.isFailure)
        Assert.assertTrue(result.exceptionOrNull() is DataNotFoundException)
    }

    @After
    fun tearDown() {
        fakeTaskRepository.clear()
    }
}