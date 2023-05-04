package com.myunidays.segmenkt

actual class AppsFlyerLib internal constructor(val ios: cocoapods.Analytics.AppsFlyerLib) {
    actual companion object {
        actual fun shared(): AppsFlyerLib =
            AppsFlyerLib(cocoapods.Analytics.AppsFlyerLib.shared())
    }
    actual val isStopped get() = ios.isStopped()
    var isDebug: Boolean
        get() = ios.isDebug()
        set(value) = ios.setIsDebug(value)
    fun waitForATTUserAuthorization(timeoutInterval: Double) {
        ios.waitForATTUserAuthorizationWithTimeoutInterval(timeoutInterval)
    }
    actual fun getAppsFlyerUID(context: Any?): String? = ios.getAppsFlyerUID()
    actual fun start(context: Any?) = ios.start()
    actual fun setDebug(isDebug: Boolean) = ios.setIsDebug(isDebug)
}
