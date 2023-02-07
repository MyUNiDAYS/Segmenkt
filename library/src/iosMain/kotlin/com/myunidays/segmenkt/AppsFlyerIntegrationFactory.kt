package com.myunidays.segmenkt

actual class AppsFlyerIntegrationFactory internal constructor(
    val ios: cocoapods.Analytics.SEGAppsFlyerIntegrationFactory
) {
    companion object {
        fun createWithSettings(
            delegate: cocoapods.Analytics.SEGAppsFlyerLibDelegateProtocol?,
            andDeepLinkDelegate: cocoapods.Analytics.SEGAppsFlyerDeepLinkDelegateProtocol?
        ): AppsFlyerIntegrationFactory =
            AppsFlyerIntegrationFactory(
                cocoapods.Analytics.SEGAppsFlyerIntegrationFactory.createWithLaunchDelegate(
                    delegate, andDeepLinkDelegate
                )!!
            )
    }
    actual fun key(): String = ios.key()
}
