package com.myunidays.segmenkt

import com.myunidays.segmenkt.integrations.Integration
import com.myunidays.segmenkt.integrations.IntegrationFactory

expect class FirebaseIntegration : Integration<FirebaseIntegration> {
    companion object {
        fun factory(delegate: Any? = null, deeplinkHandler: Any? = null): IntegrationFactory
    }
}
