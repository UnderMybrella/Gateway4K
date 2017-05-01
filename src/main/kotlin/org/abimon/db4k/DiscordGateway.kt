package org.abimon.db4k

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import io.vertx.core.Vertx
import io.vertx.core.http.HttpClientOptions
import io.vertx.core.http.RequestOptions
import io.vertx.core.http.WebSocket
import io.vertx.core.http.WebSocketFrame
import io.vertx.core.json.DecodeException
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import org.abimon.db4k.events.*
import org.abimon.db4k.jackson.LDTDeserialiser
import org.abimon.db4k.jackson.LDTSerialiser
import org.abimon.db4k.jackson.SnowflakeDeserialiser
import org.abimon.db4k.jackson.SnowflakeSerialiser
import org.abimon.db4k.objects.*
import java.net.URI
import java.time.LocalDateTime
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass

class DiscordGateway(val websocket: WebSocket, val token: String, val listeners: ArrayList<(Event) -> Unit> = ArrayList<(Event) -> Unit>()) {
    val heartbeatSender: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
    val dispatcher: ExecutorService = Executors.newCachedThreadPool()
    var seq: Int? = null

    init {
        websocket.frameHandler(this::onFrame)
    }

    fun onFrame(frame: WebSocketFrame) {
        try {
            if (frame.isText) {
                try {
                    val json = JsonObject(frame.textData())
                    handleGateway(GatewayOP.valueOf(json.getInteger("op")), json, json.getValue("d"))
                } catch(decode: DecodeException) {
                    println("What the fuck, how did we - \nWait, this isn't JSON?!")
                }
            } else if (frame.isBinary) {
                println("Uho, binary frame")
            } else if (frame.isContinuation) {
                println("Continuation frame")
            } else if (frame.isFinal) {
                println("Final frame, um?")
            } else
                println("What? $frame")
        }
        catch(th: Throwable) {
            when(th) {
                is NotImplementedError -> {
                    if(th.stackTrace[0].lineNumber > 100)
                        println("An operation is not yet implemented (line ${th.stackTrace[0].lineNumber})\nPayload: ${frame.textData()}")
                    else {
                        println("A gateway operation is not yet implemented (line ${th.stackTrace[0].lineNumber})")
                        th.printStackTrace()
                        websocket.end()
                    }
                }
                else -> th.printStackTrace()
            }
        }
    }

    fun handleGateway(op: GatewayOP, payload: JsonObject, data: Any?) {
        when(op) {
            GatewayOP.DISPATCH -> event(payload, data as? JsonObject ?: JsonObject())
            GatewayOP.HEARTBEAT -> heartbeat()
            GatewayOP.IDENTIFY -> TODO()
            GatewayOP.STATUS_UPDATE -> TODO()
            GatewayOP.VOICE_STATUS_UPDATE -> TODO()
            GatewayOP.VOICE_SERVER_PING -> TODO()
            GatewayOP.RESUME -> TODO()
            GatewayOP.RECONNECT -> TODO()
            GatewayOP.REQUEST_GUILD_MEMBERS -> TODO()
            GatewayOP.INVALID_SESSIONS -> TODO()
            GatewayOP.HELLO -> {
                heartbeatSender.scheduleAtFixedRate(this::heartbeat, 0, (data as JsonObject).getLong("heartbeat_interval"), TimeUnit.MILLISECONDS)
                identify()
            }
            GatewayOP.HEARTBEAT_ACK -> {}
        }
    }

    fun send(op: GatewayOP, data: Any?) {
        val payload = JsonObject().put("op", op.ordinal).put("d", data).encode()
        websocket.writeTextMessage(payload)
    }

    fun identify() {
        val me = JsonObject()
        val properties = JsonObject()

        properties.put("\$os", System.getProperty("os.name"))
        properties.put("\$browser", "DiscordGateway4K")
        properties.put("\$device", "DiscordGateway4K")
        properties.put("\$referrer", "")
        properties.put("\$referring_domain", "")

        me.put("token", token)
        me.put("compress", false)
        me.put("large_threshold", 50)
        me.put("shard", JsonArray().add(0).add(1))
        me.put("properties", properties)

        send(GatewayOP.IDENTIFY, me)
    }

    fun heartbeat() = send(GatewayOP.HEARTBEAT, seq)

    fun event(payload: JsonObject, data: JsonObject) {
        seq = payload.getInteger("s")
        val eventType = GatewayEvent.eventFor(payload.getString("t"))
        when (eventType) {
            GatewayEvent.READY -> dispatch(data.map(ReadyEvent::class))
            GatewayEvent.RESUMED -> dispatch(ResumedEvent())
            GatewayEvent.CHANNEL_CREATE -> if(data.getBoolean("is_private", false)) dispatch(DMChannelCreateEvent(data.map(DMChannel::class))) else dispatch(GuildChannelCreateEvent(data.map(Channel::class)))
            GatewayEvent.CHANNEL_UPDATE -> dispatch(GuildChannelUpdateEvent(data.map(Channel::class)))
            GatewayEvent.CHANNEL_DELETE -> if(data.getBoolean("is_private", false)) dispatch(DMChannelCloseEvent(data.map(DMChannel::class))) else dispatch(GuildChannelDeleteEvent(data.map(Channel::class)))
            GatewayEvent.GUILD_CREATE -> dispatch(GuildLoadedEvent(data.map(Guild::class)))
            GatewayEvent.GUILD_UPDATE -> dispatch(GuildUpdateEvent(data.map(Guild::class)))
            GatewayEvent.GUILD_DELETE -> if(data.getBoolean("unavailable", false)) dispatch(GuildUnavailableEvent(Snowflake(data.getString("id")))) else dispatch(GuildLeftEvent(Snowflake(data.getString("id"))))
            GatewayEvent.GUILD_BAN_ADD -> dispatch(GuildBanEvent(Snowflake(data.getString("guild_id")), data.map(User::class)))
            GatewayEvent.GUILD_BAN_REMOVE -> dispatch(GuildPardonEvent(Snowflake(data.getString("guild_id")), data.map(User::class)))
            GatewayEvent.GUILD_EMOJIS_UPDATE -> dispatch(data.map(GuildEmojiUpdateEvent::class))
            GatewayEvent.GUILD_INTEGRATIONS_UPDATE -> dispatch(GuildIntegrationUpdateEvent(Snowflake(data.getString("guild_id"))))
            GatewayEvent.GUILD_MEMBER_ADD -> dispatch(GuildMemberAddEvent(Snowflake(data.getString("guild_id")), data.map(GuildMember::class)))
            GatewayEvent.GUILD_MEMBER_REMOVE -> dispatch(data.map(GuildMemberRemoveEvent::class))
            GatewayEvent.GUILD_MEMBER_UPDATE -> dispatch(data.map(GuildMemberUpdateEvent::class))
            GatewayEvent.GUILD_MEMBERS_CHUNK -> dispatch(data.map(GuildMemberChunkEvent::class))
            GatewayEvent.GUILD_ROLE_CREATE -> dispatch(data.map(RoleCreateEvent::class))
            GatewayEvent.GUILD_ROLE_UPDATE -> dispatch(data.map(RoleUpdateEvent::class))
            GatewayEvent.GUILD_ROLE_DELETE -> dispatch(data.map(RoleDeleteEvent::class))
            GatewayEvent.MESSAGE_CREATE -> dispatch(MessageCreateEvent(data.map(Message::class)))
            GatewayEvent.MESSAGE_UPDATE -> dispatch(MessageUpdateEvent(data.map(PartialMessage::class)))
            GatewayEvent.MESSAGE_DELETE -> dispatch(data.map(MessageDeleteEvent::class))
            GatewayEvent.MESSAGE_BULK_DELETE -> dispatch(data.map(MessageBulkDeleteEvent::class))
            GatewayEvent.PRESENCE_UPDATE -> dispatch(data.map(PresenceUpdateEvent::class))
            GatewayEvent.TYPING_START -> dispatch(data.map(TypingStartEvent::class))
            GatewayEvent.USER_SETTINGS_UPDATE -> dispatch(data.map(UserSettingsUpdateEvent::class))
            GatewayEvent.USER_UPDATE -> TODO()
            GatewayEvent.VOICE_STATE_UPDATE -> dispatch(data.map(VoiceStateUpdateEvent::class))
            GatewayEvent.VOICE_SERVER_UPDATE -> TODO()
            GatewayEvent.MESSAGE_REACTION_ADD -> dispatch(data.map(ReactionAddEvent::class))
            GatewayEvent.MESSAGE_REACTION_REMOVE -> dispatch(data.map(ReactionRemoveEvent::class))
            GatewayEvent.MESSAGE_ACK -> dispatch(data.map(MessageAckEvent::class))

            GatewayEvent.UNKNOWN -> println("Unknown event (${payload.getString("t")})!")
        }
    }

    fun dispatch(event: Event) {
        listeners.forEach { listener ->
            dispatcher.submit { listener(event) }
        }
    }

    fun registerListener(listener: (Event) -> Unit) = listeners.add(listener)

    companion object {
        fun obtain(token: String, gatewayUrl: String = "https://discordapp.com/api/gateway", listeners: ArrayList<(Event) -> Unit> = ArrayList<(Event) -> Unit>(), onceFound: (DiscordGateway) -> Unit) {
            val vertx = Vertx.vertx()
            val httpClient = vertx.createHttpClient(HttpClientOptions().setMaxWebsocketFrameSize(Integer.MAX_VALUE))

            httpClient.getAbs(gatewayUrl).handler { response ->
                if (response.statusCode() == 200) {
                    response.bodyHandler { body ->
                        val gateway = body.toJsonObject().getString("url")
                        val uri = URI(gateway)
                        val requestOptions = RequestOptions()

                        requestOptions.isSsl = uri.scheme == "wss"
                        requestOptions.host = uri.host
                        requestOptions.port = if (uri.scheme == "wss") 443 else 80
                        requestOptions.uri = "/?encoding=json&v=6"

                        httpClient.websocketStream(requestOptions, null, null, null).handler { websocket -> onceFound(DiscordGateway(websocket, token, listeners)) }
                    }
                } else {
                    println("Error: ${response.statusCode()} (${response.statusMessage()})")
                }
            }.end()
        }
    }
}

enum class GatewayOP {
    DISPATCH,
    HEARTBEAT,
    IDENTIFY,
    STATUS_UPDATE,
    VOICE_STATUS_UPDATE,
    VOICE_SERVER_PING,
    RESUME,
    RECONNECT,
    REQUEST_GUILD_MEMBERS,
    INVALID_SESSIONS,
    HELLO,
    HEARTBEAT_ACK;

    companion object {
        fun valueOf(op: Int): GatewayOP = values()[op.coerceIn(0 until values().size)]
    }
}
enum class GatewayEvent {
    READY,
    RESUMED,
    CHANNEL_CREATE,
    CHANNEL_UPDATE,
    CHANNEL_DELETE,
    GUILD_CREATE,
    GUILD_UPDATE,
    GUILD_DELETE,
    GUILD_BAN_ADD,
    GUILD_BAN_REMOVE,
    GUILD_EMOJIS_UPDATE,
    GUILD_INTEGRATIONS_UPDATE,
    GUILD_MEMBER_ADD,
    GUILD_MEMBER_REMOVE,
    GUILD_MEMBER_UPDATE,
    GUILD_MEMBERS_CHUNK,
    GUILD_ROLE_CREATE,
    GUILD_ROLE_UPDATE,
    GUILD_ROLE_DELETE,
    MESSAGE_CREATE,
    MESSAGE_UPDATE,
    MESSAGE_DELETE,
    MESSAGE_BULK_DELETE,
    PRESENCE_UPDATE,
    TYPING_START,
    USER_SETTINGS_UPDATE,
    USER_UPDATE,
    VOICE_STATE_UPDATE,
    VOICE_SERVER_UPDATE,
    MESSAGE_REACTION_ADD,
    MESSAGE_REACTION_REMOVE,
    MESSAGE_ACK,
    UNKNOWN;

    companion object {
        fun eventFor(op: String): GatewayEvent {
            try{
                return valueOf(op)
            }
            catch(notEnum: IllegalArgumentException) {
                return UNKNOWN
            }
        }
    }
}

val objMapper: ObjectMapper = ObjectMapper()
        .findAndRegisterModules()
        .registerModule(SimpleModule()
                .addDeserializer(LocalDateTime::class.java, LDTDeserialiser())
                .addDeserializer(Snowflake::class.java, SnowflakeDeserialiser())
                .addSerializer(LocalDateTime::class.java, LDTSerialiser())
                .addSerializer(Snowflake::class.java, SnowflakeSerialiser())
        )
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

operator fun JsonObject.get(key: String): JsonObject = getJsonObject(key)
fun <T : Any> JsonObject.map(mapTo: KClass<T>): T = objMapper.readValue(encode(), mapTo.java)
fun Any.toJSON(): JsonObject = JsonObject(objMapper.writeValueAsString(this))