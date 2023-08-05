package ldev.myNotifier.utils

import ldev.myNotifier.data.room.toDate
import ldev.myNotifier.domain.entities.Time
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.util.Date

fun findNextDateByDayOfWeek(dayOfWeek: DayOfWeek, time: Time): Date {
    val localDateTime = LocalDateTime.now()
    var daysUntilNextDayOfWeek = dayOfWeek.value - localDateTime.dayOfWeek.value
    if (daysUntilNextDayOfWeek < 0) {
        daysUntilNextDayOfWeek += 7
    }
    val nextDate = localDateTime.plusDays(daysUntilNextDayOfWeek.toLong())
    return nextDate.withHour(time.hour).withMinute(time.minute).withSecond(0).withNano(0).toDate()
}