package ldev.myNotifier.core

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ldev.myNotifier.domain.repositories.CustomLogRepository
import javax.inject.Inject

class CustomLogBusinessLogic @Inject constructor(
    private val customLogRepository: CustomLogRepository,
    private val logcatProxy: LogcatProxy
) {
    suspend fun logDebug(tag: String, text: String) {
        logcatProxy.logDebug(tag, text)
        withContext(Dispatchers.IO) {
            customLogRepository.addLog(tag, text)
        }
    }
}