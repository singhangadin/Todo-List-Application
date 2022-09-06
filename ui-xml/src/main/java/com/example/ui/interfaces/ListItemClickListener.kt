package com.example.ui.interfaces

import android.view.View
import com.example.ui.listtask.entity.TaskListItem

interface ListItemClickListener {
    fun onItemClicked(task: TaskListItem)
    fun onActionMore(task: TaskListItem, view: View)
}