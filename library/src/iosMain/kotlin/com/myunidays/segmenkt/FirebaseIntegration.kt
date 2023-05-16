package com.myunidays.segmenkt

import com.myunidays.segmenkt.integrations.AliasPayload
import com.myunidays.segmenkt.integrations.GroupPayload
import com.myunidays.segmenkt.integrations.IdentifyPayload
import com.myunidays.segmenkt.integrations.Integration
import com.myunidays.segmenkt.integrations.IntegrationFactory
import com.myunidays.segmenkt.integrations.ScreenPayload
import com.myunidays.segmenkt.integrations.TrackPayload

actual class FirebaseIntegration internal constructor(
    private val ios: cocoapods.Analytics.SEGIntegrationProtocol
) : Integration<FirebaseIntegration> {
    override fun identify(identifyPayload: IdentifyPayload) = ios.identify(identifyPayload)
    override fun group(groupPayload: GroupPayload) = ios.group(groupPayload)
    override fun track(trackPayload: TrackPayload) = ios.track(trackPayload)
    override fun alias(aliasPayload: AliasPayload) = ios.alias(aliasPayload)
    override fun screen(screenPayload: ScreenPayload) = ios.screen(screenPayload)
    override fun flush() = ios.flush()
    override fun reset() = ios.reset()
    override fun debug(debug: Boolean) { }

    actual companion object {
        actual fun factory(
            delegate: Any?,
            deeplinkHandler: Any?
        ): IntegrationFactory =
            cocoapods.SegmentFirebase.SEGFirebaseIntegrationFactory.Companion.instance() as cocoapods.Analytics.SEGIntegrationFactoryProtocol
    }
}