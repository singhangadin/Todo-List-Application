package com.example.domain.usecase

import com.example.domain.repository.FakeTaskRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.*


class CreateTaskUseCaseTest {

    private lateinit var createTaskUseCase: CreateTaskUseCase
    private lateinit var fakeTaskRepository: FakeTaskRepository

    @Before
    fun init() {
        fakeTaskRepository = FakeTaskRepository()
        createTaskUseCase = CreateTaskUseCase(fakeTaskRepository)
    }

    @Test
    fun testCreationWithSuccess() = runTest {
        val title = "Title"
        val description = "Description"
        val endDate = Date()

        val result = createTaskUseCase.invoke(CreateTaskUseCase.UseCaseParams(
            title, description, endDate
        ))

        Assert.assertTrue(result.isSuccess)
        Assert.assertNotNull(result.getOrNull())

        Assert.assertEquals(result.getOrNull()?.taskTitle, title)
        Assert.assertEquals(result.getOrNull()?.taskDescription, description)
    }

    @Test
    fun testCreationWithFailure() = runTest {
        val title = "Title"
        val description = "Description"
        val endDate = Date()

        fakeTaskRepository.returnError = true
        val result = createTaskUseCase.invoke(CreateTaskUseCase.UseCaseParams(
            title, description, endDate
        ))

        Assert.assertTrue(result.isFailure)
    }
}