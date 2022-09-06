package `in`.singhangad.compose_ui.savetask

import `in`.singhangad.compose_ui.R
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SaveTaskScreen() {
    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar {  }
            }
        ) { contentPadding ->
            Column(modifier = Modifier.padding(contentPadding)) {
                Text(text = stringResource(R.string.label_task_title))
                TextField(value = TextFieldValue(), onValueChange = {

                })
                Text(text = stringResource(R.string.label_task_description))
                TextField(value = TextFieldValue(), onValueChange = {

                })
                Text(text = stringResource(R.string.label_task_doc))
            }
        }
    }
}

@Preview
@Composable
fun showPreview(){
    SaveTaskScreen()
}