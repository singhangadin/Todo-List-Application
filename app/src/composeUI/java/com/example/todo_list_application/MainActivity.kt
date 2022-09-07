package com.example.todo_list_application

import `in`.singhangad.compose_ui.savetask.SaveTaskScreen
import `in`.singhangad.ui_common.savetask.viewmodel.SaveTaskViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity: ComponentActivity() {

    val viewModel by viewModels<SaveTaskViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SaveTaskScreen(viewModel)
        }
    }
}