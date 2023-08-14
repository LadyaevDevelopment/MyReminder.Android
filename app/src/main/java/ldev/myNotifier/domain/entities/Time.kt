package ldev.myNotifier.domain.entities

data class Time(
    val hour: Int,
    val minute: Int
) : Comparable<Time> {

    override fun compareTo(other: Time): Int {
        val thisTotalMinutes = hour * 60 + minute
        val otherTotalMinutes = other.hour * 60 + other.minute
        return thisTotalMinutes.compareTo(otherTotalMinutes)
    }

    override fun toString(): String {
        return String.format("%02d:%02d", hour, minute)
    }
}