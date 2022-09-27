package `in`.singhangad.compose_ui.savetask.fragment

import `in`.singhangad.compose_ui.savetask.SaveTaskScreen
import `in`.singhangad.ui_common.savetask.viewmodel.SaveTaskViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.example.ui.setDisplayHomeAsUpEnabled
import org.koin.androidx.viewmodel.ext.android.viewModel

class SaveTaskFragment: Fragment() {

    private val viewModel by viewModel<SaveTaskViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val taskId = arguments?.run {
                    SaveTaskFragmentArgs.fromBundle(this).taskId
                }
                SaveTaskScreen(viewModel = viewModel, taskId) {
                    activity?.onBackPressed()
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setDisplayHomeAsUpEnabled(true)
    }
}