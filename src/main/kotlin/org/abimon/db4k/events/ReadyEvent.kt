package org.abimon.db4k.events

import org.abimon.db4k.objects.DMChannel
import org.abimon.db4k.objects.User
import java.util.*

data class ReadyEvent(val v: Int, val user: User, val private_channels: Array<DMChannel>): Event {
    override fun equals(other: Any?): Boolean {
        if((other ?: return false) is ReadyEvent) {
            val ready = other as ReadyEvent
            if(ready.v == v && ready.user == user && Arrays.equals(ready.private_channels, private_channels))
                return true
        }
        return false
    }

    override fun hashCode(): Int {
        return v.hashCode() and user.hashCode() and Arrays.hashCode(private_channels)
    }
}