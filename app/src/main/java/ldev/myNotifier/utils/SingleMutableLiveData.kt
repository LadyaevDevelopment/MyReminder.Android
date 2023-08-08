package ldev.myNotifier.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

typealias SingleLiveData<T> = LiveData<SingleEvent<T>>

class SingleMutableLiveData<T> : MutableLiveData<SingleEvent<T>> {
    constructor() : super()
    constructor(value: T) : super(SingleEvent(value))

    fun applyValue(value: T) {
        postValue(SingleEvent(value))
    }
}