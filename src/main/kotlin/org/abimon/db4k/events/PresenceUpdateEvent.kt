package org.abimon.db4k.events

import org.abimon.db4k.objects.PartialUser
import java.util.*

data class PresenceUpdateEvent(
        val user: Optional<PartialUser>,
        val roles: Optional<Array<String>>,
        val game: Optional<Game>,
        val guild_id: Optional<String>,
        val status: Optional<String>
): Event {
    override fun hashCode(): Int = user.map { (id) -> id.hashCode() }.orElse(0)
    override fun equals(other: Any?): Boolean {
        if((other ?: return false) is PresenceUpdateEvent) {
            val presence = (other as PresenceUpdateEvent)
            return (presence.user.isPresent && user.isPresent && presence.user.get().id == user.get().id) && guild_id == presence.guild_id
        }

        return false
    }
}

data class Game(
        val name: String,
        val type: Int = 0,
        val url: Optional<String>
)