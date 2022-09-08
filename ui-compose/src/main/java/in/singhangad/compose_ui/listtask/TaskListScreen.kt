package `in`.singhangad.compose_ui.listtask

import `in`.singhangad.ui_common.listtask.viewmodel.TaskListViewModel
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun TaskListScreen(viewModel: TaskListViewModel?, navigateSave: (String?) -> Unit) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { navigateSave.invoke(null) }) {

            }
        }
    ) { Box(
        modifier = Modifier.padding(it)
    ) {

    }

    }
}

@Preview
@Composable
fun showPreview(){
    TaskListScreen(null) { taskId ->

    }
}