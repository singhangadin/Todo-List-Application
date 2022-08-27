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


class DeleteTaskUseCaseTest {

    private lateinit var deleteTaskUseCase: DeleteTaskUseCase
    private lateinit var fakeTaskRepository: FakeTaskRepository

    @Before
    fun init() {
        fakeTaskRepository = FakeTaskRepository()
        deleteTaskUseCase = DeleteTaskUseCase(fakeTaskRepository)
    }

    @Test
    fun testDeleteWithSuccess() = runTest {
        val testTask = Task(
            null, "Title",
            "Description", false,
            Date(), Date()
        )

        val savedTask = fakeTaskRepository.createNewTask(task = testTask)

        Assert.assertTrue(fakeTaskRepository.getTasks(forceUpdate = false).getOrNull()?.size == 1)

        val result = deleteTaskUseCase.invoke(DeleteTaskUseCase.UseCaseParams(
            savedTask.getOrNull()?.taskId!!
        ))

        Assert.assertTrue(result.isSuccess)
        Assert.assertTrue(fakeTaskRepository.getTasks(forceUpdate = false).getOrNull()!!.isEmpty())
    }

    @Test
    fun testCreationWithFailure() = runTest {
        val testTask = Task(
            "1", "Title",
            "Description", false,
            Date(), Date()
        )

        Assert.assertTrue(fakeTaskRepository.getTasks(forceUpdate = false).getOrNull()?.size == 0)

        val result = deleteTaskUseCase.invoke(DeleteTaskUseCase.UseCaseParams(
            testTask.taskId!!
        ))

        Assert.assertTrue(result.isFailure)
        Assert.assertTrue(result.exceptionOrNull() is DataNotFoundException)
    }

    @After
    fun tearDown() {
        fakeTaskRepository.clear()
    }
}