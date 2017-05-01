package org.abimon.db4k.events

import org.abimon.db4k.objects.Snowflake
import java.time.LocalDateTime

data class TypingStartEvent(val user_id: Snowflake, val timestamp: LocalDateTime, val channel_id: Snowflake): Event