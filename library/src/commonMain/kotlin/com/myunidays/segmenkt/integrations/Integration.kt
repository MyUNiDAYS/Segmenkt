package com.myunidays.segmenkt.integrations

interface Integration<T> {
    fun identify(identifyPayload: IdentifyPayload)
    fun group(groupPayload: GroupPayload)
    fun track(trackPayload: TrackPayload)
    fun alias(aliasPayload: AliasPayload)
    fun screen(screenPayload: ScreenPayload)
    fun flush()
    fun reset()
    fun debug(debug: Boolean)
}
