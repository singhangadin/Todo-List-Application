package com.example.todo_list_application

import `in`.singhangad.shared_data.di.sharedDataModule
import `in`.singhangad.ui_common.di.viewModelModule
import android.app.Application
import `in`.singhangad.shared_domain.di.domainModule
import com.example.todo_list_application.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class TodoApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@TodoApplication)
            modules(appModule, sharedDataModule, domainModule, viewModelModule)
        }
    }
}