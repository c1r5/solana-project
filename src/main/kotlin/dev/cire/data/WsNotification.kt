package dev.cire.data

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
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
sealed class MethodNotificationValue {
    companion object {
        fun from(methodNotificationNames: MethodNotificationNames, jsonElement: JsonElement): MethodNotificationValue {
            return when (methodNotificationNames) {
                MethodNotificationNames.ACCOUNT_NOTIFICATION -> ProgramNotificationValue.from(jsonElement)
                MethodNotificationNames.BLOCK_NOTIFICATION -> ProgramNotificationValue.from(jsonElement)
                MethodNotificationNames.LOGS_NOTIFICATION -> LogsNotification.from(jsonElement)
                MethodNotificationNames.PROGRAM_NOTIFICATION -> ProgramNotificationValue.from(jsonElement)
                MethodNotificationNames.ROOT_NOTIFICATION -> ProgramNotificationValue.from(jsonElement)
                MethodNotificationNames.SIGNATURE_NOTIFICATION -> ProgramNotificationValue.from(jsonElement)
                MethodNotificationNames.SLOT_NOTIFICATION -> ProgramNotificationValue.from(jsonElement)
                MethodNotificationNames.SLOTS_UPDATES_NOTIFICATION -> ProgramNotificationValue.from(jsonElement)
                MethodNotificationNames.VOTE_NOTIFICATION -> ProgramNotificationValue.from(jsonElement)
            }
        }
    }

    @Serializable
    data class ProgramNotificationValue(
        val pubkey: String?,
        val account: Account?,
    ) : MethodNotificationValue() {
        companion object {
            fun from(jsonElement: JsonElement): ProgramNotificationValue {
                val valueElement = jsonElement.jsonObject["params"]
                    ?.jsonObject?.get("result")
                    ?.jsonObject?.get("value")

                val accountElement = valueElement?.jsonObject?.get("account")

                return ProgramNotificationValue(
                    account = Account(
                        data = accountElement?.jsonObject?.get("data")?.jsonArray?.toList()
                            ?.map { it.jsonPrimitive.content } ?: emptyList(),
                        executable = accountElement?.jsonObject?.get("executable")?.jsonPrimitive?.boolean ?: false,
                        lamports = accountElement?.jsonObject?.get("lamports")?.jsonPrimitive?.long ?: 0L,
                        owner = accountElement?.jsonObject?.get("owner")?.jsonPrimitive?.content ?: "",
                        rentEpoch = accountElement?.jsonObject?.get("rentEpoch")?.jsonPrimitive?.long ?: 0L,
                        space = accountElement?.jsonObject?.get("space")?.jsonPrimitive?.long ?: 0L,
                    ),
                    pubkey = valueElement?.jsonObject?.get("pubkey")?.jsonPrimitive?.content
                )
            }
        }

        @Serializable
        data class Account(
            val data: List<String>,
            val executable: Boolean,
            val lamports: Long,
            val owner: String,
            val rentEpoch: Long,
            val space: Long,
        )
    }

    @Serializable
    data class LogsNotification(
        val err: JsonElement? = null,
        val logs: List<String>? = null,
        val signature: String? = null,
    ) : MethodNotificationValue() {

        companion object {
            fun from(jsonElement: JsonElement): LogsNotification {
                val valueElement = jsonElement.jsonObject["params"]
                    ?.jsonObject?.get("result")
                    ?.jsonObject?.get("value")

                val errElement = valueElement
                    ?.jsonObject?.get("err")
                val logs = valueElement
                    ?.jsonObject?.get("logs")
                    ?.jsonArray?.map { it.jsonPrimitive.content } ?: emptyList()
                val signatureElement = valueElement?.jsonObject?.get("signature")?.jsonPrimitive?.content

                return LogsNotification(errElement, logs, signatureElement)
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
            val methodNotificationValue = jsonElement.jsonObject["method"]?.jsonPrimitive?.content?.let { m ->
                MethodNotificationValue.from(
                    methodNotificationNames = MethodNotificationNames.of(m),
                    jsonElement = jsonElement
                )
            }

            return Params(
                result = Result(
                    context = Context.from(jsonElement),
                    value = methodNotificationValue
                ),
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
    val value: MethodNotificationValue? = null,
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

enum class MethodNotificationNames {
    ACCOUNT_NOTIFICATION,
    BLOCK_NOTIFICATION,
    LOGS_NOTIFICATION,
    PROGRAM_NOTIFICATION,
    ROOT_NOTIFICATION,
    SIGNATURE_NOTIFICATION,
    SLOT_NOTIFICATION,
    SLOTS_UPDATES_NOTIFICATION,
    VOTE_NOTIFICATION;

    companion object {
        fun of(value: String): MethodNotificationNames {
            return when (value) {
                "accountNotification" -> ACCOUNT_NOTIFICATION
                "blockNotification" -> BLOCK_NOTIFICATION
                "logsNotification" -> LOGS_NOTIFICATION
                "programNotification" -> PROGRAM_NOTIFICATION
                "rootNotification" -> ROOT_NOTIFICATION
                "signatureNotification" -> SIGNATURE_NOTIFICATION
                "slotNotification" -> SLOT_NOTIFICATION
                "slotsUpdatesNotification" -> SLOTS_UPDATES_NOTIFICATION
                "voteNotification" -> VOTE_NOTIFICATION
                else -> throw IllegalArgumentException("Unknown method notification names: $value")
            }
        }
    }
}
