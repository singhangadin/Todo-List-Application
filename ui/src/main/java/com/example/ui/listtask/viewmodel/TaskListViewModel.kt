package com.example.ui.listtask.viewmodel

import com.example.domain.entity.Task
import com.example.domain.usecase.DeleteTaskUseCase
import com.example.domain.usecase.GetDateSortedTaskUseCase
import com.example.domain.usecase.PinTaskUseCase
import com.example.domain.usecase.UnPinTaskUseCase
import com.example.ui.R
import com.example.ui.listtask.entity.ItemType
import com.example.ui.listtask.entity.TaskListItem
import com.example.ui.listtask.uistate.TaskListUIState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
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

        }
    }

    fun updateTask(taskId: String) {
        viewModelScope.launch(dispatcher) {

        }
    }

    fun deleteTask(taskId: String) {
        viewModelScope.launch(dispatcher) {

        }
    }

    fun pinItem(taskId: String) {
        viewModelScope.launch(dispatcher) {

        }
    }

    fun unPinItem(taskId: String) {
        viewModelScope.launch(dispatcher) {

        }
    }
}