package com.myunidays.segmenkt

import com.myunidays.segmenkt.integrations.AliasPayload
import com.myunidays.segmenkt.integrations.GroupPayload
import com.myunidays.segmenkt.integrations.IdentifyPayload
import com.myunidays.segmenkt.integrations.Integration
import com.myunidays.segmenkt.integrations.IntegrationFactory
import com.myunidays.segmenkt.integrations.ScreenPayload
import com.myunidays.segmenkt.integrations.TrackPayload

actual class AppsFlyerIntegration : Integration<AppsFlyerIntegration> {
    override fun identify(identifyPayload: IdentifyPayload) {
    }
    override fun group(groupPayload: GroupPayload) {
    }
    override fun track(trackPayload: TrackPayload) {
    }
    override fun alias(aliasPayload: AliasPayload) {
    }
    override fun screen(screenPayload: ScreenPayload) {
    }
    override fun flush() {
    }
    override fun reset() {
    }

    override fun debug(debug: Boolean) {
    }

    actual companion object {
        actual fun factory(delegate: Any?, deeplinkHandler: Any?): IntegrationFactory = TODO()
    }
}
