package ldev.myNotifier.data.room

import androidx.room.TypeConverter
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