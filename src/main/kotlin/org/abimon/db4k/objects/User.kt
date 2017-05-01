package org.abimon.db4k.objects

import java.time.LocalDateTime
import java.util.*

enum class DefaultAvatars constructor(val code: String) {
    BLURPLE("6debd47ed13483642cf09e832ed0bc1b"),
    GREY("322c936a8c8be1b803cd94861bdfa868"),
    GREEN("dd4dbc0016779df1378e7812eabaa04d"),
    ORANGE("0e291f67c9274a1abdddeb3fd919cbaa"),
    RED("1cbd08c76f8af6dddce02c5138971129");

    val avatarURL: String
        get() = BASE_AVATAR_URL.replace("%colour", code)

    companion object {
        val BASE_AVATAR_URL = "https://discordapp.com/assets/%colour.png"

        fun getDefaultForUser(discrim: String): DefaultAvatars = values()[discrim.toInt() % DefaultAvatars.values().size]
    }
}

data class User(
        val id: Snowflake,
        val username: String,
        val discriminator: String,
        val avatar: Optional<String>,
        val bot: Boolean = false,
        val mfa_enabled: Optional<Boolean>,
        val verified: Optional<Boolean>,
        val email: Optional<String>
) {
    fun getAvatar(): String = avatar.orElseGet { DefaultAvatars.getDefaultForUser(discriminator).avatarURL }
}

data class PartialUser(
        val id: Snowflake,
        val username: Optional<String>,
        val discriminator: Optional<String>,
        val avatar: Optional<String>,
        val bot: Boolean = false,
        val mfa_enabled: Optional<Boolean>,
        val verified: Optional<Boolean>,
        val email: Optional<String>
) {
    fun getAvatar(): String = avatar.orElseGet { DefaultAvatars.getDefaultForUser(discriminator.orElse("0")).avatarURL }
}

data class GuildMember(
        val user: User,
        val nick: Optional<String>,
        val roles: Array<Snowflake>,
        val joined_at: LocalDateTime,
        val deaf: Boolean,
        val mute: Boolean
)

enum class ExplicitContentFilter {
    ON_THE_EDGE,
    FRIENDS_ARE_NICE,
    KEEP_ME_SAFE
}

data class FriendSourceFlags(
        val all: Boolean = false,
        val mutual_guilds: Boolean = all,
        val mutual_friends: Boolean = all
)