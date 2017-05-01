package org.abimon.db4k.events

import org.abimon.db4k.objects.Snowflake

data class MessageAckEvent(val message_id: Snowflake, val channel_id: Snowflake): Event