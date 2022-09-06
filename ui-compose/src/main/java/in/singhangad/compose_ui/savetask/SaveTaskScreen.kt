package `in`.singhangad.compose_ui.savetask

import `in`.singhangad.compose_ui.R
import `in`.singhangad.compose_ui.utils.DatePickerDialogFactory
import `in`.singhangad.compose_ui.values.TextTitleStyle
import android.app.Dialog
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.*

@Composable
fun SaveTaskScreen() {
    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar {
                    Text(
                        text = stringResource(id = R.string.label_task_doc)
                    )
                }
            }
        ) {
            Box(modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
            ) {
                var loading by remember { mutableStateOf(false)}
                Column(
                    modifier = Modifier
                        .padding(
                            dimensionResource(id = R.dimen.margin_regular)
                        )
                        .fillMaxHeight()
                        .fillMaxWidth()
                ) {
                    var titleText = remember { mutableStateOf("") }
                    TitleItem(titleText)
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.margin_small)))

                    DescriptionItem()
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.margin_small)))

                    val hintText = stringResource(id = R.string.label_hint_date)
                    val selectedDate = remember {
                        mutableStateOf(
                            hintText
                        )
                    }
                    val mDatePickerDialog = DatePickerDialogFactory.create(LocalContext.current) { year, month, day ->
                        selectedDate.value = "$year/$month/$day"
                    }

                    DateOfCompletionItem(mDatePickerDialog, selectedDate)

                    Spacer(modifier = Modifier.weight(1f))

                    Button(modifier = Modifier.fillMaxWidth(), onClick = { loading = !loading }) {
                        Text(text = stringResource(id = R.string.label_task_save))
                    }
                }

                if (loading) {
                    CircularProgressIndicator(
                        Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
fun TitleItem(titleText: MutableState<String>) {
    Text(
        text = stringResource(R.string.label_task_title),
        style = TextTitleStyle
    )

    val horizontalScrollState = rememberScrollState(0)
    BasicTextField(
        value = titleText.value,
        singleLine = true,
        maxLines = 1,
        onValueChange = { titleText.value = it },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .horizontalScroll(horizontalScrollState),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Text
        ),
        textStyle = TextStyle.Default.copy(fontSize = 16.sp)
    )
}

@Composable
fun DescriptionItem() {
    var descriptionText by remember { mutableStateOf("") }
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
            .padding(top = 8.dp)
            .verticalScroll(verticalScrollState),
        textStyle = TextStyle.Default.copy(fontSize = 16.sp)
    )
}

@Composable
fun DateOfCompletionItem(mDatePickerDialog: Dialog, date: MutableState<String>) {
    Text(
        text = stringResource(R.string.label_task_doc),
        style = TextTitleStyle
    )
    ClickableText(
        text = AnnotatedString(date.value),
        onClick = { mDatePickerDialog.show() },
        style = TextStyle.Default.copy(color = Color.Gray)
    )
}

@Preview
@Composable
fun showPreview(){
    SaveTaskScreen()
}