package `in`.singhangad.compose_ui.listtask

import `in`.singhangad.compose_ui.values.TextHeadingStyle
import `in`.singhangad.compose_ui.values.TextSubTitleStyle
import `in`.singhangad.compose_ui.values.TextTitleStyle
import `in`.singhangad.ui_common.R as CR
import `in`.singhangad.ui_common.listtask.entity.ItemType
import `in`.singhangad.ui_common.listtask.entity.TaskListItem
import `in`.singhangad.ui_common.listtask.uistate.TaskListUIState
import `in`.singhangad.ui_common.listtask.viewmodel.TaskListViewModel
import `in`.singhangad.ui_common.savetask.uistate.SaveTaskUIState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun TaskListScreen(viewModel: TaskListViewModel, navigateSave: (String?) -> Unit) {
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val uiState = viewModel.uiState.collectAsState(initial = TaskListUIState.HideLoader)
    var isRefreshing by remember { mutableStateOf(false) }

    MaterialTheme {
        Scaffold(
            scaffoldState = scaffoldState,
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { viewModel.createNewTask() }) {
                    Icon(Icons.Filled.Add,"")
                }
            },
            modifier
                = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Box(
                modifier =
                Modifier
                    .padding(it)
                    .fillMaxWidth()
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                val data = viewModel.taskList.observeAsState()
                if (data.value.isNullOrEmpty()) {
                    EmptyView()
                } else {
                    TaskList(data = data.value ?: mutableListOf()) { taskId ->
                        viewModel.updateTask(taskId)
                    }
                }

                when(uiState.value) {
                    TaskListUIState.HideEmptyView -> { }
                    TaskListUIState.HideLoader -> { isRefreshing = false }
                    TaskListUIState.ShowEmptyView -> EmptyView()
                    TaskListUIState.ShowLoader ->
                        CircularProgressIndicator(Modifier.align(Alignment.Center))

                    is TaskListUIState.ShowMessage -> {
                        val message = stringResource(id = (uiState.value as SaveTaskUIState.ShowMessage).message)
                        coroutineScope.launch {
                            scaffoldState.snackbarHostState.showSnackbar(
                                message = message
                            )
                        }
                    }
                    is TaskListUIState.ShowSaveTaskScreen -> {
                        navigateSave.invoke((uiState.value as TaskListUIState.ShowSaveTaskScreen).taskId)
                    }
                }
            }
        }
    }

    viewModel.loadData(true)
}

@Composable
fun EmptyView() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = CR.drawable.empty_paper), 
            contentDescription = "",
            modifier = Modifier.height(128.dp)
        )
        Text(
            text = stringResource(
                id = CR.string.title_empty_task
            ),
            modifier = Modifier.padding(
                top = dimensionResource(
                    id = CR.dimen.margin_large
                ),
                bottom = dimensionResource(
                    id = CR.dimen.margin_regular
                )
            ),
            style = TextHeadingStyle
        )
        Text(
            text = stringResource(
                id = CR.string.description_empty_task
            ),
            style = TextSubTitleStyle
        )
    }
}

@Composable
fun TaskList(data: List<TaskListItem>, onItemClick: (String) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier =
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        items(
            items = data,
            span = { item ->
                when (item.itemType) {
                    ItemType.HEADER -> GridItemSpan(2)
                    ItemType.TASK_ITEM -> GridItemSpan(1)
                }
            }, key = { task -> task.itemId }) { item ->
            when (item.itemType) {
                ItemType.HEADER -> TaskHeaderItem(item)
                ItemType.TASK_ITEM -> TaskItem(item) {
                    onItemClick(it)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TaskItem(taskListItem: TaskListItem, onClick: (String) -> Unit) {
    Card(elevation = 3.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = CR.dimen.margin_small)),
        onClick = { onClick(taskListItem.itemId) }
    ) {
        ConstraintLayout(
            modifier = Modifier.padding(
                dimensionResource(id = CR.dimen.margin_regular)
            )) {
            val (labelTitle, moreOptions, labelDescription) = createRefs()
            
            Text(
                text = taskListItem.itemTitle,
                modifier = Modifier.constrainAs(labelTitle) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(moreOptions.start)
                    width = Dimension.fillToConstraints
                },
                style = TextTitleStyle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Image(
                painter = painterResource(id = CR.drawable.ic_action_more),
                contentDescription = "" ,
                modifier = Modifier.constrainAs(moreOptions) {
                    top.linkTo(parent.top)
                    start.linkTo(labelTitle.end)
                    end.linkTo(parent.end)
                }
            )

            Text(
                text = taskListItem.itemDescription?:"",
                modifier = Modifier
                    .constrainAs(labelDescription) {
                        top.linkTo(labelTitle.bottom)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    }
                    .padding(top = dimensionResource(id = CR.dimen.margin_regular)),
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun TaskHeaderItem(taskListItem: TaskListItem) {
    Text(
        text = taskListItem.itemTitle,
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = CR.dimen.margin_small)),
        style = TextSubTitleStyle,
        maxLines = 1
    )
}

@Preview
@Composable
fun EmptyViewPreview() {
    EmptyView()
}

@Preview
@Composable
fun TaskListItemPreview() {
    TaskItem(TaskListItem(
        "itemId",
        ItemType.TASK_ITEM,
        "Title",
        "Description",
        false
    ), { })
}

@Preview
@Composable
fun TaskHeaderItemPreview() {
    TaskHeaderItem(TaskListItem(
        "itemId",
        ItemType.TASK_ITEM,
        "Title",
        "Description",
        false
    ))
}