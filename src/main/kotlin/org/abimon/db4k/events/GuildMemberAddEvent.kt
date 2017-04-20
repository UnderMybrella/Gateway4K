package org.abimon.db4k.events

import org.abimon.db4k.objects.GuildMember
import org.abimon.db4k.objects.Snowflake

data class GuildMemberAddEvent(val guild_id: Snowflake, val member: GuildMember): Event