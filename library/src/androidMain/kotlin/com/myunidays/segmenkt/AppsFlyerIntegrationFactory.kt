package com.myunidays.segmenkt

import com.segment.analytics.integrations.Integration

actual class AppsFlyerIntegrationFactory internal constructor(
    val android: Integration.Factory
) {
    companion object {
        fun create(): AppsFlyerIntegrationFactory =
            AppsFlyerIntegrationFactory(
                com.segment.analytics.android.integrations.appsflyer.AppsflyerIntegration.FACTORY
            )
    }
    actual fun key(): String = android.key()
}
