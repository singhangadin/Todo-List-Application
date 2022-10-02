package `in`.singhangad.compose_ui.listtask.fragment

import `in`.singhangad.compose_ui.listtask.TaskListScreen
import `in`.singhangad.ui_common.listtask.viewmodel.TaskListViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.ui.setDisplayHomeAsUpEnabled
import org.koin.androidx.viewmodel.ext.android.viewModel

class TaskListFragment: Fragment() {
    private val viewModel by viewModel<TaskListViewModel>()

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setDisplayHomeAsUpEnabled(false)
    }

    private fun navigateToSaveTaskScreen(taskId: Long?) {
        val action = TaskListFragmentDirections.actionTaskListFragmentToSaveTaskFragment(taskId?:-1)
        findNavController().navigate(action)
    }
}