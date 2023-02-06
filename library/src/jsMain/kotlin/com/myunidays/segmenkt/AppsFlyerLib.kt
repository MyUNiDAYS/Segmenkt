package com.myunidays.segmenkt

actual class AppsFlyerLib {
    actual companion object {
        actual fun shared(context: Any?): AppsFlyerLib {
            TODO("Not yet implemented")
        }
    }

    actual fun getAppsFlyerUID(context: Any?): String? = null
    actual fun start(context: Any?) { TODO("Not yet implemented") }
    actual val isStopped: Boolean
        get() = TODO("Not yet implemented")
}
