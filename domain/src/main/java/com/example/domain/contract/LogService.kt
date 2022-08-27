package com.example.domain.contract

interface LogService {

    fun logException(tag: String, throwable: Throwable)

    fun logDebug(tag: String, message: String)

    fun logError(tag: String, message: String)
}