package `in`.singhangad.shared_data.service

import `in`.singhangad.shared_domain.contract.LogService
import io.github.aakira.napier.Napier

class CommonLogService : LogService {
    override fun logException(tag: String, throwable: Throwable) {
        Napier.e(throwable = throwable, tag = tag, message = "")
    }

    override fun logDebug(tag: String, message: String) {
        Napier.d(tag = tag, message = message)
    }

    override fun logError(tag: String, message: String) {
        Napier.e(tag = tag, message = message)
    }
}