package `in`.singhangad.compose_ui.savetask

import `in`.singhangad.compose_ui.R
import `in`.singhangad.compose_ui.values.TextTitleStyle
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SaveTaskScreen() {
    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar {
                    Text(text = stringResource(id = R.string.label_task_doc))
                }
            }
        ) {
            Box(modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .padding(
                            dimensionResource(id = R.dimen.margin_regular)
                        )
                        .fillMaxHeight()
                        .fillMaxWidth()
                ) {
                    var titleText by remember { mutableStateOf("") }
                    var descriptionText by remember { mutableStateOf("") }

                    Text(
                        text = stringResource(R.string.label_task_title),
                        style = TextTitleStyle
                    )
                    val horizontalScrollState = rememberScrollState(0)
                    BasicTextField(
                        value = titleText,
                        singleLine = true,
                        onValueChange = { titleText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                            .horizontalScroll(horizontalScrollState),
                        textStyle = TextStyle.Default.copy(fontSize = 16.sp)
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.margin_small)))
                    Text(
                        text = stringResource(R.string.label_task_description),
                        style = TextTitleStyle
                    )
                    val verticalScrollState = rememberScrollState(0)
                    BasicTextField(
                        value = descriptionText,
                        onValueChange = {
                            descriptionText = it
                        },
                        maxLines = 4,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                            .verticalScroll(verticalScrollState),
                        textStyle = TextStyle.Default.copy(fontSize = 16.sp)
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.margin_small)))
                    Text(
                        text = stringResource(R.string.label_task_doc),
                        style = TextTitleStyle
                    )
                    Text(
                        text = stringResource(R.string.label_hint_date),
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Button(modifier = Modifier.fillMaxWidth(), onClick = { /*TODO*/ }) {
                        Text(text = stringResource(id = R.string.label_task_save))
                    }
                }

//                CircularProgressIndicator(
//                    Modifier.align(Alignment.Center)
//                )
            }
        }
    }
}

@Preview
@Composable
fun showPreview(){
    SaveTaskScreen()
}