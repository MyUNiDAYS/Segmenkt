package com.myunidays.segmenkt

import android.content.Context

actual class AppsFlyerLib internal constructor(val android: com.appsflyer.AppsFlyerLib) {
    actual companion object {
        actual fun shared(): AppsFlyerLib =
            AppsFlyerLib(com.appsflyer.AppsFlyerLib.getInstance())
    }

    actual val isStopped get() = android.isStopped

    actual fun getAppsFlyerUID(context: Any?): String? = android.getAppsFlyerUID(context as Context)
    actual fun start(context: Any?) = android.start(context as Context)
    actual fun setDebug(isDebug: Boolean) = android.setDebugLog(isDebug)
    actual fun stop(shouldStop: Boolean, context: Any?) = android.stop(shouldStop, context as Context)
}
