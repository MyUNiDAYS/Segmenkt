package com.myunidays.segmenkt

import android.app.Activity
import android.os.Bundle
import com.myunidays.segmenkt.integrations.AliasPayload
import com.myunidays.segmenkt.integrations.GroupPayload
import com.myunidays.segmenkt.integrations.IdentifyPayload
import com.myunidays.segmenkt.integrations.Integration
import com.myunidays.segmenkt.integrations.IntegrationFactory
import com.myunidays.segmenkt.integrations.ScreenPayload
import com.myunidays.segmenkt.integrations.TrackPayload

actual class AppsFlyerIntegration internal constructor(
    private val android: com.segment.analytics.integrations.Integration<*>
) : Integration<AppsFlyerIntegration> {
    fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) =
        android.onActivityCreated(activity, savedInstanceState)
    fun onActivityStarted(activity: Activity?) = android.onActivityStarted(activity)
    fun onActivityResumed(activity: Activity?) = android.onActivityResumed(activity)
    fun onActivityPaused(activity: Activity?) = android.onActivityPaused(activity)
    fun onActivityStopped(activity: Activity?) = android.onActivityStopped(activity)
    fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) =
        android.onActivitySaveInstanceState(activity, outState)
    fun onActivityDestroyed(activity: Activity?) = android.onActivityDestroyed(activity)
    override fun identify(identifyPayload: IdentifyPayload) = android.identify(identifyPayload)
    override fun group(groupPayload: GroupPayload) = android.group(groupPayload)
    override fun track(trackPayload: TrackPayload) = android.track(trackPayload)
    override fun alias(aliasPayload: AliasPayload) = android.alias(aliasPayload)
    override fun screen(screenPayload: ScreenPayload) = android.screen(screenPayload)
    override fun flush() = android.flush()
    override fun reset() = android.reset()
    override fun debug(debug: Boolean) {
    }

    actual companion object {
        actual fun factory(delegate: Any?, deeplinkHandler: Any?): IntegrationFactory = com.segment.analytics.android.integrations.appsflyer.AppsflyerIntegration.FACTORY
    }
}
