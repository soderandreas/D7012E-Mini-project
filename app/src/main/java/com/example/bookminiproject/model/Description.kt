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
sealed class Description {
    @Serializable
    @SerialName("String")
    data class Text(val value: String) : Description()

    @Serializable
    @SerialName("Object")
    data class Object(val type: String, val value: String) : Description()
}


object DescriptionSerializer : KSerializer<Description> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Description") {
        element<String>("type", isOptional = true)
        element<String>("value")
    }

    override fun deserialize(decoder: Decoder): Description {
        val jsonElement = (decoder as JsonDecoder).decodeJsonElement()
        return when (jsonElement) {
            is JsonPrimitive -> Description.Text(jsonElement.content)
            is JsonObject -> {
                val type = jsonElement["type"]?.jsonPrimitive?.content ?: ""
                val value = jsonElement["value"]?.jsonPrimitive?.content ?: throw SerializationException("Missing 'value' field in description object")
                Description.Object(type, value)
            }
            else -> throw SerializationException("Unknown description format")
        }
    }

    override fun serialize(encoder: Encoder, value: Description) {
        when (value) {
            is Description.Text -> encoder.encodeString(value.value)
            is Description.Object -> encoder.encodeSerializableValue(
                JsonObject.serializer(),
                JsonObject(mapOf("type" to JsonPrimitive(value.type), "value" to JsonPrimitive(value.value)))
            )
        }
    }
}