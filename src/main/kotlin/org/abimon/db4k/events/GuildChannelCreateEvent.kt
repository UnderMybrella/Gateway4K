package org.abimon.db4k.events

import org.abimon.db4k.objects.Channel

data class GuildChannelCreateEvent(val channel: Channel): Event