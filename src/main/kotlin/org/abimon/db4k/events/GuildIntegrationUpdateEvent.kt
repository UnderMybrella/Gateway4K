package org.abimon.db4k.events

import org.abimon.db4k.objects.Snowflake

data class GuildIntegrationUpdateEvent(val guild_id: Snowflake): Event