package `in`.singhangad.shared_domain.usecase

import `in`.singhangad.shared_domain.repository.FakeTaskRepository
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import java.util.*
import kotlin.test.*


class CreateTaskUseCaseTest {

    private lateinit var createTaskUseCase: CreateTaskUseCase
    private lateinit var fakeTaskRepository: FakeTaskRepository

    @BeforeTest
    fun init() {
        fakeTaskRepository = FakeTaskRepository()
        createTaskUseCase = CreateTaskUseCase(fakeTaskRepository)
    }

    @Test
    fun testCreationWithSuccess() = runTest {
        val title = "Title"
        val description = "Description"
        val endDate = Clock.System.now().toEpochMilliseconds()

        val result = createTaskUseCase.invoke(CreateTaskUseCase.UseCaseParams(
            title, description, endDate
        ))

        assertTrue(result.isSuccess)
        assertNotNull(result.getOrNull())

        assertEquals(result.getOrNull()?.taskTitle, title)
        assertEquals(result.getOrNull()?.taskDescription, description)
    }

    @Test
    fun testCreationWithFailure() = runTest {
        val title = "Title"
        val description = "Description"
        val endDate = Clock.System.now().toEpochMilliseconds()

        fakeTaskRepository.returnError = true
        val result = createTaskUseCase.invoke(CreateTaskUseCase.UseCaseParams(
            title, description, endDate
        ))

        assertTrue(result.isFailure)
    }
}