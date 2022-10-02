package `in`.singhangad.ui_common.savetask.viewmodel

import `in`.singhangad.shared_domain.entity.Task
import `in`.singhangad.shared_domain.usecase.GetTaskUseCase
import `in`.singhangad.shared_domain.usecase.UpsertTaskUseCase
import `in`.singhangad.ui_common.R
import `in`.singhangad.ui_common.savetask.uistate.SaveTaskUIState
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.util.*

class SaveTaskViewModel constructor(
    private val saveTaskUseCase: UpsertTaskUseCase,
    private val getTaskUseCase: GetTaskUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
): ViewModel() {
    private val taskId = MutableLiveData<Long>()
    val taskTitle = MutableLiveData<String>()
    val taskDescription = MutableLiveData<String?>()
    val endDate = MutableLiveData<Date>()

    private val _uiState = MutableSharedFlow<SaveTaskUIState>()

    val uiState: SharedFlow<SaveTaskUIState>
        get() = _uiState


    fun init(taskId: Long) {
        if (taskId != -1L) {
            this.taskId.value = taskId
            viewModelScope.launch(dispatcher) {
                _uiState.emit(SaveTaskUIState.ShowLoader)
                val taskResult = getTaskUseCase.invoke(taskId.toLong())
                if (taskResult.isSuccess) {
                    val task = taskResult.getOrNull()!!
                    taskTitle.postValue(task.taskTitle)
                    taskDescription.postValue(task.taskDescription)
                    endDate.postValue(Date(task.endDate))
                    _uiState.emit(SaveTaskUIState.HideLoader)
                } else {
                    _uiState.emit(SaveTaskUIState.HideLoader)
                    _uiState.emit(SaveTaskUIState.ShowMessage(R.string.message_task_not_found))
                }
            }
        }
    }

    fun saveTask() {
        viewModelScope.launch(dispatcher) {
            _uiState.emit(SaveTaskUIState.ShowLoader)
            val message = validateInput()
            if (message == null) {
                kotlin.runCatching {
                    saveTaskUseCase.invoke(
                        UpsertTaskUseCase.UseCaseParams(
                            getUpsertTask(
                                getTaskUseCase.invoke(
                                    taskId.value ?: 0
                                ).getOrNull()
                            )
                        )
                    )
                }.onSuccess {
                    _uiState.emit(SaveTaskUIState.HideLoader)
                    if (it.isSuccess) {
                        _uiState.emit(SaveTaskUIState.ShowMessage(R.string.message_task_saved))
                        _uiState.emit(SaveTaskUIState.Success)
                    } else if (it.isFailure) {
                        it.exceptionOrNull()?.printStackTrace()
                        _uiState.emit(SaveTaskUIState.ShowMessage(R.string.message_error_saving_task))
                    }
                }.onFailure {
                    it.printStackTrace()
                    _uiState.emit(SaveTaskUIState.HideLoader)
                    _uiState.emit(SaveTaskUIState.ShowMessage(R.string.message_error_saving_task))
                }
            } else {
                _uiState.emit(SaveTaskUIState.HideLoader)
                _uiState.emit(SaveTaskUIState.ShowMessage(message))
            }
        }
    }

    private fun getUpsertTask(oldTask: Task?): Task {
        return Task(
            taskId = taskId.value,
            taskTitle = taskTitle.value?:"",
            taskDescription = taskDescription.value,
            isPinned = oldTask?.isPinned?:false,
            createdAt = oldTask?.createdAt?:Date().time,
            endDate = endDate.value?.time!!
        )
    }

    fun showDatePicker() = viewModelScope.launch(dispatcher) {
        _uiState.emit(SaveTaskUIState.ShowDatePicker)
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