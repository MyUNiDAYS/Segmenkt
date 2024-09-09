package com.myunidays.segmenkt

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.myunidays.segmenkt.integrations.AliasPayload
import com.myunidays.segmenkt.integrations.GroupPayload
import com.myunidays.segmenkt.integrations.IdentifyPayload
import com.myunidays.segmenkt.integrations.Integration
import com.myunidays.segmenkt.integrations.IntegrationFactory
import com.myunidays.segmenkt.integrations.ScreenPayload
import com.myunidays.segmenkt.integrations.TrackPayload
import com.segment.analytics.Analytics
import com.segment.analytics.Properties
import com.segment.analytics.ValueMap
import com.segment.analytics.integrations.Logger
import com.segment.analytics.internal.Utils
import kotlin.math.min

@Suppress("TooManyFunctions")
actual class FirebaseIntegration internal constructor(
    private val android: AndroidFirebaseIntegration
) : Integration<FirebaseIntegration> {
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

    @Suppress("EmptyFunctionBlock")
    override fun debug(debug: Boolean) { }

    actual companion object {
        actual fun factory(
            delegate: Any?,
            deeplinkHandler: Any?
        ): IntegrationFactory = AndroidFirebaseIntegration.FACTORY
    }
}

/**
    A kotlin converted implementation of FirebaseIntegration https://github.com/segment-integrations/analytics-android-integration-firebase
 */
internal class AndroidFirebaseIntegration(context: Context?, private val logger: Logger) :
    com.segment.analytics.integrations.Integration<FirebaseAnalytics?>() {
    private val firebaseAnalytics = FirebaseAnalytics.getInstance(context!!)
    private var currentActivity: Activity? = null

    override fun onActivityResumed(activity: Activity?) {
        super.onActivityResumed(activity)

        val packageManager = activity?.packageManager
        try {
            val info =
                packageManager?.getActivityInfo(activity.componentName, PackageManager.GET_META_DATA)
            val activityLabel = info?.loadLabel(packageManager).toString()
            firebaseAnalytics.setCurrentScreen(activity!!, activityLabel, null)
            logger.verbose("firebaseAnalytics.setCurrentScreen(activity, %s, null);", activityLabel)
        } catch (e: PackageManager.NameNotFoundException) {
            throw AssertionError("Activity Not Found: $e")
        }
    }

    override fun onActivityStarted(activity: Activity?) {
        super.onActivityStarted(activity)

        this.currentActivity = activity
    }

    override fun onActivityStopped(activity: Activity?) {
        super.onActivityStopped(activity)

        this.currentActivity = null
    }

    override fun identify(identify: com.segment.analytics.integrations.IdentifyPayload) {
        super.identify(identify)

        if (!Utils.isNullOrEmpty(identify.userId())) {
            firebaseAnalytics.setUserId(identify.userId())
        }
        val traits: Map<String?, Any> = identify.traits()
        for (entry in traits.entries) {
            var trait = entry.key
            val value = entry.value.toString()
            trait = makeKey(trait)
            firebaseAnalytics.setUserProperty(trait, value)
            logger.verbose("firebaseAnalytics.setUserProperty(%s, %s);", trait, value)
        }
    }

    override fun track(track: com.segment.analytics.integrations.TrackPayload) {
        super.track(track)

        val event = track.event()
        val eventName = if (EVENT_MAPPER.containsKey(event)) {
            EVENT_MAPPER[event]
        } else {
            makeKey(event)
        }
        val properties = track.properties()
        val formattedProperties = formatProperties(properties)
        firebaseAnalytics.logEvent(eventName!!, formattedProperties)
        logger.verbose("firebaseAnalytics.logEvent(%s, %s);", eventName, formattedProperties)
    }

    override fun screen(screen: com.segment.analytics.integrations.ScreenPayload) {
        super.screen(screen)

        val propertiesBundle = Bundle()
        for ((key, value) in screen) {
            propertiesBundle.putString(key, value.toString())
        }
        propertiesBundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screen.name())
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, propertiesBundle)
        logger.verbose("firebaseAnalytics.screen(activity, %s, null);", screen.name())
    }

    companion object {
        val FACTORY: Factory = object : Factory {
            override fun create(
                settings: ValueMap,
                analytics: Analytics
            ): com.segment.analytics.integrations.Integration<*>? {
                val logger = analytics.logger(FIREBASE_ANALYTICS_KEY)
                if (!Utils.hasPermission(
                        analytics.application, Manifest.permission.ACCESS_NETWORK_STATE
                    )
                ) {
                    logger.debug("ACCESS_NETWORK_STATE is required for Firebase Analytics.")
                    return null
                }
                if (!Utils.hasPermission(analytics.application, Manifest.permission.WAKE_LOCK)) {
                    logger.debug("WAKE_LOCK is required for Firebase Analytics.")
                    return null
                }

                val context: Context = analytics.application

                return AndroidFirebaseIntegration(context, logger)
            }

            override fun key(): String {
                return FIREBASE_ANALYTICS_KEY
            }
        }

        private const val FIREBASE_ANALYTICS_KEY = "Firebase"
        private val EVENT_MAPPER = createEventMap()
        private fun createEventMap(): Map<String, String> {
            val EVENT_MAPPER: MutableMap<String, String> = HashMap()
            EVENT_MAPPER["Product Added"] = FirebaseAnalytics.Event.ADD_TO_CART
            EVENT_MAPPER["Checkout Started"] = FirebaseAnalytics.Event.BEGIN_CHECKOUT
            EVENT_MAPPER["Order Completed"] = FirebaseAnalytics.Event.PURCHASE
            EVENT_MAPPER["Order Refunded"] = FirebaseAnalytics.Event.REFUND
            EVENT_MAPPER["Product Viewed"] = FirebaseAnalytics.Event.VIEW_ITEM
            EVENT_MAPPER["Product List Viewed"] = FirebaseAnalytics.Event.VIEW_ITEM_LIST
            EVENT_MAPPER["Payment Info Entered"] = FirebaseAnalytics.Event.ADD_PAYMENT_INFO
            EVENT_MAPPER["Promotion Viewed"] = FirebaseAnalytics.Event.VIEW_PROMOTION
            EVENT_MAPPER["Product Added to Wishlist"] = FirebaseAnalytics.Event.ADD_TO_WISHLIST
            EVENT_MAPPER["Product Shared"] = FirebaseAnalytics.Event.SHARE
            EVENT_MAPPER["Product Clicked"] = FirebaseAnalytics.Event.SELECT_CONTENT
            EVENT_MAPPER["Products Searched"] = FirebaseAnalytics.Event.SEARCH
            return EVENT_MAPPER
        }

        private val PROPERTY_MAPPER = createPropertyMap()

        private fun createPropertyMap(): Map<String?, String> {
            val PROPERTY_MAPPER: MutableMap<String?, String> = HashMap()
            PROPERTY_MAPPER["category"] = FirebaseAnalytics.Param.ITEM_CATEGORY
            PROPERTY_MAPPER["product_id"] = FirebaseAnalytics.Param.ITEM_ID
            PROPERTY_MAPPER["name"] = FirebaseAnalytics.Param.ITEM_NAME
            PROPERTY_MAPPER["price"] = FirebaseAnalytics.Param.PRICE
            PROPERTY_MAPPER["quantity"] = FirebaseAnalytics.Param.QUANTITY
            PROPERTY_MAPPER["query"] = FirebaseAnalytics.Param.SEARCH_TERM
            PROPERTY_MAPPER["shipping"] = FirebaseAnalytics.Param.SHIPPING
            PROPERTY_MAPPER["tax"] = FirebaseAnalytics.Param.TAX
            PROPERTY_MAPPER["total"] = FirebaseAnalytics.Param.VALUE
            PROPERTY_MAPPER["revenue"] = FirebaseAnalytics.Param.VALUE
            PROPERTY_MAPPER["order_id"] = FirebaseAnalytics.Param.TRANSACTION_ID
            PROPERTY_MAPPER["currency"] = FirebaseAnalytics.Param.CURRENCY
            PROPERTY_MAPPER["products"] = FirebaseAnalytics.Param.ITEMS
            return PROPERTY_MAPPER
        }

        private val PRODUCT_MAPPER = createProductMap()

        private fun createProductMap(): Map<String?, String> {
            val MAPPER: MutableMap<String?, String> = HashMap()
            MAPPER["category"] = FirebaseAnalytics.Param.ITEM_CATEGORY
            MAPPER["product_id"] = FirebaseAnalytics.Param.ITEM_ID
            MAPPER["id"] = FirebaseAnalytics.Param.ITEM_ID
            MAPPER["name"] = FirebaseAnalytics.Param.ITEM_NAME
            MAPPER["price"] = FirebaseAnalytics.Param.PRICE
            MAPPER["quantity"] = FirebaseAnalytics.Param.QUANTITY
            return MAPPER
        }

        private fun formatProperties(properties: Properties): Bundle {
            val bundle = Bundle()
            if ((properties.revenue() != 0.0 || properties.total() != 0.0)
                && Utils.isNullOrEmpty(properties.currency())
            ) {
                bundle.putString(FirebaseAnalytics.Param.CURRENCY, "USD")
            }
            for (entry in properties.entries) {
                val value = entry.value
                var property = entry.key
                property = if (PROPERTY_MAPPER.containsKey(property)) {
                    PROPERTY_MAPPER[property]
                } else {
                    makeKey(property)
                }
                if (property == FirebaseAnalytics.Param.ITEMS && value != null) {
                    val products = properties.getList(
                        "products",
                        ValueMap::class.java
                    )
                    val mappedProducts = formatProducts(products)
                    bundle.putParcelableArrayList(property, mappedProducts)
                } else {
                    putValue(bundle, property, value)
                }
            }
            return bundle
        }

        private fun formatProducts(products: List<ValueMap>?): ArrayList<Bundle> {
            val mappedProducts = ArrayList<Bundle>()
            if (products == null) return mappedProducts

            for (product in products) {
                val mappedProduct = Bundle()
                for (innerEntry in product.entries) {
                    var key = innerEntry.key
                    val value = innerEntry.value
                    key = if (PRODUCT_MAPPER.containsKey(key)) {
                        PRODUCT_MAPPER[key]
                    } else {
                        makeKey(key)
                    }
                    putValue(mappedProduct, key, value)
                }
                mappedProducts.add(mappedProduct)
            }
            return mappedProducts
        }

        private fun putValue(bundle: Bundle, key: String?, value: Any?) {
            if (value is Int) {
                bundle.putInt(key, value)
            } else if (value is Double) {
                bundle.putDouble(key, value)
            } else if (value is Long) {
                bundle.putLong(key, value)
            } else {
                val stringValue = value.toString()
                bundle.putString(key, stringValue)
            }
        }

        fun makeKey(key: String?): String {
            var key = key
            val forbiddenChars = arrayOf(".", "-", " ", ":")
            for (forbidden in forbiddenChars) {
                if (key!!.contains(forbidden)) {
                    key = key.trim { it <= ' ' }.replace(forbidden, "_")
                }
            }

            return key!!.substring(0, min(key.length.toDouble(), 40.0).toInt())
        }
    }
}
