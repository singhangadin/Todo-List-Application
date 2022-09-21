package `in`.singhangad.compose_ui.savetask

import `in`.singhangad.ui_common.R as CR
import `in`.singhangad.compose_ui.utils.DatePickerDialogFactory
import `in`.singhangad.compose_ui.values.TextTitleStyle
import `in`.singhangad.ui_common.savetask.uistate.SaveTaskUIState
import `in`.singhangad.ui_common.savetask.viewmodel.SaveTaskViewModel
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun SaveTaskScreen(viewModel: SaveTaskViewModel, taskId: String?, onBackPress: () -> Unit) {
    val mDatePickerDialog = DatePickerDialogFactory.create(LocalContext.current) { year, month, day ->
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH,month)
        calendar.set(Calendar.DAY_OF_MONTH,day)

        viewModel.endDate.value = calendar.time
    }

    MaterialTheme {
        val scaffoldState: ScaffoldState = rememberScaffoldState()
        val uiState = viewModel.uiState.onSubscription {
            viewModel.init(taskId)
        }.collectAsState(initial = SaveTaskUIState.HideLoader)

        Scaffold(
            scaffoldState = scaffoldState
        ) {
            Box(modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(it)
            ) {
                TaskForm(viewModel = viewModel)

                when(uiState.value) {
                    SaveTaskUIState.HideLoader -> { }

                    SaveTaskUIState.ShowDatePicker ->
                        mDatePickerDialog.show()

                    SaveTaskUIState.ShowLoader ->
                        CircularProgressIndicator(Modifier.align(Alignment.Center))

                    is SaveTaskUIState.ShowMessage -> {
                        val message = stringResource(id = (uiState.value as SaveTaskUIState.ShowMessage).message)
                        LaunchedEffect(scaffoldState.snackbarHostState) {
                            scaffoldState.snackbarHostState.showSnackbar(
                                message = message
                            )
                        }
                    }
                    SaveTaskUIState.Success -> { onBackPress() }
                }
            }
        }
    }
}

@Composable
fun TaskForm(viewModel: SaveTaskViewModel) {
    val selectedDate = viewModel.endDate.observeAsState()

    Column(
        modifier = Modifier
            .padding(
                dimensionResource(id = CR.dimen.margin_regular)
            )
            .fillMaxHeight()
            .fillMaxWidth()
    ) {
        val titleText = viewModel.taskTitle.observeAsState()
        TitleItem(viewModel, titleText)
        Spacer(modifier = Modifier.height(dimensionResource(id = CR.dimen.margin_small)))

        val descriptionText = viewModel.taskDescription.observeAsState()
        DescriptionItem(viewModel, descriptionText)
        Spacer(modifier = Modifier.height(dimensionResource(id = CR.dimen.margin_small)))

        DateOfCompletionItem(viewModel, selectedDate)
        Spacer(modifier = Modifier.weight(1f))

        Button(modifier = Modifier.fillMaxWidth(), onClick = { viewModel.saveTask() }) {
            Text(text = stringResource(id = CR.string.label_task_save))
        }
    }
}

@Composable
fun TitleItem(viewModel: SaveTaskViewModel?, titleText: State<String?>) {
    Text(
        text = stringResource(CR.string.label_task_title),
        style = TextTitleStyle,
        modifier = Modifier.fillMaxWidth()
    )

    val horizontalScrollState = rememberScrollState(0)
    BasicTextField(
        value = titleText.value?:"",
        singleLine = true,
        maxLines = 1,
        onValueChange = { viewModel?.taskTitle?.value = it },
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
fun DescriptionItem(viewModel: SaveTaskViewModel?, descriptionText: State<String?>) {
    Text(
        text = stringResource(CR.string.label_task_description),
        style = TextTitleStyle,
        modifier = Modifier.fillMaxWidth()
    )
    val verticalScrollState = rememberScrollState(0)
    BasicTextField(
        value = descriptionText.value?:"",
        onValueChange = {
            viewModel?.taskDescription?.value = it
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
fun DateOfCompletionItem(viewModel: SaveTaskViewModel?, date: State<Date?>) {
    Text(
        text = stringResource(CR.string.label_task_doc),
        style = TextTitleStyle,
        modifier = Modifier.fillMaxWidth()
    )
    val label = if (date.value == null) {
        stringResource(id = CR.string.label_hint_date)
    } else {
        val calendar = Calendar.getInstance()
        calendar.time = date.value!!
        "${calendar.get(Calendar.YEAR)}/" +
        "${calendar.get(Calendar.MONTH)}/" +
        "${calendar.get(Calendar.DAY_OF_MONTH)}"
    }
    ClickableText(
        text = AnnotatedString(label),
        onClick = { viewModel?.showDatePicker() },
        style = TextStyle.Default.copy(color = Color.Gray),
        modifier = Modifier.fillMaxWidth()
    )
}