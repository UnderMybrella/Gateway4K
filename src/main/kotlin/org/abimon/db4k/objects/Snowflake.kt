package org.abimon.db4k.objects

import java.math.BigInteger
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

data class Snowflake(val id: String): CharSequence {
    override val length: Int
        get() = id.length

    override fun get(index: Int): Char = id[index]

    override fun subSequence(startIndex: Int, endIndex: Int): CharSequence = id.subSequence(startIndex, endIndex)

    companion object {
        val DISCORD_EPOCH = BigInteger("1420070400000")
    }

    val creationDate: LocalDateTime
        get() = LocalDateTime.ofInstant(Instant.ofEpochMilli(DISCORD_EPOCH.add(BigInteger(id).shiftRight(22)).toLong()), ZoneId.systemDefault())

    override fun equals(other: Any?): Boolean {
        if(other == null)
            return false

        when(other) {
            is String -> return id == other
            is Snowflake -> return id == other.id
        }

        return false
    }

    override fun hashCode(): Int = id.hashCode()
    override fun toString(): String = id
}