package `in`.singhangad.ui_common.savetask.uistate

sealed class SaveTaskUIState {
    object ShowLoader: SaveTaskUIState()
    object HideLoader: SaveTaskUIState()

    class ShowMessage(val message: Int): SaveTaskUIState()

    object Success: SaveTaskUIState()
    object ShowDatePicker : SaveTaskUIState()
}
