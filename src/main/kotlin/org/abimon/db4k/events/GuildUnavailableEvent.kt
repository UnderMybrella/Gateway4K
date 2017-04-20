package org.abimon.db4k.events

import org.abimon.db4k.objects.Snowflake

data class GuildUnavailableEvent(val id: Snowflake): Event