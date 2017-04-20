package org.abimon.db4k.objects

import java.util.*

data class Channel(
        val id: Snowflake,
        val guild_id: Snowflake,
        val name: String,
        val type: String,
        val position: Int,
        val is_private: Boolean,
        val permission_overwrites: Array<PermissionOverwrite>
) {
    override fun equals(other: Any?): Boolean {
        if(other == null)
            return false

        when(other) {
            is Channel -> return other.id == id
            is String -> return other == id
            else -> return super.equals(other)
        }
    }

    override fun hashCode(): Int = id.hashCode()
}

data class DMChannel(
        val id: Snowflake,
        val is_private: Boolean,
        val recipients: Array<User>,
        val last_message_id: Optional<String>
) {
    override fun equals(other: Any?): Boolean {
        if(other == null)
            return false

        when(other) {
            is DMChannel -> return other.id == id
            is Channel -> return other.id == id
            is String -> return other == id
            else -> return super.equals(other)
        }
    }

    override fun hashCode(): Int = id.hashCode()
}

data class PermissionOverwrite(val id: Snowflake, val type: String, val allow: Int, val deny: Int)