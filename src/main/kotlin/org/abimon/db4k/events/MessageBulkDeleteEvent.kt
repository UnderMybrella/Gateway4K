package org.abimon.db4k.events

import java.util.*

data class MessageBulkDeleteEvent(val ids: Array<String>, val channel_id: String): Event {
    override fun hashCode(): Int = Arrays.hashCode(ids)
    override fun equals(other: Any?): Boolean {
        if((other ?: return false) is MessageBulkDeleteEvent)
            return (other as MessageBulkDeleteEvent).ids.contentEquals(ids)
        return false
    }
}