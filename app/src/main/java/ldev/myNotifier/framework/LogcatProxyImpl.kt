package ldev.myNotifier.framework

import android.util.Log
import ldev.myNotifier.core.LogcatProxy
import javax.inject.Inject

class LogcatProxyImpl @Inject constructor() : LogcatProxy {
    override fun logDebug(tag: String, text: String) {
        Log.d(tag, text)
    }
}