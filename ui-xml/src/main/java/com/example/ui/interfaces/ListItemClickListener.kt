package com.example.ui.interfaces

import android.view.View
import `in`.singhangad.ui_common.listtask.entity.TaskListItem

interface ListItemClickListener {
    fun onItemClicked(task: TaskListItem)
    fun onActionMore(task: TaskListItem, view: View)
}