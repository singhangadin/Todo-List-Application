package com.example.todo_list_application.service

import `in`.singhangad.shared_domain.contract.LogService
import android.util.Log

class AndroidLogService : LogService {
    override fun logException(tag: String, throwable: Throwable) {
        Log.e(tag, null, throwable)
    }

    override fun logDebug(tag: String, message: String) {
        Log.d(tag, message)
    }

    override fun logError(tag: String, message: String) {
        Log.e(tag, message)
    }
}