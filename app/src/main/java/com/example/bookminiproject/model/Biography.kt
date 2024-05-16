package com.example.bookminiproject.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive

@Serializable
sealed class Biography {
    @Serializable
    @SerialName("String")
    data class Text(val value: String) : Biography()

    @Serializable
    @SerialName("Object")
    data class Object(val type: String, val value: String) : Biography()
}


object BiographySerializer : KSerializer<Biography> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Bio") {
        element<String>("type", isOptional = true)
        element<String>("value")
    }

    override fun deserialize(decoder: Decoder): Biography {
        val jsonElement = (decoder as JsonDecoder).decodeJsonElement()
        return when (jsonElement) {
            is JsonPrimitive -> Biography.Text(jsonElement.content)
            is JsonObject -> {
                val type = jsonElement["type"]?.jsonPrimitive?.content ?: ""
                val value = jsonElement["value"]?.jsonPrimitive?.content ?: throw SerializationException("Missing 'value' field in bio object")
                Biography.Object(type, value)
            }
            else -> throw SerializationException("Unknown biography format")
        }
    }

    override fun serialize(encoder: Encoder, value: Biography) {
        when (value) {
            is Biography.Text -> encoder.encodeString(value.value)
            is Biography.Object -> encoder.encodeSerializableValue(
                JsonObject.serializer(),
                JsonObject(mapOf("type" to JsonPrimitive(value.type), "value" to JsonPrimitive(value.value)))
            )
        }
    }
}