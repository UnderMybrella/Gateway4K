package org.abimon.db4k.events

import org.abimon.db4k.objects.PartialMessage

data class MessageUpdateEvent(val msg: PartialMessage): Event