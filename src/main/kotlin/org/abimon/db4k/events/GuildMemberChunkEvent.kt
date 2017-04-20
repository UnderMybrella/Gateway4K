package org.abimon.db4k.events

import org.abimon.db4k.objects.GuildMember
import org.abimon.db4k.objects.Snowflake

data class GuildMemberChunkEvent(val guild_id: Snowflake, val members: Array<GuildMember>): Event