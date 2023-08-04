package ldev.myNotifier.utils

import ldev.myNotifier.domain.entities.Time
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

fun findNextDateByDayOfWeek(dayOfWeek: DayOfWeek, time: Time): Instant {
    val currentInstant = Instant.now()
    val zoneId = ZoneId.systemDefault()
    val currentOffsetDateTime = currentInstant.atZone(zoneId).toOffsetDateTime()

    val localDate = LocalDate.now()
    var daysUntilNextDayOfWeek = dayOfWeek.value - localDate.dayOfWeek.value
    if (daysUntilNextDayOfWeek < 0) {
        daysUntilNextDayOfWeek += 7
    }

    val nextDate = currentOffsetDateTime.plusDays(daysUntilNextDayOfWeek.toLong())
    return nextDate.withHour(time.hour).withMinute(time.minute).withSecond(0).toInstant()
}