package org.abimon.db4k.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class LDTDeserialiser: JsonDeserializer<LocalDateTime>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): LocalDateTime = if(p.valueAsString.matches("\\d+".toRegex())) LocalDateTime.ofEpochSecond(p.valueAsLong, 0, ZoneOffset.UTC) else LocalDateTime.parse(p.valueAsString, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
}

class LDTSerialiser: JsonSerializer<LocalDateTime>() {
    override fun serialize(value: LocalDateTime, gen: JsonGenerator, serializers: SerializerProvider) = gen.writeString(value.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME  ))
}