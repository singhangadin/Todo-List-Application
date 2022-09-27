package `in`.singhangad.shared_domain.usecase

import `in`.singhangad.shared_domain.entity.Task
import `in`.singhangad.shared_domain.exception.DataNotFoundException
import `in`.singhangad.shared_domain.repository.FakeTaskRepository
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue


class UpdateTaskUseCaseTest {

    private lateinit var updateTaskUseCase: UpdateTaskUseCase
    private lateinit var fakeTaskRepository: FakeTaskRepository

    @BeforeTest
    fun init() {
        fakeTaskRepository = FakeTaskRepository()
        updateTaskUseCase = UpdateTaskUseCase(fakeTaskRepository)
    }

    @Test
    fun testUpdateSuccess() = runTest {
        val testTask = Task(
            "testId", "Title",
            "Description", true,
            Clock.System.now().toEpochMilliseconds(),
            Clock.System.now().toEpochMilliseconds()
        )

        val savedTask = fakeTaskRepository.createNewTask(task = testTask)
        assertTrue(fakeTaskRepository.getTasks(forceUpdate = false).getOrNull()?.size == 1)

        val result = updateTaskUseCase.invoke(
            UpdateTaskUseCase.UseCaseParams(
                savedTask.getOrNull()?.taskId!!,
                savedTask.getOrNull()?.copy(taskDescription = "Description 2")!!
            )
        )

        assertTrue(result.isSuccess)
        assertTrue(
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
            Clock.System.now().toEpochMilliseconds(),
            Clock.System.now().toEpochMilliseconds()
        )

        val savedTask = fakeTaskRepository.createNewTask(task = testTask)
        assertTrue(fakeTaskRepository.getTasks(forceUpdate = false).getOrNull()?.size == 1)

        val result = updateTaskUseCase.invoke(
            UpdateTaskUseCase.UseCaseParams(savedTask.getOrNull()?.taskId!! + "test", testTask)
        )
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is DataNotFoundException)
    }

    @AfterTest
    fun tearDown() {
        fakeTaskRepository.clear()
    }
}