package ldev.myNotifier.data.room

import ldev.myNotifier.domain.repositories.CustomLogRepository
import java.util.Date
import javax.inject.Inject

class CustomLogLocalRepository @Inject constructor(
    private val dbLogDao: DbLogDao
) : CustomLogRepository {
    override suspend fun addLog(tag: String, text: String) {
        dbLogDao.addLog(
            DbLog(
                id = 0L,
                tag = tag,
                text = text,
                dateGmt = Date().toStringGmt()
            )
        )
    }
}