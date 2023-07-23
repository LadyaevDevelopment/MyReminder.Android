package ldev.myNotifier.debugSettings

import ldev.myNotifier.domain.entities.NotificationRule
import ldev.myNotifier.domain.entities.PeriodicNotification
import ldev.myNotifier.domain.entities.Time
import java.time.DayOfWeek

class DebugDataProvider : DataProvider {

    @Suppress("UNCHECKED_CAST")
    override fun <T> provide(clazz: Class<T>): T? {
        if (clazz == PeriodicNotification::class.java) {
            return PeriodicNotification(
                id = 1,
                title = "My first notification",
                text = "Stuff...",
                rules = listOf(
                    NotificationRule(id = 0, dayOfWeek = DayOfWeek.MONDAY, time = Time(hour = 12, minute = 56)),
                    NotificationRule(id = 0, dayOfWeek = DayOfWeek.MONDAY, time = Time(hour = 21, minute = 56)),
                    NotificationRule(id = 0, dayOfWeek = DayOfWeek.WEDNESDAY, time = Time(hour = 20, minute = 56)),
                    NotificationRule(id = 0, dayOfWeek = DayOfWeek.WEDNESDAY, time = Time(hour = 18, minute = 56)),
                    NotificationRule(id = 0, dayOfWeek = DayOfWeek.WEDNESDAY, time = Time(hour = 12, minute = 56)),
                    NotificationRule(id = 0, dayOfWeek = DayOfWeek.FRIDAY, time = Time(hour = 12, minute = 56)),
                    NotificationRule(id = 0, dayOfWeek = DayOfWeek.SUNDAY, time = Time(hour = 4, minute = 56)),
                    NotificationRule(id = 0, dayOfWeek = DayOfWeek.SUNDAY, time = Time(hour = 12, minute = 56)),
                )
            ) as T
        }
        return null
    }

}