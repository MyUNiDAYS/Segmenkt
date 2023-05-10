package com.myunidays.segmenkt

actual class AppsFlyerLib {
    actual companion object {
        actual fun shared(): AppsFlyerLib {
            TODO("Not yet implemented")
        }
    }
    actual fun getAppsFlyerUID(context: Any?): String? = null
    actual fun start(context: Any?) { TODO("Not yet implemented") }
    actual val isStopped: Boolean
        get() = TODO("Not yet implemented")

    actual fun setDebug(isDebug: Boolean) {
    }

    actual fun stop(shouldStop: Boolean, context: Any?) {
    }
}
