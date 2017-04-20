package org.abimon.db4k.events

import org.abimon.db4k.objects.Channel

data class GuildChannelDeleteEvent(val channel: Channel): Event