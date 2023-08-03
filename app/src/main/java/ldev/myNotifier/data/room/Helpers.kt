package ldev.myNotifier.data.room

import ldev.myNotifier.domain.entities.NotificationType
import ldev.myNotifier.domain.entities.PeriodicNotificationRule
import ldev.myNotifier.domain.entities.OneTimeNotification
import ldev.myNotifier.domain.entities.PeriodicNotification
import ldev.myNotifier.domain.entities.Time
import ldev.myNotifier.domain.entities.TodayNotification
import ldev.myNotifier.domain.entities.TodayNotificationStatus
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters
import java.util.Date

fun TodayNotification.Companion.oneTimeNotification(notification: OneTimeNotification) : TodayNotification {
    return TodayNotification(
        id = notification.id,
        title = notification.title,
        date = notification.date,
        type = NotificationType.OneTime,
        // TODO: fix
        status = TodayNotificationStatus.Completed
    )
}

fun TodayNotification.Companion.periodicNotification(
    notification: PeriodicNotification,
    rule: PeriodicNotificationRule
) : TodayNotification {

    return TodayNotification(
        id = notification.id,
        title = notification.title,
        date = findNextDateByDayOfWeek(rule.dayOfWeek, rule.time).toDate(),
        type = NotificationType.Periodic,
        // TODO: fix
        status = TodayNotificationStatus.Completed
    )
}

fun findNextDateByDayOfWeek(dayOfWeek: DayOfWeek, time: Time): LocalDateTime {
    val today = LocalDate.now()
    val currentDayOfWeek = today.dayOfWeek

    val nextDate = if (dayOfWeek == currentDayOfWeek) {
        today
    } else {
        today.with(TemporalAdjusters.next(dayOfWeek))
    }

    return nextDate.atTime(time.hour, time.minute)
}

fun Instant.toLocalDate(): LocalDate {
    return atZone(ZoneId.systemDefault()).toLocalDate()
}

fun Instant.toDate(): Date {
    return Date.from(this)
}

fun LocalDateTime.toDate(): Date {
    return Date.from(atZone(ZoneId.systemDefault()).toInstant())
}