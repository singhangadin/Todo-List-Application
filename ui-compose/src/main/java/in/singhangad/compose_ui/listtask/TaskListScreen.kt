package `in`.singhangad.compose_ui.listtask

import `in`.singhangad.compose_ui.values.TextHeadingStyle
import `in`.singhangad.compose_ui.values.TextSubTitleStyle
import `in`.singhangad.compose_ui.values.TextTitleStyle
import `in`.singhangad.ui_common.R as CR
import `in`.singhangad.ui_common.listtask.entity.ItemType
import `in`.singhangad.ui_common.listtask.entity.TaskListItem
import `in`.singhangad.ui_common.listtask.uistate.TaskListUIState
import `in`.singhangad.ui_common.listtask.viewmodel.TaskListViewModel
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import kotlinx.coroutines.flow.onSubscription

@Composable
fun TaskListScreen(viewModel: TaskListViewModel, navigateSave: (String?) -> Unit) {
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val uiState = viewModel.uiState.onSubscription {
        viewModel.loadData(true)
    }.collectAsState(initial = TaskListUIState.HideLoader)

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
                when(uiState.value) {
                    TaskListUIState.HideEmptyView -> { }
                    TaskListUIState.HideLoader -> {  }
                    TaskListUIState.ShowEmptyView -> EmptyView()
                    TaskListUIState.ShowLoader ->
                        CircularProgressIndicator(Modifier.align(Alignment.Center))

                    is TaskListUIState.ShowMessage -> {
                        val message = stringResource(id = (uiState.value as TaskListUIState.ShowMessage).message)
                        LaunchedEffect(scaffoldState.snackbarHostState) {
                            scaffoldState.snackbarHostState.showSnackbar(
                                message = message
                            )
                        }
                    }
                    is TaskListUIState.ShowSaveTaskScreen -> {
                        navigateSave.invoke((uiState.value as TaskListUIState.ShowSaveTaskScreen).taskId)
                    }
                }

                val data = viewModel.taskList.observeAsState()

                if (!data.value.isNullOrEmpty()) {
                    TaskList(data = data.value ?: mutableListOf(), { id, taskId ->
                        when(id) {
                            CR.string.option_delete ->
                                viewModel.deleteTask(taskId)

                            CR.string.option_pin_item ->
                                viewModel.pinItem(taskId)

                            CR.string.option_unpin_item ->
                                viewModel.unPinItem(taskId)
                        }
                    }) { taskId ->
                        viewModel.updateTask(taskId)
                    }
                } else {
                    EmptyView()
                }
            }
        }
    }
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
fun ActionMenu(expanded: MutableState<Boolean>, task: TaskListItem, menuItemClick: (Int, String) -> Unit){
    DropdownMenu(
        expanded = expanded.value,
        onDismissRequest = { expanded.value = false }
    ) {
        val id = if (task.itemPinned) {
            CR.string.option_unpin_item
        } else {
            CR.string.option_pin_item
        }
        DropdownMenuItem(onClick = {
            menuItemClick(id, task.itemId)
        }) {
            Text(stringResource(id =
                id
            ))
        }
        DropdownMenuItem(onClick = { menuItemClick(CR.string.option_delete, task.itemId) }) {
            Text(stringResource(id = CR.string.option_delete))
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskList(data: List<TaskListItem>, menuItemClick: (Int, String) -> Unit, onItemClick: (String) -> Unit) {

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
            Row(Modifier.animateItemPlacement()) {
                when (item.itemType) {
                    ItemType.HEADER -> TaskHeaderItem(item)
                    ItemType.TASK_ITEM ->
                        TaskItem(
                            item,
                            menuItemClick = menuItemClick
                        ) { onItemClick(it) }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TaskItem(taskListItem: TaskListItem, menuItemClick: (Int, String) -> Unit, onClick: (String) -> Unit) {
    val expanded = remember {
        mutableStateOf(false)
    }
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
            Box (
                modifier = Modifier
                    .constrainAs(moreOptions) {
                        top.linkTo(parent.top)
                        start.linkTo(labelTitle.end)
                        end.linkTo(parent.end)
                    }
            ){
                Image(
                    painter = painterResource(id = CR.drawable.ic_action_more),
                    contentDescription = "",
                    modifier = Modifier
                        .clickable(
                            enabled = true,
                            onClick = { expanded.value = true }
                        )
                )
                ActionMenu(
                    expanded = expanded,
                    task = taskListItem,
                    menuItemClick = { id, taskId ->
                        expanded.value = false
                        menuItemClick(id, taskId)
                    }
                )
            }

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
    ),{ _,_ -> }, { })
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