package ldev.myNotifier.debugSettings

import ldev.myNotifier.domain.entities.OneTimeNotification
import ldev.myNotifier.domain.entities.PeriodicNotificationRule
import ldev.myNotifier.domain.entities.PeriodicNotification
import ldev.myNotifier.domain.entities.PeriodicNotificationWithRules
import ldev.myNotifier.domain.entities.Time
import java.time.DayOfWeek
import java.util.Date

class DebugDataProvider : DataProvider {

    @Suppress("UNCHECKED_CAST")
    override fun <T> provide(clazz: Class<T>): T? {
        if (clazz == PeriodicNotificationWithRules::class.java) {
            return PeriodicNotificationWithRules(
                notification = PeriodicNotification(
                    id = 0,
                    title = "My first notification",
                    text = "Stuff...",
                ),
                rules = listOf(
                    PeriodicNotificationRule(id = 0, dayOfWeek = DayOfWeek.MONDAY, time = Time(hour = 12, minute = 56)),
                    PeriodicNotificationRule(id = 0, dayOfWeek = DayOfWeek.MONDAY, time = Time(hour = 21, minute = 56)),
                    PeriodicNotificationRule(id = 0, dayOfWeek = DayOfWeek.WEDNESDAY, time = Time(hour = 20, minute = 56)),
                    PeriodicNotificationRule(id = 0, dayOfWeek = DayOfWeek.WEDNESDAY, time = Time(hour = 18, minute = 56)),
                    PeriodicNotificationRule(id = 0, dayOfWeek = DayOfWeek.WEDNESDAY, time = Time(hour = 12, minute = 56)),
                    PeriodicNotificationRule(id = 0, dayOfWeek = DayOfWeek.FRIDAY, time = Time(hour = 12, minute = 56)),
                    PeriodicNotificationRule(id = 0, dayOfWeek = DayOfWeek.SUNDAY, time = Time(hour = 4, minute = 56)),
                    PeriodicNotificationRule(id = 0, dayOfWeek = DayOfWeek.SUNDAY, time = Time(hour = 12, minute = 56)),
                )
            ) as T
        }
        if (clazz == OneTimeNotification::class.java) {
            return OneTimeNotification(
                id = 0,
                title = "My first notification",
                text = "Stuff...",
                date = Date(),
                isActive = true
            ) as T
        }
        return null
    }

}