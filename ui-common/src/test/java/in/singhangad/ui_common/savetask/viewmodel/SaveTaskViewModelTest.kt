package `in`.singhangad.ui_common.savetask.viewmodel

import `in`.singhangad.ui_common.FakeTaskRepository
import `in`.singhangad.ui_common.MainCoroutineRule
import `in`.singhangad.ui_common.R
import `in`.singhangad.ui_common.savetask.uistate.SaveTaskUIState
import com.example.domain.entity.Task
import com.example.domain.usecase.CreateTaskUseCase
import com.example.domain.usecase.GetTaskUseCase
import com.example.domain.usecase.UpdateTaskUseCase
import com.example.domain.usecase.UpsertTaskUseCase
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

@ExperimentalCoroutinesApi
class SaveTaskViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: SaveTaskViewModel
    private lateinit var fakeRepository: FakeTaskRepository

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setupViewModel() {
        fakeRepository = FakeTaskRepository()
        val getTaskUseCase = GetTaskUseCase(fakeRepository)
        val createTaskUseCase = CreateTaskUseCase(fakeRepository)
        val updateTaskUseCase = UpdateTaskUseCase(fakeRepository)
        val saveTaskUseCase = UpsertTaskUseCase(createTaskUseCase, updateTaskUseCase)
        viewModel = SaveTaskViewModel(
            saveTaskUseCase,
            getTaskUseCase,
            testDispatcher
        )
    }

    @Test
    fun testInitialization() = runTest {
        val results = mutableListOf<SaveTaskUIState>()
        val job = launch(testDispatcher) {
            viewModel.uiState.toList(results)
        }

        viewModel.init(null)
        runCurrent()

        // There are no ui states
        assertTrue(results.size == 0)
        job.cancel()
    }

    @Test
    fun testInitializationWithId() = runTest {
        val testTask = Task(
            null, "Title",
            "Description", false,
            Date(), Date()
        )

        val savedTask = fakeRepository.createNewTask(task = testTask)

        val results = mutableListOf<SaveTaskUIState>()
        val job = launch(testDispatcher) {
            viewModel.uiState.toList(results)
        }

        viewModel.init(savedTask.getOrNull()?.taskId)
        runCurrent()

        // First show loader value
        assertEquals(SaveTaskUIState.ShowLoader, results[0])

        // Hide after the UI is loaded
        assertEquals(SaveTaskUIState.HideLoader, results[1])
        job.cancel()
    }

    @Test
    fun testInitializationError() = runTest {
        val testTask = Task(
            null, "Title",
            "Description", false,
            Date(), Date()
        )

        val savedTask = fakeRepository.createNewTask(task = testTask)

        val results = mutableListOf<SaveTaskUIState>()
        val job = launch(testDispatcher) {
            viewModel.uiState.toList(results)
        }

        viewModel.init(savedTask.getOrNull()?.taskId + "test")
        runCurrent()

        // First show loader value
        assertEquals(SaveTaskUIState.ShowLoader, results[0])

        // Hide after the UI is loaded
        assertEquals(SaveTaskUIState.HideLoader, results[1])

        // Show the error message
        assertTrue(results[2] is SaveTaskUIState.ShowMessage)
        assertEquals((results[2] as SaveTaskUIState.ShowMessage).message,
            R.string.message_task_not_found
        )
        job.cancel()
    }

    @Test
    fun testTitleValidation() = runTest {
        val results = mutableListOf<SaveTaskUIState>()
        val job = launch(testDispatcher) {
            viewModel.uiState.toList(results)
        }

        viewModel.saveTask()
        runCurrent()

        // First show loader value
        assertEquals(SaveTaskUIState.ShowLoader, results[0])

        // Hide after the UI is loaded
        assertEquals(SaveTaskUIState.HideLoader, results[1])

        // Show the error message as title is not entered
        assertTrue(results[2] is SaveTaskUIState.ShowMessage)
        assertEquals((results[2] as SaveTaskUIState.ShowMessage).message,
            R.string.message_error_title_empty
        )
        job.cancel()
    }

    @Test
    fun testEmptyDateValidation() = runTest {
        val testTask = Task(
            null, "Title",
            "Description", false,
            Date(), Date()
        )

        viewModel.taskTitle.value = testTask.taskTitle
        viewModel.taskDescription.value = testTask.taskDescription

        val results = mutableListOf<SaveTaskUIState>()
        val job = launch(testDispatcher) {
            viewModel.uiState.toList(results)
        }

        viewModel.saveTask()
        runCurrent()

        // First show loader value
        assertEquals(SaveTaskUIState.ShowLoader, results[0])

        // Hide after the UI is loaded
        assertEquals(SaveTaskUIState.HideLoader, results[1])

        // Show the error message as date is not entered
        assertTrue(results[2] is SaveTaskUIState.ShowMessage)
        assertEquals((results[2] as SaveTaskUIState.ShowMessage).message,
            R.string.message_error_data_empty
        )
        job.cancel()
    }

    @Test
    fun testOldDateValidation() = runTest {
        val yesterday = Calendar.getInstance()
        yesterday.add(Calendar.DAY_OF_YEAR, -1)

        val testTask = Task(
            null, "Title",
            "Description", false,
            Date(), yesterday.time
        )

        viewModel.taskTitle.value = testTask.taskTitle
        viewModel.taskDescription.value = testTask.taskDescription
        viewModel.endDate.value = testTask.endDate

        val results = mutableListOf<SaveTaskUIState>()
        val job = launch(testDispatcher) {
            viewModel.uiState.toList(results)
        }

        viewModel.saveTask()
        runCurrent()

        // First show loader value
        assertEquals(SaveTaskUIState.ShowLoader, results[0])

        // Hide after the UI is loaded
        assertEquals(SaveTaskUIState.HideLoader, results[1])

        // Show the error message as date is old
        assertTrue(results[2] is SaveTaskUIState.ShowMessage)
        assertEquals(viewModel.endDate.value.toString(), (results[2] as SaveTaskUIState.ShowMessage).message,
            R.string.message_error_date_limit
        )
        job.cancel()
    }

    @Test
    fun testCreateTask() = runTest {
        val tomorrow = Calendar.getInstance()
        tomorrow.add(Calendar.DAY_OF_YEAR, 1)

        val testTask = Task(
            null, "Title",
            "Description", false,
            Date(), tomorrow.time
        )

        viewModel.taskTitle.value = testTask.taskTitle
        viewModel.taskDescription.value = testTask.taskDescription
        viewModel.endDate.value = testTask.endDate

        val results = mutableListOf<SaveTaskUIState>()
        val job = launch(testDispatcher) {
            viewModel.uiState.toList(results)
        }

        viewModel.saveTask()
        runCurrent()

        // First show loader value
        assertEquals(SaveTaskUIState.ShowLoader, results[0])

        // Hide after the UI is loaded
        assertEquals(SaveTaskUIState.HideLoader, results[1])

        // Show the success message
        assertTrue(results[2] is SaveTaskUIState.ShowMessage)
        assertEquals((results[2] as SaveTaskUIState.ShowMessage).message,
            R.string.message_task_saved
        )

        assertTrue(results[3] is SaveTaskUIState.Success)
        job.cancel()
    }

    @Test
    fun testCreateTaskErrorSaving() = runTest {
        val tomorrow = Calendar.getInstance()
        tomorrow.add(Calendar.DAY_OF_YEAR, 1)

        fakeRepository.returnError = true

        val testTask = Task(
            null, "Title",
            "Description", false,
            Date(), tomorrow.time
        )

        viewModel.taskTitle.value = testTask.taskTitle
        viewModel.taskDescription.value = testTask.taskDescription
        viewModel.endDate.value = testTask.endDate

        val results = mutableListOf<SaveTaskUIState>()
        val job = launch(testDispatcher) {
            viewModel.uiState.toList(results)
        }

        viewModel.saveTask()
        runCurrent()

        // First show loader value
        assertEquals(SaveTaskUIState.ShowLoader, results[0])

        // Hide after the UI is loaded
        assertEquals(SaveTaskUIState.HideLoader, results[1])

        // Show the success message
        assertTrue(results[2] is SaveTaskUIState.ShowMessage)
        assertEquals((results[2] as SaveTaskUIState.ShowMessage).message,
            R.string.message_error_saving_task
        )
        job.cancel()
    }

    @Test
    fun testUpdateTask() = runTest {
        val tomorrow = Calendar.getInstance()
        tomorrow.add(Calendar.DAY_OF_YEAR, 1)

        val testTask = Task(
            null, "Title",
            "Description", false,
            Date(), tomorrow.time
        )
        val savedTask = fakeRepository.createNewTask(task = testTask)
        viewModel.init(savedTask.getOrNull()?.taskId)

        val results = mutableListOf<SaveTaskUIState>()

        viewModel.taskTitle.value = "Title 1"
        viewModel.taskDescription.value = "Description 1"

        val job = launch(testDispatcher) {
            viewModel.uiState.toList(results)
        }

        viewModel.saveTask()
        runCurrent()

        // First show loader value
        assertEquals(SaveTaskUIState.ShowLoader, results[0])

        // Hide after the UI is loaded
        assertEquals(SaveTaskUIState.HideLoader, results[1])

        // Show the success message
        assertTrue(results[2] is SaveTaskUIState.ShowMessage)
        assertEquals((results[2] as SaveTaskUIState.ShowMessage).message,
            R.string.message_task_saved
        )

        assertTrue(results[3] is SaveTaskUIState.Success)

        job.cancel()
    }

    @Test
    fun testUpdateTaskErrorSaving() = runTest {
        val tomorrow = Calendar.getInstance()
        tomorrow.add(Calendar.DAY_OF_YEAR, 1)

        val testTask = Task(
            null, "Title",
            "Description", false,
            Date(), tomorrow.time
        )
        val savedTask = fakeRepository.createNewTask(task = testTask)
        viewModel.init(savedTask.getOrNull()?.taskId)

        fakeRepository.returnError = true

        val results = mutableListOf<SaveTaskUIState>()

        viewModel.taskTitle.value = "Title 1"
        viewModel.taskDescription.value = "Description 1"

        val job = launch(testDispatcher) {
            viewModel.uiState.toList(results)
        }

        viewModel.saveTask()
        runCurrent()

        // First show loader value
        assertEquals(SaveTaskUIState.ShowLoader, results[0])

        // Hide after the UI is loaded
        assertEquals(SaveTaskUIState.HideLoader, results[1])

        // Show the success message
        assertTrue(results[2] is SaveTaskUIState.ShowMessage)
        assertEquals((results[2] as SaveTaskUIState.ShowMessage).message,
            R.string.message_error_saving_task
        )
        job.cancel()
    }

    @Test
    fun testDatePickerDialogShow() = runTest {
        val results = mutableListOf<SaveTaskUIState>()

        val job = launch(testDispatcher) {
            viewModel.uiState.toList(results)
        }

        viewModel.showDatePicker()
        runCurrent()
        assertTrue(results[0] is SaveTaskUIState.ShowDatePicker)

        job.cancel()
    }

    @After
    fun tearDown() = fakeRepository.clear()
}