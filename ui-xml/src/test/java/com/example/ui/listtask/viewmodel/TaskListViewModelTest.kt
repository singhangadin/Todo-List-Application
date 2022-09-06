package com.example.ui.listtask.viewmodel

import com.example.domain.entity.Task
import com.example.domain.usecase.DeleteTaskUseCase
import com.example.domain.usecase.GetDateSortedTaskUseCase
import com.example.domain.usecase.PinTaskUseCase
import com.example.domain.usecase.UnPinTaskUseCase
import com.example.ui.FakeTaskRepository
import com.example.ui.MainCoroutineRule
import com.example.ui.R
import com.example.ui.listtask.uistate.TaskListUIState
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.*
import java.util.*

@ExperimentalCoroutinesApi
class TaskListViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: TaskListViewModel
    private lateinit var fakeRepository: FakeTaskRepository

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setupViewModel() {
        fakeRepository = FakeTaskRepository()
        val getDateSortedTaskUseCase = GetDateSortedTaskUseCase(fakeRepository)
        val pinTaskUseCase = PinTaskUseCase(fakeRepository)
        val unPinTaskUseCase = UnPinTaskUseCase(fakeRepository)
        val deleteTaskUseCase = DeleteTaskUseCase(fakeRepository)

        viewModel = TaskListViewModel(
            getDateSortedTaskUseCase, pinTaskUseCase, unPinTaskUseCase, deleteTaskUseCase, testDispatcher
        )
    }

    @Test
    fun testShowDataEmptyScreen() = runTest {
        val results = mutableListOf<TaskListUIState>()

        val job = launch(testDispatcher) {
            viewModel.uiState.toList(results)
        }

        viewModel.loadData(true)
        runCurrent()
        Assert.assertTrue(results[0] is TaskListUIState.ShowLoader)
        Assert.assertTrue(results[1] is TaskListUIState.ShowEmptyView)
        Assert.assertTrue(results[2] is TaskListUIState.HideLoader)

        job.cancel()
    }

    @Test
    fun testShowDataScreen() = runTest {
        val testTask = Task(
            null, "Title",
            "Description", false,
            Date(), Date()
        )

        fakeRepository.createNewTask(task = testTask)

        val results = mutableListOf<TaskListUIState>()

        val job = launch(testDispatcher) {
            viewModel.uiState.toList(results)
        }

        viewModel.loadData(true)
        runCurrent()
        Assert.assertTrue(results[0] is TaskListUIState.ShowLoader)
        Assert.assertTrue(results[1] is TaskListUIState.HideEmptyView)
        Assert.assertTrue(results[2] is TaskListUIState.HideLoader)

        job.cancel()
    }

    @Test
    fun testShowErrorMessage() = runTest {
        val testTask = Task(
            null, "Title",
            "Description", false,
            Date(), Date()
        )

        fakeRepository.createNewTask(task = testTask)
        fakeRepository.throwError = true

        val results = mutableListOf<TaskListUIState>()

        val job = launch(testDispatcher) {
            viewModel.uiState.toList(results)
        }

        viewModel.loadData(false)
        runCurrent()
        Assert.assertTrue(results[0] is TaskListUIState.ShowLoader)
        Assert.assertTrue(results[1] is TaskListUIState.ShowMessage)
        Assert.assertTrue((results[1] as TaskListUIState.ShowMessage).message == R.string.message_error_loading_data)
        Assert.assertTrue(results[2] is TaskListUIState.HideLoader)

        job.cancel()
    }

    @Test
    fun testCreateNewTask() = runTest {
        val results = mutableListOf<TaskListUIState>()

        val job = launch(testDispatcher) {
            viewModel.uiState.toList(results)
        }

        viewModel.createNewTask()
        runCurrent()
        Assert.assertTrue(results[0] is TaskListUIState.ShowSaveTaskScreen)
        Assert.assertTrue((results[0] as TaskListUIState.ShowSaveTaskScreen).taskId == null)

        job.cancel()
    }

    @Test
    fun testUpdateTask() = runTest {
        val taskId = "Task ID"
        val results = mutableListOf<TaskListUIState>()

        val job = launch(testDispatcher) {
            viewModel.uiState.toList(results)
        }

        viewModel.updateTask(taskId)
        runCurrent()
        Assert.assertTrue(results[0] is TaskListUIState.ShowSaveTaskScreen)
        Assert.assertTrue((results[0] as TaskListUIState.ShowSaveTaskScreen).taskId == taskId)

        job.cancel()
    }

    @Test
    fun testDeleteTaskSuccess() = runTest {
        val testTask = Task(
            null, "Title",
            "Description", false,
            Date(), Date()
        )

        val savedTask = fakeRepository.createNewTask(task = testTask)

        val results = mutableListOf<TaskListUIState>()

        val job = launch(testDispatcher) {
            viewModel.uiState.toList(results)
        }

        viewModel.deleteTask(savedTask.getOrNull()?.taskId!!)
        runCurrent()
        Assert.assertTrue(results[0] is TaskListUIState.ShowLoader)
        Assert.assertTrue(results[1] is TaskListUIState.HideLoader)
        Assert.assertTrue(results[2] is TaskListUIState.ShowMessage)
        Assert.assertTrue((results[2] as TaskListUIState.ShowMessage).message == R.string.message_task_deleted)

        job.cancel()
    }

    @Test
    fun testDeleteTaskFail() = runTest {
        val results = mutableListOf<TaskListUIState>()

        val job = launch(testDispatcher) {
            viewModel.uiState.toList(results)
        }

        viewModel.deleteTask("testTaskId")
        runCurrent()
        Assert.assertTrue(results[0] is TaskListUIState.ShowLoader)
        Assert.assertTrue(results[1] is TaskListUIState.HideLoader)
        Assert.assertTrue(results[2] is TaskListUIState.ShowMessage)
        Assert.assertTrue((results[2] as TaskListUIState.ShowMessage).message == R.string.message_error_deleting_task)

        job.cancel()
    }

    @Test
    fun testDeleteTaskException() = runTest {
        fakeRepository.throwError = true

        val results = mutableListOf<TaskListUIState>()

        val job = launch(testDispatcher) {
            viewModel.uiState.toList(results)
        }

        viewModel.deleteTask("testTaskId")
        runCurrent()
        Assert.assertTrue(results[0] is TaskListUIState.ShowLoader)
        Assert.assertTrue(results[1] is TaskListUIState.HideLoader)
        Assert.assertTrue(results[2] is TaskListUIState.ShowMessage)
        Assert.assertTrue((results[2] as TaskListUIState.ShowMessage).message == R.string.message_error_deleting_task)

        job.cancel()
    }

    @Test
    fun testPinTaskSuccess() = runTest {
        val testTask = Task(
            null, "Title",
            "Description", false,
            Date(), Date()
        )

        val savedTask = fakeRepository.createNewTask(task = testTask)

        val results = mutableListOf<TaskListUIState>()

        val job = launch(testDispatcher) {
            viewModel.uiState.toList(results)
        }

        viewModel.pinItem(savedTask.getOrNull()?.taskId!!)
        runCurrent()
        Assert.assertTrue(results[0] is TaskListUIState.ShowLoader)
        Assert.assertTrue(results[1] is TaskListUIState.HideLoader)
        Assert.assertTrue(results[2] is TaskListUIState.ShowMessage)
        Assert.assertTrue((results[2] as TaskListUIState.ShowMessage).message == R.string.message_task_pinned)

        job.cancel()
    }

    @Test
    fun testPinTaskFail() = runTest {
        val results = mutableListOf<TaskListUIState>()

        val job = launch(testDispatcher) {
            viewModel.uiState.toList(results)
        }

        viewModel.pinItem("testTaskId")
        runCurrent()
        Assert.assertTrue(results[0] is TaskListUIState.ShowLoader)
        Assert.assertTrue(results[1] is TaskListUIState.HideLoader)
        Assert.assertTrue(results[2] is TaskListUIState.ShowMessage)
        Assert.assertTrue((results[2] as TaskListUIState.ShowMessage).message == R.string.message_error_pinning_task)

        job.cancel()
    }

    @Test
    fun testPinTaskException() = runTest {
        fakeRepository.throwError = true

        val results = mutableListOf<TaskListUIState>()

        val job = launch(testDispatcher) {
            viewModel.uiState.toList(results)
        }

        viewModel.pinItem("testTaskId")
        runCurrent()
        Assert.assertTrue(results[0] is TaskListUIState.ShowLoader)
        Assert.assertTrue(results[1] is TaskListUIState.HideLoader)
        Assert.assertTrue(results[2] is TaskListUIState.ShowMessage)
        Assert.assertTrue((results[2] as TaskListUIState.ShowMessage).message == R.string.message_error_pinning_task)

        job.cancel()
    }

    @Test
    fun testUnPinTaskSuccess() = runTest {
        val testTask = Task(
            null, "Title",
            "Description", false,
            Date(), Date()
        )

        val savedTask = fakeRepository.createNewTask(task = testTask)

        val results = mutableListOf<TaskListUIState>()

        val job = launch(testDispatcher) {
            viewModel.uiState.toList(results)
        }

        viewModel.unPinItem(savedTask.getOrNull()?.taskId!!)
        runCurrent()
        Assert.assertTrue(results[0] is TaskListUIState.ShowLoader)
        Assert.assertTrue(results[1] is TaskListUIState.HideLoader)
        Assert.assertTrue(results[2] is TaskListUIState.ShowMessage)
        Assert.assertTrue((results[2] as TaskListUIState.ShowMessage).message == R.string.message_task_unpinned)

        job.cancel()
    }

    @Test
    fun testUnPinTaskFail() = runTest {
        val results = mutableListOf<TaskListUIState>()

        val job = launch(testDispatcher) {
            viewModel.uiState.toList(results)
        }

        viewModel.unPinItem("testTaskId")
        runCurrent()
        Assert.assertTrue(results[0] is TaskListUIState.ShowLoader)
        Assert.assertTrue(results[1] is TaskListUIState.HideLoader)
        Assert.assertTrue(results[2] is TaskListUIState.ShowMessage)
        Assert.assertTrue((results[2] as TaskListUIState.ShowMessage).message == R.string.message_error_unpinning_task)

        job.cancel()
    }

    @Test
    fun testUnPinTaskException() = runTest {
        fakeRepository.throwError = true

        val results = mutableListOf<TaskListUIState>()

        val job = launch(testDispatcher) {
            viewModel.uiState.toList(results)
        }

        viewModel.unPinItem("testTaskId")
        runCurrent()
        Assert.assertTrue(results[0] is TaskListUIState.ShowLoader)
        Assert.assertTrue(results[1] is TaskListUIState.HideLoader)
        Assert.assertTrue(results[2] is TaskListUIState.ShowMessage)
        Assert.assertTrue((results[2] as TaskListUIState.ShowMessage).message == R.string.message_error_unpinning_task)

        job.cancel()
    }

    @After
    fun tearDown() = fakeRepository.clear()
}