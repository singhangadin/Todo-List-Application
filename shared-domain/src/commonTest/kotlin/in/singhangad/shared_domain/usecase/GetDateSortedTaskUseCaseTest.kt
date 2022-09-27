package `in`.singhangad.shared_domain.usecase

import `in`.singhangad.shared_domain.entity.Task
import `in`.singhangad.shared_domain.repository.FakeTaskRepository
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import java.util.*
import kotlin.random.Random
import kotlin.test.*


class GetDateSortedTaskUseCaseTest {

    private lateinit var getTaskUseCase: GetDateSortedTaskUseCase
    private lateinit var fakeTaskRepository: FakeTaskRepository

    @BeforeTest
    fun init() {
        fakeTaskRepository = FakeTaskRepository()
        getTaskUseCase = GetDateSortedTaskUseCase(fakeTaskRepository)
    }

    @Test
    fun testUseCaseResult() = runTest {
        // GIVEN
        val items = getRandomTasks(10)
        for (item in items) {
            fakeTaskRepository.createNewTask(item)
        }

        // WHEN
        val getData = getTaskUseCase.invoke()

        // THEN

        // Convert to linear list
        val list = getData.flatMap {
            it.value
        }

        // Validate size of result data
        assertEquals(list.size, items.size)

        // Validate content in result data
        for (item in items) {
            assertTrue(list.contains(item))
        }

        // Validate distinct
        val pinnedCount = items.count { it.isPinned }
        val unPinnedCount = items.count { !it.isPinned }

        assertTrue(pinnedCount == getData[true]?.count())
        assertTrue(unPinnedCount == getData[false]?.count())

        // Validate Data is sorted
        getData.forEach { (_, values) ->
            assertTrue(isSorted(values))
        }
    }

    private fun isSorted(items: List<Task>): Boolean {
        for (i in 0 until items.size - 1) {
            if (items[i].createdAt > items[i + 1].createdAt) {
                return false
            }
        }
        return true
    }

    private fun getRandomTasks(count: Int): List<Task> {
        val list = mutableListOf<Task>()
        for (i in 0 until count - 1) {
            val randomNumber = Random.nextInt(Int.MAX_VALUE)

            list.add(
                Task(
                    randomNumber.toString(),
                    "Task Title", "Task Description",
                    randomNumber % 2 == 0, Clock.System.now().toEpochMilliseconds() + randomNumber,
                    Clock.System.now().toEpochMilliseconds()
                )
            )
        }
        return list
    }

    @AfterTest
    fun tearDown() = fakeTaskRepository.clear()
}