package org.abimon.db4k.events

import org.abimon.db4k.objects.ExplicitContentFilter
import org.abimon.db4k.objects.FriendSourceFlags
import java.util.*

data class UserSettingsUpdateEvent(
        val theme: Optional<String>,
        val message_display_compact: Optional<Boolean>,
        val explicit_content_filter: Optional<ExplicitContentFilter>,
        val default_guilds_restricted: Optional<Boolean>,
        val friend_source_flags: Optional<FriendSourceFlags>,
        val detect_platform_accounts: Optional<Boolean>
): Event