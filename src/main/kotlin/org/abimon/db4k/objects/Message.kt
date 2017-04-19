package org.abimon.db4k.objects

import java.awt.Color
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.util.*

data class Message(
        val id: String,
        val channel_id: String,
        val author: User,
        val content: String,
        val timestamp: LocalDateTime,
        val edited_timestamp: Optional<ZonedDateTime>,
        val tts: Boolean,
        val mentions_everyone: Boolean,
        val mentions: Array<User>,
        val mention_roles: Array<String>,
        val attachments: Optional<Array<Attachment>>,
        val embeds: Optional<Array<Embed>>,
        val reactions: Optional<Array<Reaction>>,
        val nonce: Optional<String>,
        val pinned: Boolean,
        val webhook_id: Optional<String>
) {
    override fun equals(other: Any?): Boolean {
        if(other == null)
            return false

        when(other) {
            is Message -> return other.id == id
            is String -> return other == id
            else -> return super.equals(other)
        }
    }

    override fun hashCode(): Int = id.hashCode()
}

data class PartialMessage(
        val id: String,
        val channel_id: String,
        val author: Optional<User>,
        val content: Optional<String>,
        val timestamp: Optional<LocalDateTime>,
        val edited_timestamp: Optional<ZonedDateTime>,
        val tts: Optional<Boolean>,
        val mentions_everyone: Optional<Boolean>,
        val mentions: Optional<Array<User>>,
        val mention_roles: Optional<Array<String>>,
        val attachments: Optional<Array<Attachment>>,
        val embeds: Optional<Array<Embed>>,
        val reactions: Optional<Array<Reaction>>,
        val nonce: Optional<String>,
        val pinned: Optional<Boolean>,
        val webhook_id: Optional<String>
) {
    override fun equals(other: Any?): Boolean {
        if(other == null)
            return false

        when(other) {
            is Message -> return other.id == id
            is String -> return other == id
            else -> return super.equals(other)
        }
    }

    override fun hashCode(): Int = id.hashCode()
}

data class Reaction(
        val count: Int,
        val me: Boolean,
        val emoji: Emoji
)

data class Emoji(
        val id: Optional<String>,
        val name: String
)

data class Embed(
        val title: Optional<String>,
        val type: String = "rich",
        val description: Optional<String>,
        val url: Optional<String>,
        val timestamp: Optional<LocalDateTime>,
        val color: Optional<Color>,
        val footer: Optional<EmbedFooter>,
        val image: Optional<EmbedImage>,
        val thumbnail: Optional<EmbedImage>,
        val video: Optional<EmbedVideo>,
        val provider: Optional<EmbedProvider>,
        val author: Optional<EmbedAuthor>,
        val fields: Optional<Array<EmbedField>>
)

data class EmbedFooter(
        val text: Optional<String>,
        val icon_url: Optional<String>,
        val proxy_icon_url: Optional<String>
)

data class EmbedImage(
        val url: Optional<String>,
        val proxy_url: Optional<String>,
        val width: Optional<Int>,
        val height: Optional<Int>
)

data class EmbedVideo(
        val url: Optional<String>,
        val width: Optional<Int>,
        val height: Optional<Int>
)

data class EmbedProvider(
        val name: Optional<String>,
        val url: Optional<String>
)

data class EmbedAuthor(
        val name: Optional<String>,
        val url: Optional<String>,
        val icon_url: Optional<String>,
        val proxy_icon_url: Optional<String>
)

data class EmbedField(
        val name: String,
        val value: String,
        val inline: Boolean
)

data class Attachment(
        val id: String,
        val filename: String,
        val size: Int,
        val url: String,
        val proxy_url: String,
        val width: Optional<Int>,
        val height: Optional<Int>
)