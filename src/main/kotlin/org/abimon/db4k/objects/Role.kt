package org.abimon.db4k.objects

data class Role(
        val id: Snowflake,
        val name: String,
        val color: Int,
        val hoist: Boolean,
        val position: Int,
        val permissions: Int,
        val managed: Boolean,
        val mentionable: Boolean
)