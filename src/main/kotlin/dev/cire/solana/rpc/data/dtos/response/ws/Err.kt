package dev.cire.solana.rpc.data.dtos.response.ws

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*

@Serializable(with = ErrSerializer::class)
data class Err (
    @SerialName("InstructionError") var instructionError : List<Any> = listOf()
)

class ErrSerializer : KSerializer<Err> {
    override val descriptor: SerialDescriptor
        get() = ListSerializer(JsonElement.serializer()).descriptor

    override fun deserialize(decoder: Decoder): Err {
        val jsonDecoder = decoder as JsonDecoder
        val jsonObject = jsonDecoder.decodeJsonElement().jsonObject
        val instructionError = jsonObject["InstructionError"]?.jsonArray?.mapNotNull {element ->
            val content = checkElement(element);

            if (content is JsonElement) {
                val custom = content.jsonObject["Custom"]
                custom?.let {
                    when {
                        custom is JsonPrimitive && custom.isString -> custom.content
                        custom is JsonPrimitive && custom.intOrNull != null -> custom.int
                        else -> null
                    }
                }
            } else {
                content
            }

        } ?: listOf()

        return Err(instructionError = instructionError)
    }

    override fun serialize(encoder: Encoder, value: Err) {
        TODO("Not yet implemented")
    }

    private fun checkElement(element: JsonElement): Any {
        return when {
            element is JsonPrimitive && element.isString -> element.content
            element is JsonPrimitive && element.intOrNull != null -> element.int
            else -> element
        }
    }
}


