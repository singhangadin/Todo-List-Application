package `in`.singhangad.ui_common.listtask.adapter

import com.example.ui.databinding.ListItemHeadingBinding
import com.example.ui.databinding.ListItemTaskBinding
import com.example.ui.interfaces.ListItemClickListener
import `in`.singhangad.ui_common.listtask.entity.ItemType
import `in`.singhangad.ui_common.listtask.entity.TaskListItem
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager


class TaskListAdapter(private val itemClickListener: ListItemClickListener): ListAdapter<TaskListItem, RecyclerView.ViewHolder>(
    TaskListItemDiffCallback()
) {

    private val ITEM_TYPE_HEADER = 0
    private val ITEM_TYPE_TASK = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            ITEM_TYPE_HEADER -> {
                val binding = ListItemHeadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                TitleViewHolder(binding)
            }
            else -> {
                val binding = ListItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                TaskViewHolder(binding, itemClickListener)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is TaskViewHolder -> holder.setData(getItem(position))
            is TitleViewHolder -> holder.setData(getItem(position))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position).itemType) {
            ItemType.HEADER -> ITEM_TYPE_HEADER
            ItemType.TASK_ITEM -> ITEM_TYPE_TASK
        }
    }

    class TitleViewHolder(private val binding: ListItemHeadingBinding): RecyclerView.ViewHolder(binding.root) {
        fun setData(task: TaskListItem) {
            binding.task = task
            if (binding.root.layoutParams is StaggeredGridLayoutManager.LayoutParams) {
                (binding.root.layoutParams as StaggeredGridLayoutManager.LayoutParams).isFullSpan =
                    true
            }
        }
    }

    class TaskViewHolder(private val binding: ListItemTaskBinding, private val itemClickListener: ListItemClickListener): RecyclerView.ViewHolder(binding.root) {
        fun setData(task: TaskListItem) {
            binding.task = task
            binding.itemClickListener = itemClickListener
        }
    }

    class TaskListItemDiffCallback: DiffUtil.ItemCallback<TaskListItem>() {
        override fun areItemsTheSame(oldItem: TaskListItem, newItem: TaskListItem): Boolean
        = oldItem.itemId == newItem.itemId

        override fun areContentsTheSame(oldItem: TaskListItem, newItem: TaskListItem): Boolean = (
            oldItem.itemId == newItem.itemId &&
            oldItem.itemTitle == newItem.itemTitle &&
            oldItem.itemDescription == newItem.itemDescription &&
            oldItem.itemPinned == newItem.itemPinned
        )
    }
}