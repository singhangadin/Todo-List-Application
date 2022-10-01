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


class DeleteTaskUseCaseTest {

    private lateinit var deleteTaskUseCase: DeleteTaskUseCase
    private lateinit var fakeTaskRepository: FakeTaskRepository

    @BeforeTest
    fun init() {
        fakeTaskRepository = FakeTaskRepository()
        deleteTaskUseCase = DeleteTaskUseCase(fakeTaskRepository)
    }

    @Test
    fun testDeleteWithSuccess() = runTest {
        val testTask = Task(
            null, "Title",
            "Description", false,
            Clock.System.now().toEpochMilliseconds(),
            Clock.System.now().toEpochMilliseconds()
        )

        val savedTask = fakeTaskRepository.createNewTask(task = testTask)

        assertTrue(fakeTaskRepository.getTasks(forceUpdate = false).getOrNull()?.size == 1)

        val result = deleteTaskUseCase.invoke(DeleteTaskUseCase.UseCaseParams(
            savedTask.getOrNull()?.taskId!!
        ))

        assertTrue(result.isSuccess)
        assertTrue(fakeTaskRepository.getTasks(forceUpdate = false).getOrNull()!!.isEmpty())
    }

    @Test
    fun testCreationWithFailure() = runTest {
        val testTask = Task(
            "1", "Title",
            "Description", false,
            Clock.System.now().toEpochMilliseconds(),
            Clock.System.now().toEpochMilliseconds()
        )

        assertTrue(fakeTaskRepository.getTasks(forceUpdate = false).getOrNull()?.size == 0)

        val result = deleteTaskUseCase.invoke(DeleteTaskUseCase.UseCaseParams(
            testTask.taskId!!
        ))

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is DataNotFoundException)
    }

    @AfterTest
    fun tearDown() {
        fakeTaskRepository.clear()
    }
}