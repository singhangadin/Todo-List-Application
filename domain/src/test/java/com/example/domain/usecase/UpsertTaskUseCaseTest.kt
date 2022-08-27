package com.example.domain.usecase

import com.example.domain.contract.TaskRepositoryContract
import com.example.domain.entity.Task
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import java.util.*


class UpsertTaskUseCaseTest {
    private lateinit var upsertTaskUseCase: UpsertTaskUseCase

    @Mock
    private lateinit var taskRepositoryContract: TaskRepositoryContract

    @Before
    fun init() {
        MockitoAnnotations.openMocks(this);
        upsertTaskUseCase = UpsertTaskUseCase(
            CreateTaskUseCase(taskRepositoryContract),
            UpdateTaskUseCase(taskRepositoryContract)
        )
    }

    @Test
    fun testCreate() = runTest {
        // GIVEN
        val testTask = Task(
            null, "Title",
            "Description", false,
            Date(), Date()
        )

        // WHEN
        upsertTaskUseCase.invoke(
            UpsertTaskUseCase.UseCaseParams(
                testTask
            )
        )

        // THEN
        // Not testing implementation as they are already tested in other tests
        verify(taskRepositoryContract).createNewTask(any())
    }

    @Test
    fun testUpdate() = runTest {
        // GIVEN
        val testTask = Task(
            "1", "Title",
            "Description", false,
            Date(), Date()
        )

        // WHEN
        upsertTaskUseCase.invoke(
            UpsertTaskUseCase.UseCaseParams(
                testTask
            )
        )

        // THEN
        // Not testing implementation as they are already tested in other tests
        verify(taskRepositoryContract).updateTask(eq(testTask.taskId!!), any())
    }
}