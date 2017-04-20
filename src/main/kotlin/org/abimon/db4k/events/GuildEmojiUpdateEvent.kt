package org.abimon.db4k.events

import org.abimon.db4k.objects.Emoji
import org.abimon.db4k.objects.Snowflake

data class GuildEmojiUpdateEvent(val guild_id: Snowflake, val emojis: Array<Emoji>): Event