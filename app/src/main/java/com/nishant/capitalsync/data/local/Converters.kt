package com.nishant.capitalsync.data.local

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {

    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromString(value: String?): List<String> {
        if (value.isNullOrBlank()) return emptyList()
        return try {
            json.decodeFromString(value)
        } catch (_: Exception) {
            emptyList()
        }
    }

    @TypeConverter
    fun fromList(list: List<String>?): String {
        return json.encodeToString(list ?: emptyList())
    }
}