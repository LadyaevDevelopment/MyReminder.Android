package ldev.myNotifier.framework

object NumberIncrementer {
    private var currentNumber = 1

    fun next(): Int {
        val nextNumber = currentNumber
        currentNumber++
        return nextNumber
    }
}