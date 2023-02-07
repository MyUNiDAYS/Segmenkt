package com.myunidays.segmenkt

expect class Analytics {

    companion object {
        fun setup(configuration: Configuration): Analytics
        fun shared(context: Any? = null): Analytics
    }

    fun track(name: String, properties: Map<Any?, *>? = null, options: Map<Any?, *>? = null)
    fun identify(userId: String?, traits: Map<Any?, *>? = null, options: Map<Any?, *>? = null)
    fun alias(userId: String, options: Map<Any?, *>? = null)
    fun screen(screenTitle: String, properties: Map<Any?, *>? = null, options: Map<Any?, *>? = null)
    fun group(groupId: String, traits: Map<Any?, *>? = null, options: Map<Any?, *>? = null)
    fun optOut(status: Boolean)

    fun reset()
}
