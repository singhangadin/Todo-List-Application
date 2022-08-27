package com.example.domain.usecase

import com.example.domain.entity.Task
import com.example.domain.repository.FakeTaskRepository
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.*


class GetDateSortedTaskUseCaseTest {

    private lateinit var getTaskUseCase: GetDateSortedTaskUseCase
    private lateinit var fakeTaskRepository: FakeTaskRepository

    @Before
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
        Assert.assertEquals(list.size, items.size)

        // Validate content in result data
        for (item in items) {
            Assert.assertTrue(list.contains(item))
        }

        // Validate distinct
        val pinnedCount = items.count { it.isPinned }
        val unPinnedCount = items.count { !it.isPinned }

        Assert.assertTrue(pinnedCount == getData[true]?.count())
        Assert.assertTrue(unPinnedCount == getData[false]?.count())

        // Validate Data is sorted
        getData.forEach { (_, values) ->
            Assert.assertTrue(isSorted(values))
        }
    }

    private fun isSorted(items: List<Task>): Boolean {
        for (i in 0 until items.size - 1) {
            if (items[i].createdAt.time > items[i + 1].createdAt.time) {
                return false
            }
        }
        return true
    }

    private fun getRandomTasks(count: Int): List<Task> {
        val list = mutableListOf<Task>()
        for (i in 0 until count - 1) {
            val randomNumber = Random().nextInt(Integer.MAX_VALUE)
            val calendar = Calendar.getInstance()

            calendar.add(Calendar.DAY_OF_YEAR, randomNumber % 365)
            calendar.add(Calendar.MONTH, randomNumber % 12)

            list.add(
                Task(
                    randomNumber.toString(),
                    "Task Title", "Task Description",
                    randomNumber % 2 == 0, calendar.time,
                    Date()
                )
            )
        }
        return list
    }

    @After
    fun tearDown() = fakeTaskRepository.clear()
}