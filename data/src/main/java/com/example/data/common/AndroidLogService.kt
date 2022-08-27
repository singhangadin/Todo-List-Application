package com.example.data.common

import com.example.domain.contract.LogService
import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidLogService @Inject constructor(): LogService {
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