package `in`.singhangad.compose_ui.listtask.fragment

import `in`.singhangad.compose_ui.listtask.TaskListScreen
import `in`.singhangad.ui_common.listtask.viewmodel.TaskListViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskListFragment: Fragment() {
    private val viewModel by viewModels<TaskListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                TaskListScreen(viewModel = viewModel) { taskId ->
                    navigateToSaveTaskScreen(taskId)
                }
            }
        }
    }

    private fun navigateToSaveTaskScreen(taskId: String?) {
        val action = TaskListFragmentDirections.actionTaskListFragmentToSaveTaskFragment(taskId)
        findNavController().navigate(action)
    }
}