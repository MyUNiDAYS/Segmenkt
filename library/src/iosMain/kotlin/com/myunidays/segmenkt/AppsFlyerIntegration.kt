package com.myunidays.segmenkt

import cocoapods.Analytics.SEGAppsFlyerDeepLinkDelegateProtocol
import cocoapods.Analytics.SEGAppsFlyerLibDelegateProtocol
import com.myunidays.segmenkt.integrations.AliasPayload
import com.myunidays.segmenkt.integrations.GroupPayload
import com.myunidays.segmenkt.integrations.IdentifyPayload
import com.myunidays.segmenkt.integrations.Integration
import com.myunidays.segmenkt.integrations.IntegrationFactory
import com.myunidays.segmenkt.integrations.ScreenPayload
import com.myunidays.segmenkt.integrations.TrackPayload

actual class AppsFlyerIntegration internal constructor(
    private val ios: cocoapods.Analytics.SEGIntegrationProtocol
) : Integration<AppsFlyerIntegration> {
    override fun identify(identifyPayload: IdentifyPayload) = ios.identify(identifyPayload)
    override fun group(groupPayload: GroupPayload) = ios.group(groupPayload)
    override fun track(trackPayload: TrackPayload) = ios.track(trackPayload)
    override fun alias(aliasPayload: AliasPayload) = ios.alias(aliasPayload)
    override fun screen(screenPayload: ScreenPayload) = ios.screen(screenPayload)
    override fun flush() = ios.flush()
    override fun reset() = ios.reset()
    override fun debug(debug: Boolean) {
        AppsFlyerLib.shared().isDebug = debug
    }

    actual companion object {
        actual fun factory(delegate: Any?, deeplinkHandler: Any?): IntegrationFactory =
            cocoapods.Analytics.SEGAppsFlyerIntegrationFactory.Companion.createWithLaunchDelegate(
                delegate as? SEGAppsFlyerLibDelegateProtocol,
                deeplinkHandler as? SEGAppsFlyerDeepLinkDelegateProtocol
            ) ?: cocoapods.Analytics.SEGAppsFlyerIntegrationFactory()
    }
}
