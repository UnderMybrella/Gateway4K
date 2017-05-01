package org.abimon.db4k.events

import org.abimon.db4k.objects.Emoji
import org.abimon.db4k.objects.Snowflake

data class ReactionAddEvent(val user_id: Snowflake, val message_id: Snowflake, val emoji: Emoji): Event