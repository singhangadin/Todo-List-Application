package com.example.ui.adapter

import com.example.ui.listtask.adapter.TaskListAdapter
import `in`.singhangad.ui_common.listtask.entity.TaskListItem
import android.graphics.Paint
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("taskListData")
fun bindTaskListAdapter(recyclerView: RecyclerView, data: List<TaskListItem>?) {
    val adapter = recyclerView.adapter as TaskListAdapter
    adapter.submitList(data)
}

@BindingAdapter("formattedDateText")
fun bindFormattedDateText(endDateInput: TextView, calendar: Date?) {
    calendar?.let {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        endDateInput.text = dateFormat.format(it.time)
    }
}

@BindingAdapter("strike")
fun bindTextView(view: TextView, strike: Boolean) {
    view.paintFlags = if (strike) {
        view.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG
    } else {
        0 or Paint.ANTI_ALIAS_FLAG
    }
}