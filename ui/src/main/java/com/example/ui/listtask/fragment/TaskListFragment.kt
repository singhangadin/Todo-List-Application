package com.example.ui.listtask.fragment

import com.example.ui.R
import com.example.ui.databinding.FragmentTaskListBinding
import com.example.ui.interfaces.ListItemClickListener
import com.example.ui.listtask.adapter.TaskListAdapter
import com.example.ui.listtask.entity.TaskListItem
import com.example.ui.listtask.uistate.TaskListUIState
import com.example.ui.listtask.viewmodel.TaskListViewModel
import com.example.ui.setDisplayHomeAsUpEnabled
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TaskListFragment : Fragment(), ListItemClickListener {

    val viewModel by viewModels<TaskListViewModel>()

    private lateinit var binding: FragmentTaskListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskListBinding.inflate(LayoutInflater.from(context), container, false)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.taskListView.adapter = TaskListAdapter(this)
        binding.lifecycleOwner = viewLifecycleOwner

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect{
                    when(it) {
                        TaskListUIState.HideEmptyView -> hideEmptyView()
                        TaskListUIState.HideLoader -> hideLoader()
                        TaskListUIState.ShowEmptyView -> showEmptyView()
                        TaskListUIState.ShowLoader -> showLoader()
                        is TaskListUIState.ShowMessage -> showMessage(it.id)
                    }
                }
            }
        }

        setDisplayHomeAsUpEnabled(false)
    }

    private fun showLoader() {
        binding.progressLayout.visibility = View.VISIBLE
    }

    private fun hideLoader() {
        binding.refreshLayout.isRefreshing = false
        binding.progressLayout.visibility = View.GONE
    }

    private fun showEmptyView() {
        binding.emptyView.visibility = View.VISIBLE
    }

    private fun hideEmptyView() {
        binding.emptyView.visibility = View.GONE
    }

    private fun showMessage(message: Int) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    private fun navigateToSaveTaskScreen(taskId: String?) {
        val action = TaskListFragmentDirections.actionTaskListFragmentToSaveTaskFragment(taskId)
        findNavController().navigate(action)
    }

    override fun onActionMore(task: TaskListItem, view: View) {
        displayPopupWindow(view, task)
    }

    override fun onItemClicked(task: TaskListItem) {
        viewModel.updateTask(taskId = task.itemId)
    }

    private fun displayPopupWindow(view: View, task: TaskListItem) {
        val popupMenu = PopupMenu(requireContext(), view).apply {
            this.inflate(R.menu.task_overflow_menu)
            if (task.itemPinned) {
                this.menu.findItem(R.id.item_overflow_pin).title = getString(R.string.option_unpin_item)
            } else {
                this.menu.findItem(R.id.item_overflow_pin).title = getString(R.string.option_pin_item)
            }
        }.also {
            it.show()
        }

        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener {
            return@OnMenuItemClickListener when(it.itemId) {
                R.id.item_overflow_delete -> {
                    viewModel.deleteTask(taskId = task.itemId)
                    return@OnMenuItemClickListener true
                }
                R.id.item_overflow_pin -> {
                    if (task.itemPinned) {
                        viewModel.unPinItem(taskId = task.itemId)
                    } else {
                        viewModel.pinItem(taskId = task.itemId)
                    }
                    return@OnMenuItemClickListener true
                }
                else -> false
            }
        })
    }
}