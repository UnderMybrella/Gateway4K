package org.abimon.db4k.events

import org.abimon.db4k.objects.Guild

data class GuildLoadedEvent(val guild: Guild): Event