package `in`.singhangad.ui_common.listtask.viewmodel

import `in`.singhangad.ui_common.R
import `in`.singhangad.ui_common.listtask.entity.ItemType
import `in`.singhangad.ui_common.listtask.entity.TaskListItem
import com.example.domain.entity.Task
import com.example.domain.usecase.DeleteTaskUseCase
import com.example.domain.usecase.GetDateSortedTaskUseCase
import com.example.domain.usecase.PinTaskUseCase
import com.example.domain.usecase.UnPinTaskUseCase
import `in`.singhangad.ui_common.listtask.uistate.TaskListUIState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class TaskListViewModel constructor(
    private val getDateSortedTaskUseCase: GetDateSortedTaskUseCase,
    private val pinTaskUseCase: PinTaskUseCase,
    private val unPinTaskUseCase: UnPinTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
): ViewModel() {
    private val _taskList = MutableLiveData<List<TaskListItem>>()

    val taskList: LiveData<List<TaskListItem>>
        get() = _taskList

    private val _uiState = MutableSharedFlow<TaskListUIState>()
    val uiState: SharedFlow<TaskListUIState>
        get() = _uiState

    private fun getHeaderItemType(key: Boolean): TaskListItem {
        return TaskListItem(
            if (key) "true" else "false",
            ItemType.HEADER,
            if (key) "Pinned" else "Tasks",
            null, key
        )
    }

    var updateJob : Job? = null

    private fun updateList(list: MutableList<TaskListItem>) {
        _taskList.postValue(list)
    }

    fun loadData(forceUpdate: Boolean) {
        viewModelScope.launch(dispatcher) {
            _uiState.emit(TaskListUIState.ShowLoader)
            kotlin.runCatching {
                getDateSortedTaskUseCase.invoke(forceUpdate)
            }.map {
                transformToUIList(it)
            }.onSuccess {
                if (it.isEmpty()) {
                    _uiState.emit(TaskListUIState.ShowEmptyView)
                } else {
                    _uiState.emit(TaskListUIState.HideEmptyView)
                }

                updateJob?.cancel()
                updateJob = launch {
                    updateList(it)
                }
                _uiState.emit(TaskListUIState.HideLoader)
            }.onFailure {
                _uiState.emit(TaskListUIState.ShowMessage(R.string.message_error_loading_data))
                _uiState.emit(TaskListUIState.HideLoader)
            }
        }
    }

    private fun transformToUIList(it: Map<Boolean, List<Task>>): MutableList<TaskListItem> {
        return mutableListOf<TaskListItem>().apply {
            it.forEach { (key, list) ->
                add (getHeaderItemType(key))
                addAll(list.map { task -> getTaskItemType(task) })
            }
        }
    }

    private fun getTaskItemType(task: Task): TaskListItem {
        return TaskListItem(
            task.taskId!!, ItemType.TASK_ITEM, task.taskTitle, task.taskDescription, task.isPinned
        )
    }

    fun createNewTask() {
        viewModelScope.launch(dispatcher) {
            _uiState.emit(TaskListUIState.ShowSaveTaskScreen(null))
        }
    }

    fun updateTask(taskId: String) {
        viewModelScope.launch(dispatcher) {
            _uiState.emit(TaskListUIState.ShowSaveTaskScreen(taskId))
        }
    }

    fun deleteTask(taskId: String) {
        viewModelScope.launch(dispatcher) {
            kotlin.runCatching {
                _uiState.emit(TaskListUIState.ShowLoader)
                deleteTaskUseCase.invoke(DeleteTaskUseCase.UseCaseParams(taskId))
            }.onSuccess {
                if (it.isSuccess) {
                    _uiState.emit(TaskListUIState.HideLoader)
                    _uiState.emit(TaskListUIState.ShowMessage(R.string.message_task_deleted))
                    loadData(true)
                } else {
                    _uiState.emit(TaskListUIState.HideLoader)
                    _uiState.emit(TaskListUIState.ShowMessage(R.string.message_error_deleting_task))
                }
            }.onFailure {
                _uiState.emit(TaskListUIState.HideLoader)
                _uiState.emit(TaskListUIState.ShowMessage(R.string.message_error_deleting_task))
            }
        }
    }

    fun pinItem(taskId: String) {
        viewModelScope.launch(dispatcher) {
            kotlin.runCatching {
                _uiState.emit(TaskListUIState.ShowLoader)
                pinTaskUseCase.invoke(PinTaskUseCase.UseCaseParams(taskId))
            }.onSuccess {
                if (it.isSuccess) {
                    _uiState.emit(TaskListUIState.HideLoader)
                    _uiState.emit(TaskListUIState.ShowMessage(R.string.message_task_pinned))
                    loadData(false)
                } else {
                    _uiState.emit(TaskListUIState.HideLoader)
                    _uiState.emit(TaskListUIState.ShowMessage(R.string.message_error_pinning_task))
                }
            }.onFailure {
                _uiState.emit(TaskListUIState.HideLoader)
                _uiState.emit(TaskListUIState.ShowMessage(R.string.message_error_pinning_task))
            }
        }
    }

    fun unPinItem(taskId: String) {
        viewModelScope.launch(dispatcher) {
            kotlin.runCatching {
                _uiState.emit(TaskListUIState.ShowLoader)
                unPinTaskUseCase.invoke(UnPinTaskUseCase.UseCaseParams(taskId))
            }.onSuccess {
                if (it.isSuccess) {
                    _uiState.emit(TaskListUIState.HideLoader)
                    _uiState.emit(TaskListUIState.ShowMessage(R.string.message_task_unpinned))
                    loadData(false)
                } else {
                    _uiState.emit(TaskListUIState.HideLoader)
                    _uiState.emit(TaskListUIState.ShowMessage(R.string.message_error_unpinning_task))
                }
            }.onFailure {
                _uiState.emit(TaskListUIState.HideLoader)
                _uiState.emit(TaskListUIState.ShowMessage(R.string.message_error_unpinning_task))
            }
        }
    }
}