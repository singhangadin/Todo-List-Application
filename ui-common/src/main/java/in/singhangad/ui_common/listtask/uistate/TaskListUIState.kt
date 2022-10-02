package `in`.singhangad.ui_common.listtask.uistate

sealed class TaskListUIState {

    object ShowLoader: TaskListUIState()
    object HideLoader: TaskListUIState()

    object ShowEmptyView: TaskListUIState()
    object HideEmptyView: TaskListUIState()

    class ShowMessage(val message: Int): TaskListUIState()
    class ShowSaveTaskScreen(val taskId: Long?): TaskListUIState()
}