package com.myunidays.segmenkt

/**
 * Configuration that analytics can use
 * @property writeKey the Segment writeKey
 * @property application defaults to `null`
 * @property storageProvider Provider for storage class, defaults to `ConcreteStorageProvider`
 * @property collectDeviceId collect deviceId, defaults to `false`
 * @property trackApplicationLifecycleEvents automatically send track for Lifecycle events defaults to `false`
 * @property useLifecycleObserver enables the use of LifecycleObserver Defaults to `false`.
 * @property trackDeepLinks automatically track opened based on intents, defaults to `false`
 * @property flushAt count of events at which we flush events, defaults to `20`
 * @property flushInterval interval in seconds at which we flush events, defaults to `30 seconds`
 * @property defaultSettings Settings object that will be used as fallback in case of network failure, defaults to empty
 * @property autoAddSegmentDestination automatically add SegmentDestination plugin, defaults to `true`
 * @property apiHost set a default apiHost to which Segment sends events, defaults to `api.segment.io/v1`
 */

data class Configuration(
    val writeKey: String,
    var tag: String? = null,
    var application: Any? = null,
//    val storageProvider: StorageProvider,
    var collectDeviceId: Boolean = false,
    var trackApplicationLifecycleEvents: Boolean = false,
    var useLifecycleObserver: Boolean = false,
    var trackDeepLinks: Boolean = false,
    var flushAt: Int = 20,
    var flushInterval: Int = 30,
//    val defaultSettings: Settings = Settings(),
//    val autoAddSegmentDestination: Boolean = true,

    var apiHost: String? = null,
//    val cdnHost: String? = null
    var factories: List<AppsFlyerIntegrationFactory> = emptyList(), // need to change this to be generic factory
    var recordScreenViews: Boolean = true,
    var enableAdvertisingTracking: Boolean = false,
    var adSupportBlock: (() -> String)? = null,
) {
    constructor(writeKey: String) : this(writeKey = writeKey, application = null)
    constructor(writeKey: WriteKey, tag: String?, context: Any? = null) :
        this(writeKey = writeKey.keyForPlatform(), tag = tag, application = context)

    fun use(factory: AppsFlyerIntegrationFactory) {
        factories = factories + factory
    }
}
