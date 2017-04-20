package org.abimon.db4k.events

import org.abimon.db4k.objects.Role
import org.abimon.db4k.objects.Snowflake

data class RoleCreateEvent(val guild_id: Snowflake, val role: Role): Event