package org.abimon.db4k.events

import org.abimon.db4k.objects.Snowflake

data class RoleDeleteEvent(val guild_id: Snowflake, val role_id: Snowflake): Event