package `in`.singhangad.shared_domain.usecase

import `in`.singhangad.shared_domain.entity.Task
import `in`.singhangad.shared_domain.exception.DataNotFoundException
import `in`.singhangad.shared_domain.repository.FakeTaskRepository
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import java.util.*
import kotlin.test.*


class UnPinTaskUseCaseTest {

    private lateinit var unPinTaskUseCase: UnPinTaskUseCase
    private lateinit var fakeTaskRepository: FakeTaskRepository

    @BeforeTest
    fun init() {
        fakeTaskRepository = FakeTaskRepository()
        unPinTaskUseCase = UnPinTaskUseCase(fakeTaskRepository)
    }

    @Test
    fun testUnPinWithSuccess() = runTest {
        val testTask = Task(
            "testId", "Title",
            "Description", true,
            Clock.System.now().toEpochMilliseconds(),
            Clock.System.now().toEpochMilliseconds()
        )

        val savedTask = fakeTaskRepository.createNewTask(task = testTask)
        assertTrue(fakeTaskRepository.getTasks(forceUpdate = false).getOrNull()?.size == 1)

        val result = unPinTaskUseCase.invoke(
            UnPinTaskUseCase.UseCaseParams(savedTask.getOrNull()?.taskId!!)
        )

        assertTrue(result.isSuccess)
        assertFalse(fakeTaskRepository.getTaskById(savedTask.getOrNull()?.taskId!!).getOrNull()?.isPinned!!)
    }

    @Test
    fun testUnPinWithFailure() = runTest {
        val testTask = Task(
            "1", "Title",
            "Description", true,
            Clock.System.now().toEpochMilliseconds(),
            Clock.System.now().toEpochMilliseconds()
        )

        val savedTask = fakeTaskRepository.createNewTask(task = testTask)
        assertTrue(fakeTaskRepository.getTasks(forceUpdate = false).getOrNull()?.size == 1)

        val result = unPinTaskUseCase.invoke(
            UnPinTaskUseCase.UseCaseParams(savedTask.getOrNull()?.taskId!! + "test")
        )
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is DataNotFoundException)
    }

    @AfterTest
    fun tearDown() {
        fakeTaskRepository.clear()
    }
}