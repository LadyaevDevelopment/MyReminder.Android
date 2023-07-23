package ldev.myNotifier.debugSettings

interface DataProvider {
    fun <T> provide(clazz: Class<T>) : T?
}