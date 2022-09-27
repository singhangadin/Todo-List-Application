package com.example.data.repository

import com.example.domain.contract.LogService
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