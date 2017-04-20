package org.abimon.db4k.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.abimon.db4k.objects.Snowflake

class SnowflakeDeserialiser: JsonDeserializer<Snowflake>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Snowflake = Snowflake(p.valueAsString)
}

class SnowflakeSerialiser: JsonSerializer<Snowflake>() {
    override fun serialize(value: Snowflake, gen: JsonGenerator, serializers: SerializerProvider) = gen.writeString(value.id)
}