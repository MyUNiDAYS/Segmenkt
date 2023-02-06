package com.myunidays.segmenkt

expect class AppsFlyerLib {
    companion object {
        fun shared(context: Any? = null): AppsFlyerLib
    }
    val isStopped: Boolean
    fun getAppsFlyerUID(context: Any? = null): String?

    fun start(context: Any? = null)
}
