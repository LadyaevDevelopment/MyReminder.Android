package ldev.myNotifier.data.room

fun OneTimeNotification.toDomainEntity() : ldev.myNotifier.domain.entities.OneTimeNotification {
    return ldev.myNotifier.domain.entities.OneTimeNotification(
        id = id.toInt(),
        title = title,
        text = text,
        date = date
    )
}

fun ldev.myNotifier.domain.entities.OneTimeNotification.toRoomEntity() : OneTimeNotification {
    return OneTimeNotification(
        id = id.toLong(),
        title = title,
        text = text,
        date = date,
    )
}

fun PeriodicNotificationRule.toDomainEntity() : ldev.myNotifier.domain.entities.PeriodicNotificationRule {
    return ldev.myNotifier.domain.entities.PeriodicNotificationRule(
        id = id.toInt(),
        dayOfWeek = dayOfWeek,
        time = time,
    )
}

fun ldev.myNotifier.domain.entities.PeriodicNotificationRule.toRoomEntity(
    notificationId: Int,
) : PeriodicNotificationRule {
    return PeriodicNotificationRule(
        id = id.toLong(),
        dayOfWeek = dayOfWeek,
        time = time,
        notificationId = notificationId,
    )
}

fun PeriodicNotification.toDomainEntity() : ldev.myNotifier.domain.entities.PeriodicNotification {
    return ldev.myNotifier.domain.entities.PeriodicNotification(
        id = id.toInt(),
        title = title,
        text = text,
    )
}

fun ldev.myNotifier.domain.entities.PeriodicNotification.toRoomEntity() : PeriodicNotification {
    return PeriodicNotification(
        id = id.toLong(),
        title = title,
        text = text,
    )
}