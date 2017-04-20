package org.abimon.db4k.events

import org.abimon.db4k.objects.DMChannel

data class DMChannelCreateEvent(val dm: DMChannel): Event