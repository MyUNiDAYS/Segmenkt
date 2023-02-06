package com.myunidays.segmenkt

import android.content.Context

actual class AppsFlyerLib internal constructor(val android: com.appsflyer.AppsFlyerLib) {
    actual companion object {
        actual fun shared(context: Any?): AppsFlyerLib =
            AppsFlyerLib(com.appsflyer.AppsFlyerLib.getInstance())
    }

    actual val isStopped get() = android.isStopped

    fun setDebugLog(enable: Boolean) = android.setDebugLog(enable)

    actual fun getAppsFlyerUID(context: Any?): String? = android.getAppsFlyerUID(context as Context)
    actual fun start(context: Any?) = android.start(context as Context)
}
