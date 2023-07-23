package ldev.myNotifier.data.room

import androidx.room.TypeConverter
import ldev.myNotifier.domain.entities.Time
import java.time.DayOfWeek
import java.util.Date

class DateConverter {
    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return timestamp?.let { Date(it) }
    }

    @TypeConverter
    fun toPrimitiveValue(date: Date?): Long? {
        return date?.time
    }
}

class DayOfWeekConverter {
    @TypeConverter
    fun toPrimitiveValue(value: DayOfWeek): String {
        return value.name
    }

    @TypeConverter
    fun toEnumValue(value: String): DayOfWeek {
        return DayOfWeek.valueOf(value)
    }
}

class TimeConverter {

    @TypeConverter
    fun fromTime(time: Time): String {
        return "${time.hour}:${time.minute}"
    }

    @TypeConverter
    fun toTime(timeString: String): Time {
        val parts = timeString.split(":")
        val hour = parts[0].toInt()
        val minute = parts[1].toInt()
        return Time(hour, minute)
    }

}