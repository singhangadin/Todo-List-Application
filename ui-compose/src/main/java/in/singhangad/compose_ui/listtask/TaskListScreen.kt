package `in`.singhangad.compose_ui.listtask

import `in`.singhangad.compose_ui.R
import `in`.singhangad.ui_common.R as CR
import `in`.singhangad.ui_common.listtask.entity.ItemType
import `in`.singhangad.ui_common.listtask.entity.TaskListItem
import `in`.singhangad.ui_common.listtask.viewmodel.TaskListViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Card
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension

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
        viewModel?.loadData(false)
        val data = viewModel?.taskList?.observeAsState()
        TaskList(data = data?.value?: mutableListOf())
    }

    }
}

@Composable
fun TaskList(data: List<TaskListItem>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2)
    ) {
        items(items = data, span = { item ->
            when (item.itemType) {
                ItemType.HEADER -> GridItemSpan(2)
                ItemType.TASK_ITEM -> GridItemSpan(1)
            }
        }) { item ->
            when (item.itemType) {
                ItemType.HEADER -> TaskHeaderItem(item)
                ItemType.TASK_ITEM -> TaskItem(item)
            }
        }
    }
}

@Composable
fun TaskItem(taskListItem: TaskListItem) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(dimensionResource(id = CR.dimen.margin_tiny))) {
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
                }
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
                maxLines = 4
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
            .padding(dimensionResource(id = CR.dimen.margin_small))
    )
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
    ))
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

//@Preview(name = "Main Screen")
//@Composable
//fun showPreview(){
//    TaskListScreen(null) { taskId ->
//
//    }
//}