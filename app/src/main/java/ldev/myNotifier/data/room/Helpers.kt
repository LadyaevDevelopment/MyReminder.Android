package ldev.myNotifier.data.room

import android.annotation.SuppressLint
import ldev.myNotifier.domain.entities.NotificationType
import ldev.myNotifier.domain.entities.PeriodicNotificationRule
import ldev.myNotifier.domain.entities.OneTimeNotification
import ldev.myNotifier.domain.entities.PeriodicNotification
import ldev.myNotifier.domain.entities.TodayNotification
import ldev.myNotifier.domain.entities.TodayNotificationStatus
import ldev.myNotifier.utils.findNextDateByDayOfWeek
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date
import java.util.TimeZone

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
        date = findNextDateByDayOfWeek(rule.dayOfWeek, rule.time, false),
        type = NotificationType.Periodic,
        // TODO: fix
        status = TodayNotificationStatus.Completed
    )
}

fun LocalDateTime.toDate(): Date {
    return Date.from(atZone(ZoneId.systemDefault()).toInstant())
}

@SuppressLint("SimpleDateFormat")
fun Date.toStringGmt(): String {
    val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm")
    sdf.timeZone = TimeZone.getTimeZone("GMT")
    return sdf.format(this)
}