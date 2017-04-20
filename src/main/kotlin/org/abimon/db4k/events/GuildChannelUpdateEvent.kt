package org.abimon.db4k.events

import org.abimon.db4k.objects.Channel

data class GuildChannelUpdateEvent(val channel: Channel): Event