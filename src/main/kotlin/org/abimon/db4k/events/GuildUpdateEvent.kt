package org.abimon.db4k.events

import org.abimon.db4k.objects.Guild

data class GuildUpdateEvent(val guild: Guild): Event