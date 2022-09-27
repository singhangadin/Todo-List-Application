package `in`.singhangad.ui_common.di

import `in`.singhangad.shared_common.DefaultDispatcher
import `in`.singhangad.ui_common.listtask.viewmodel.TaskListViewModel
import `in`.singhangad.ui_common.savetask.viewmodel.SaveTaskViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { TaskListViewModel(get(), get(), get(), get(), get(DefaultDispatcher())) }
    viewModel { SaveTaskViewModel(get(), get(), get(DefaultDispatcher())) }
}