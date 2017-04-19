package org.abimon.db4k.events

import org.abimon.db4k.objects.Message

data class MessageCreateEvent(val msg: Message): Event