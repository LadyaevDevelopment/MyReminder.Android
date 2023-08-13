package ldev.myNotifier.domain.repositories

interface CustomLogRepository {
    suspend fun addLog(tag: String, text: String)
}