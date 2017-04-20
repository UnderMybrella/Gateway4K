package org.abimon.db4k.events

import org.abimon.db4k.objects.Snowflake
import org.abimon.db4k.objects.User

data class GuildMemberUpdateEvent(val guild_id: Snowflake, val roles: Array<Snowflake>, val user: User, val nick: String): Event