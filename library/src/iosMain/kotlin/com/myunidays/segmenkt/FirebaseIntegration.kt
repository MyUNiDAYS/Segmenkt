package com.myunidays.segmenkt

import com.myunidays.segmenkt.integrations.AliasPayload
import com.myunidays.segmenkt.integrations.GroupPayload
import com.myunidays.segmenkt.integrations.IdentifyPayload
import com.myunidays.segmenkt.integrations.Integration
import com.myunidays.segmenkt.integrations.IntegrationFactory
import com.myunidays.segmenkt.integrations.ScreenPayload
import com.myunidays.segmenkt.integrations.TrackPayload

actual class FirebaseIntegration internal constructor(
    private val ios: cocoapods.Segment_Firebase
) : Integration<FirebaseIntegration> {
    override fun identify(identifyPayload: IdentifyPayload) {
        TODO("Not yet implemented")
    }

    override fun group(groupPayload: GroupPayload) {
        TODO("Not yet implemented")
    }

    override fun track(trackPayload: TrackPayload) {
        TODO("Not yet implemented")
    }

    override fun alias(aliasPayload: AliasPayload) {
        TODO("Not yet implemented")
    }

    override fun screen(screenPayload: ScreenPayload) {
        TODO("Not yet implemented")
    }

    override fun flush() {
        TODO("Not yet implemented")
    }

    override fun reset() {
        TODO("Not yet implemented")
    }

    override fun debug(debug: Boolean) {
        TODO("Not yet implemented")
    }

    actual companion object {
        actual fun factory(
            delegate: Any?,
            deeplinkHandler: Any?
        ): IntegrationFactory {
            TODO("Not yet implemented")
        }
    }
}