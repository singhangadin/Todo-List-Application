package com.example.ui.savetask.uistate

sealed class SaveTaskUIState {
    object ShowLoader: SaveTaskUIState()
    object HideLoader: SaveTaskUIState()

    class ShowMessage(val message: Int): SaveTaskUIState()

    object Success: SaveTaskUIState()
    object ShowDatePicker : SaveTaskUIState()
}