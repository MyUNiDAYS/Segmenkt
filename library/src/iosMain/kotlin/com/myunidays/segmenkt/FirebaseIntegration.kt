package com.myunidays.segmenkt

import com.myunidays.segmenkt.integrations.AliasPayload
import com.myunidays.segmenkt.integrations.GroupPayload
import com.myunidays.segmenkt.integrations.IdentifyPayload
import com.myunidays.segmenkt.integrations.Integration
import com.myunidays.segmenkt.integrations.IntegrationFactory
import com.myunidays.segmenkt.integrations.ScreenPayload
import com.myunidays.segmenkt.integrations.TrackPayload

actual class FirebaseIntegration internal constructor(
    private val ios: cocoapods.SegmentFirebase.SEGFirebaseIntegration
) : Integration<FirebaseIntegration> {
    override fun identify(identifyPayload: IdentifyPayload) =
        ios.identify(identifyPayload as cocoapods.SegmentFirebase.SEGIdentifyPayload)

    override fun group(groupPayload: GroupPayload) =
        ios.group(groupPayload as cocoapods.SegmentFirebase.SEGGroupPayload)

    override fun track(trackPayload: TrackPayload) =
        ios.track(trackPayload as cocoapods.SegmentFirebase.SEGTrackPayload)

    override fun alias(aliasPayload: AliasPayload) =
        ios.alias(aliasPayload as cocoapods.SegmentFirebase.SEGAliasPayload)

    override fun screen(screenPayload: ScreenPayload) =
        ios.screen(screenPayload as cocoapods.SegmentFirebase.SEGScreenPayload)

    override fun flush() = ios.flush()

    override fun reset() = ios.reset()

    override fun debug(debug: Boolean) {
    }

    actual companion object {
        actual fun factory(
            delegate: Any?,
            deeplinkHandler: Any?
        ): IntegrationFactory =
            cocoapods.SegmentFirebase.SEGFirebaseIntegrationFactory.Companion.instance() as cocoapods.Analytics.SEGIntegrationFactoryProtocol
    }
}