package com.myunidays.segmenkt

expect class AppsFlyerLib {
    companion object {
        fun shared(): AppsFlyerLib
    }
    val isStopped: Boolean
    fun getAppsFlyerUID(context: Any? = null): String?
    fun start(context: Any? = null)
    fun stop(shouldStop: Boolean, context: Any? = null)
    fun setDebug(isDebug: Boolean)
}
