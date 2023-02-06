package com.myunidays.segmenkt

actual class AppsFlyerLib internal constructor(val ios: cocoapods.Analytics.AppsFlyerLib) {
    actual companion object {
        actual fun shared(context: Any?): AppsFlyerLib =
            AppsFlyerLib(cocoapods.Analytics.AppsFlyerLib.shared())
        fun shared(): AppsFlyerLib = shared(null)
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
}
