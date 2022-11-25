package com.myunidays.segmenkt

data class WriteKey(
    val android: String?,
    val ios: String?,
    val js: String?
) {

    fun keyForPlatform(): String = when (platform) {
        PlatformType.IOS -> ios!!
        PlatformType.ANDROID -> android!!
        PlatformType.JS -> js!!
    }
}
