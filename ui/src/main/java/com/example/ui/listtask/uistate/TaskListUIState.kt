package com.example.ui.listtask.uistate

sealed class TaskListUIState {

    object ShowLoader: TaskListUIState()
    object HideLoader: TaskListUIState()

    object ShowEmptyView: TaskListUIState()
    object HideEmptyView: TaskListUIState()

    class ShowMessage(val id: Int): TaskListUIState()
}