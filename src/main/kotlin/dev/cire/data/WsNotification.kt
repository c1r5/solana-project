package dev.cire.data

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*

object WsNotificationSerializer : KSerializer<WsNotification> {
    override val descriptor: SerialDescriptor
        get() = buildClassSerialDescriptor("WsNotification") {
            element<String>("method")
        }

    override fun deserialize(decoder: Decoder): WsNotification {
        val jsonDecoder = decoder as? JsonDecoder
            ?: throw SerializationException("This class can be deserialized only by Json")
        val jsonElement = jsonDecoder.decodeJsonElement()
        val wsNotification = WsNotification.from(jsonElement)

        return wsNotification
    }

    override fun serialize(encoder: Encoder, value: WsNotification) {
        TODO("Not yet implemented")
    }

}

@Serializable(with = WsNotificationSerializer::class)
data class WsNotification(
    val jsonrpc: String? = null,
    val method: String? = null,
    val params: Params? = null,
) {
    companion object {
        fun from(jsonElement: JsonElement): WsNotification {
            return WsNotification(
                jsonrpc = jsonElement.jsonObject["jsonrpc"]?.jsonPrimitive?.content,
                method = jsonElement.jsonObject["method"]?.jsonPrimitive?.content,
                params = Params.from(jsonElement)
            )
        }
    }
}

@Serializable
sealed class MethodNotification {
    @Serializable
    data class ProgramNotification(
        val data: List<String>,
        val executable: Boolean,
        val lamports: Long,
        val owner: String,
        val rentEpoch: Long,
        val space: Long,
    ): MethodNotification() {
        companion object {
            fun from(jsonElement: JsonElement): ProgramNotification {
                val account = jsonElement.jsonObject["params"]
                    ?.jsonObject?.get("result")
                    ?.jsonObject?.get("value")
                    ?.jsonObject?.get("account")
                return ProgramNotification(
                    data = account?.jsonObject?.get("data")?.jsonArray?.toList()?.map {
                        it.jsonPrimitive.content
                    } ?: emptyList(),
                    executable = account?.jsonObject?.get("executable")?.jsonPrimitive?.boolean ?: false,
                    lamports = account?.jsonObject?.get("lamports")?.jsonPrimitive?.long ?: 0L ,
                    owner = account?.jsonObject?.get("owner")?.jsonPrimitive?.content ?: "" ,
                    rentEpoch = account?.jsonObject?.get("rentEpoch")?.jsonPrimitive?.long ?: 0L ,
                    space = account?.jsonObject?.get("space")?.jsonPrimitive?.long ?: 0L ,
                )
            }
        }
    }
}

@Serializable
data class Params(
    val result: Result? = null,
    val subscription: Long? = null,
) {
    companion object {
        fun from(jsonElement: JsonElement): Params {
            return Params(
                result = Result(context = Context.from(jsonElement), value = Value.from(jsonElement)),
                subscription = jsonElement.jsonObject["params"]
                    ?.jsonObject?.get("subscription")
                    ?.jsonPrimitive?.long
            )
        }
    }
}

@Serializable
data class Result(
    val context: Context? = null,
    val value: Value? = null,
)

@Serializable
data class Context(
    val slot: Long? = null,
) {
    companion object {
        fun from(jsonElement: JsonElement): Context {
            return Context(
                slot = jsonElement.jsonObject["params"]
                    ?.jsonObject?.get("result")
                    ?.jsonObject?.get("context")
                    ?.jsonObject?.get("slot")
                    ?.jsonPrimitive?.long
            )
        }
    }
}

@Serializable
data class Value(
    val account: MethodNotification? = null,
    val pubkey: String? = null,
) {
    companion object {
        fun from(jsonElement: JsonElement): Value {
            val method = jsonElement.jsonObject["method"]?.jsonPrimitive?.content
            val valueElement = jsonElement.jsonObject["params"]
                ?.jsonObject?.get("result")
                ?.jsonObject?.get("value")
            val account = when(method) {
                "programNotification" -> MethodNotification.ProgramNotification.from(jsonElement)
                else -> null
            }
            return Value(
                account = account,
                pubkey = valueElement?.jsonObject?.get("pubkey")?.jsonPrimitive?.content
            )
        }
    }
}
