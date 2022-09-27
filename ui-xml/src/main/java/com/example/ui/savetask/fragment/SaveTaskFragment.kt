package com.example.ui.savetask.fragment

import com.example.ui.databinding.FragmentSaveTaskBinding
import `in`.singhangad.ui_common.savetask.uistate.SaveTaskUIState
import `in`.singhangad.ui_common.savetask.viewmodel.SaveTaskViewModel
import com.example.ui.setDisplayHomeAsUpEnabled
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.launch
import java.util.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class SaveTaskFragment: Fragment() {

    val viewModel by viewModel<SaveTaskViewModel>()

    private lateinit var binding: FragmentSaveTaskBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSaveTaskBinding.inflate(LayoutInflater.from(context), container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState
                    .onSubscription {
                        arguments?.let {
                            viewModel.init(SaveTaskFragmentArgs.fromBundle(it).taskId)
                        }
                    }
                    .collect { uiState ->
                    when (uiState) {
                        is SaveTaskUIState.HideLoader -> binding.progressLayout.visibility = View.GONE
                        is SaveTaskUIState.ShowLoader -> binding.progressLayout.visibility = View.VISIBLE

                        is SaveTaskUIState.ShowMessage ->
                            Snackbar.make(binding.root, uiState.message, Snackbar.LENGTH_LONG).show()

                        is SaveTaskUIState.Success -> activity?.onBackPressed()

                        is SaveTaskUIState.ShowDatePicker -> showDatePicker()
                    }
                }
            }
        }
        setDisplayHomeAsUpEnabled(true)
    }

    private val date =
        OnDateSetListener { _, year, month, day ->
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH,month)
            calendar.set(Calendar.DAY_OF_MONTH,day)

            viewModel.endDate.value = calendar.time
        }

    private fun showDatePicker() {
        val timeNow = Calendar.getInstance()

        DatePickerDialog(
            requireContext(),
            date,
            timeNow.get(Calendar.YEAR),
            timeNow.get(Calendar.MONTH),
            timeNow.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
}