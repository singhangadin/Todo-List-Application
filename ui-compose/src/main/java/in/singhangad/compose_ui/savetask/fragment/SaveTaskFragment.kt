package `in`.singhangad.compose_ui.savetask.fragment

import `in`.singhangad.compose_ui.savetask.SaveTaskScreen
import `in`.singhangad.ui_common.savetask.viewmodel.SaveTaskViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SaveTaskFragment: Fragment() {

    private val viewModel by viewModels<SaveTaskViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                SaveTaskScreen(viewModel = viewModel)
            }
        }
    }
}