package org.abimon.db4k.events

import org.abimon.db4k.objects.Snowflake
import org.abimon.db4k.objects.User

data class GuildBanEvent(val guild_id: Snowflake, val banned: User): Event