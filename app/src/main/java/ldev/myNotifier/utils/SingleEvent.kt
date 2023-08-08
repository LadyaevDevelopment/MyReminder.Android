package ldev.myNotifier.utils

open class SingleEvent<out T>(private val data: T) {

    private var hasBeenHandled = false

    fun getIfNotConsumed(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            data
        }
    }

    fun get(): T {
        return data
    }
}