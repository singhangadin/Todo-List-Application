package com.example.ui.savetask.viewmodel

import com.example.domain.entity.Task
import com.example.domain.usecase.GetTaskUseCase
import com.example.domain.usecase.UpsertTaskUseCase
import com.example.ui.R
import com.example.ui.savetask.uistate.SaveTaskUIState
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SaveTaskViewModel @Inject constructor(
        private val saveTaskUseCase: UpsertTaskUseCase,
        private val getTaskUseCase: GetTaskUseCase,
        private val dispatcher: CoroutineDispatcher = Dispatchers.Default
): ViewModel() {
    private val taskId = MutableLiveData<String>()
    val taskTitle = MutableLiveData<String>()
    val taskDescription = MutableLiveData<String?>()
    val endDate = MutableLiveData<Date>()

    private val _uiState = MutableSharedFlow<SaveTaskUIState>()

    val uiState: SharedFlow<SaveTaskUIState>
        get() = _uiState


    fun init(taskId: String?) {
        if (taskId != null) {
            this.taskId.value = taskId
            viewModelScope.launch(dispatcher) {

            }
        }
    }

    fun saveTask() {
        viewModelScope.launch(dispatcher) {

        }
    }

    private fun getUpsertTask(oldTask: Task?): Task {
        return Task(
            taskId = taskId.value,
            taskTitle = taskTitle.value?:"",
            taskDescription = taskDescription.value,
            isPinned = oldTask?.isPinned?:false,
            createdAt = oldTask?.createdAt?:Date(),
            endDate = endDate.value!!
        )
    }

    fun showDatePicker() = viewModelScope.launch(dispatcher) {

    }

    private fun validateInput(): Int? {
        if (taskTitle.value.isNullOrEmpty()) {
            return R.string.message_error_title_empty
        } else if (endDate.value == null) {
            return R.string.message_error_data_empty
        } else if (endDate.value!!.time < Date().time) {
            return R.string.message_error_date_limit
        }
        return null
    }
}