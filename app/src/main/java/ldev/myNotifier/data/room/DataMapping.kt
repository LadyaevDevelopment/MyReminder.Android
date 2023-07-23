package ldev.myNotifier.data.room

import ldev.myNotifier.domain.entities.NotificationRule
import java.util.Date

fun OneTimeNotification.toDomainEntity() : ldev.myNotifier.domain.entities.OneTimeNotification {
    return ldev.myNotifier.domain.entities.OneTimeNotification(
        id = id,
        title = title,
        text = text,
        date = date
    )
}

fun ldev.myNotifier.domain.entities.OneTimeNotification.toRoomEntity() : OneTimeNotification {
    return OneTimeNotification(
        id = id,
        title = title,
        text = text,
        date = date,
        oldDate = Date(),
        isPostponed = true
    )
}

fun PeriodicNotificationRule.toDomainEntity() : ldev.myNotifier.domain.entities.NotificationRule {
    return ldev.myNotifier.domain.entities.NotificationRule(
        id = id,
        dayOfWeek = dayOfWeek,
        time = time
    )
}

fun ldev.myNotifier.domain.entities.NotificationRule.toRoomEntity(
    notificationId: Long,
) : PeriodicNotificationRule {
    return PeriodicNotificationRule(
        id = id,
        dayOfWeek = dayOfWeek,
        time = time,
        notificationId = notificationId,
        postponedTime = null
    )
}

fun PeriodicNotification.toDomainEntity(
    rules: List<NotificationRule>
) : ldev.myNotifier.domain.entities.PeriodicNotification {
    return ldev.myNotifier.domain.entities.PeriodicNotification(
        id = id,
        title = title,
        text = text,
        rules = rules
    )
}

fun ldev.myNotifier.domain.entities.PeriodicNotification.toRoomEntity() : PeriodicNotification {
    return PeriodicNotification(
        id = id,
        title = title,
        text = text,
    )
}