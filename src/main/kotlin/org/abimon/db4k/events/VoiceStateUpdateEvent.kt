package org.abimon.db4k.events

import org.abimon.db4k.objects.Snowflake

data class VoiceStateUpdateEvent(val user_id: Snowflake, val suppress: Boolean, val session_id: String, val self_mute: Boolean, val mute: Boolean = false, val guild_id: Snowflake, val deaf: Boolean, val channel_id: Snowflake): Event