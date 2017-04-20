package org.abimon.db4k.objects

data class Guild(
        val id: Snowflake,
        val name: String,
        val icon: String,
        val splash: String,
        val owner_id: Snowflake,
        val region: String,
        val afk_channel_id: Snowflake,
        val afk_timeout: Int,
        val embed_enabled: Boolean,
        val embed_channel_id: Snowflake,
        val verification_level: Int,
        val default_messages_notification: Int,
        val roles: Array<Role>,
        val emojis: Array<Emoji>,
        val features: Array<String>,
        val mfa_level: Int,
        val channels: Array<Channel>
)