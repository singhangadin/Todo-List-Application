package `in`.singhangad.shared_domain.usecase

import `in`.singhangad.shared_domain.entity.Task
import `in`.singhangad.shared_domain.exception.DataNotFoundException
import `in`.singhangad.shared_domain.repository.FakeTaskRepository
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import java.util.*
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue


class GetTaskUseCaseTest {

    private lateinit var getTaskUseCase: GetTaskUseCase
    private lateinit var fakeTaskRepository: FakeTaskRepository

    @BeforeTest
    fun init() {
        fakeTaskRepository = FakeTaskRepository()
        getTaskUseCase = GetTaskUseCase(fakeTaskRepository)
    }

    @Test
    fun testGetWithSuccess() = runTest {
        val testTask = Task(
            "testId", "Title",
            "Description", false,
            Clock.System.now().toEpochMilliseconds(),
            Clock.System.now().toEpochMilliseconds()
        )

        val savedTask = fakeTaskRepository.createNewTask(task = testTask)
        assertTrue(fakeTaskRepository.getTasks(forceUpdate = false).getOrNull()?.size == 1)

        val result = getTaskUseCase.invoke(savedTask.getOrNull()?.taskId!!)

        assertTrue(result.isSuccess)
        assertTrue(testTask == result.getOrNull())
    }

    @Test
    fun testGetWithFailure() = runTest {
        val testTask = Task(
            "1", "Title",
            "Description", false,
            Clock.System.now().toEpochMilliseconds(),
            Clock.System.now().toEpochMilliseconds()
        )

        fakeTaskRepository.createNewTask(task = testTask)
        assertTrue(fakeTaskRepository.getTasks(forceUpdate = false).getOrNull()?.size == 1)

        val result = getTaskUseCase.invoke(testTask.taskId+"test")
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is DataNotFoundException)
    }

    @AfterTest
    fun tearDown() {
        fakeTaskRepository.clear()
    }
}