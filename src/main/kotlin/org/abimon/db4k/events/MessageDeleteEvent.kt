package org.abimon.db4k.events

data class MessageDeleteEvent(val id: String, val channel_id: String): Event