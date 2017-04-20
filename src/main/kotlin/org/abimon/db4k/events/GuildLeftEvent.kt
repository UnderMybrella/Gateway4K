package org.abimon.db4k.events

import org.abimon.db4k.objects.Snowflake

data class GuildLeftEvent(val id: Snowflake): Event
