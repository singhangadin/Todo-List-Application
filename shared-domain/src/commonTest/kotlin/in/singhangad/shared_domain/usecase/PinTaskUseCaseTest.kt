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


class PinTaskUseCaseTest {

    private lateinit var pinTaskUseCase: PinTaskUseCase
    private lateinit var fakeTaskRepository: FakeTaskRepository

    @BeforeTest
    fun init() {
        fakeTaskRepository = FakeTaskRepository()
        pinTaskUseCase = PinTaskUseCase(fakeTaskRepository)
    }

    @Test
    fun testPinWithSuccess() = runTest {
        val testTask = Task(
            "testId", "Title",
            "Description", false,
            Clock.System.now().toEpochMilliseconds(),
            Clock.System.now().toEpochMilliseconds()
        )

        val savedTask = fakeTaskRepository.createNewTask(task = testTask)
        assertTrue(fakeTaskRepository.getTasks(forceUpdate = false).getOrNull()?.size == 1)

        val result = pinTaskUseCase.invoke(
            PinTaskUseCase.UseCaseParams(savedTask.getOrNull()?.taskId!!)
        )

        assertTrue(result.isSuccess)
        assertTrue(fakeTaskRepository.getTaskById(savedTask.getOrNull()?.taskId!!).getOrNull()?.isPinned!!)
    }

    @Test
    fun testPinWithFailure() = runTest {
        val testTask = Task(
            "1", "Title",
            "Description", false,
            Clock.System.now().toEpochMilliseconds(),
            Clock.System.now().toEpochMilliseconds()
        )

        val savedTask = fakeTaskRepository.createNewTask(task = testTask)
        assertTrue(fakeTaskRepository.getTasks(forceUpdate = false).getOrNull()?.size == 1)

        val result = pinTaskUseCase.invoke(
            PinTaskUseCase.UseCaseParams(savedTask.getOrNull()?.taskId!! + "test")
        )
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is DataNotFoundException)
    }

    @AfterTest
    fun tearDown() {
        fakeTaskRepository.clear()
    }
}