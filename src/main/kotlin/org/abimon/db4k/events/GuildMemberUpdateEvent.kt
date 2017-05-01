package org.abimon.db4k.events

import org.abimon.db4k.objects.Snowflake
import org.abimon.db4k.objects.User
import java.util.*

data class GuildMemberUpdateEvent(val guild_id: Snowflake, val roles: Array<Snowflake>, val user: User, val nick: Optional<String>): Event