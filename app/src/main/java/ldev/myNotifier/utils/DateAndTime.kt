package ldev.myNotifier.utils

import ldev.myNotifier.data.room.toDate
import ldev.myNotifier.domain.entities.Time
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.util.Calendar
import java.util.Date

fun findNextDateByDayOfWeek(dayOfWeek: DayOfWeek, time: Time, necessarilyForNextWeek: Boolean): Date {
    val localDateTime = LocalDateTime.now()
    var daysUntilNextDayOfWeek = dayOfWeek.value - localDateTime.dayOfWeek.value
    if (daysUntilNextDayOfWeek < 0) {
        daysUntilNextDayOfWeek += 7
    }
    if (daysUntilNextDayOfWeek == 0) {
        if (localDateTime.hour > time.hour
            || localDateTime.hour == time.hour && localDateTime.minute > time.minute
            || necessarilyForNextWeek
        ) {
            daysUntilNextDayOfWeek += 7
        }
    }
    val nextDate = localDateTime.plusDays(daysUntilNextDayOfWeek.toLong())
    return nextDate.withHour(time.hour).withMinute(time.minute).withSecond(0).withNano(0).toDate()
}

fun Date.getLocalTime(): Time {
    val calendar = Calendar.getInstance()
    calendar.time = this

    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    return Time(hour, minute)
}