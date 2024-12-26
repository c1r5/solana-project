package dev.cire.solana.data

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*

object SolanaNotificationSerializer : KSerializer<SolanaNotification> {
    override val descriptor: SerialDescriptor
        get() = buildClassSerialDescriptor("WsNotification") {
            element<String>("method")
        }

    override fun deserialize(decoder: Decoder): SolanaNotification {
        val jsonDecoder = decoder as? JsonDecoder
            ?: throw SerializationException("This class can be deserialized only by Json")
        val jsonElement = jsonDecoder.decodeJsonElement()
        val solanaNotification = SolanaNotification.from(jsonElement)

        return solanaNotification
    }

    override fun serialize(encoder: Encoder, value: SolanaNotification) {
        TODO("Not yet implemented")
    }

}

@Serializable(with = SolanaNotificationSerializer::class)
data class SolanaNotification(
    val jsonrpc: String? = null,
    val method: String? = null,
    val params: Params? = null,
) {
    companion object {
        fun from(jsonElement: JsonElement): SolanaNotification {
            return SolanaNotification(
                jsonrpc = jsonElement.jsonObject["jsonrpc"]?.jsonPrimitive?.content,
                method = jsonElement.jsonObject["method"]?.jsonPrimitive?.content,
                params = Params.from(jsonElement)
            )
        }
    }

    fun<T: MethodNotificationValue>valueOf(value: T?): T? {
        return value
    }
}

interface MethodNotificationValue {
    companion object {
        fun from(
            methodNotificationNames: MethodNotificationNames,
            jsonElement: JsonElement
        ): MethodNotificationValue {
            return when (methodNotificationNames) {
                MethodNotificationNames.ACCOUNT_NOTIFICATION -> ProgramNotification.from(jsonElement)
                MethodNotificationNames.BLOCK_NOTIFICATION -> ProgramNotification.from(jsonElement)
                MethodNotificationNames.LOGS_NOTIFICATION -> LogsNotification.from(jsonElement)
                MethodNotificationNames.PROGRAM_NOTIFICATION -> ProgramNotification.from(jsonElement)
                MethodNotificationNames.ROOT_NOTIFICATION -> ProgramNotification.from(jsonElement)
                MethodNotificationNames.SIGNATURE_NOTIFICATION -> ProgramNotification.from(jsonElement)
                MethodNotificationNames.SLOT_NOTIFICATION -> ProgramNotification.from(jsonElement)
                MethodNotificationNames.SLOTS_UPDATES_NOTIFICATION -> ProgramNotification.from(jsonElement)
                MethodNotificationNames.VOTE_NOTIFICATION -> ProgramNotification.from(jsonElement)
            }
        }
    }
}

data class ProgramNotification(
    val pubkey: String?,
    val account: Account?,
): MethodNotificationValue {
    companion object {
        fun from(jsonElement: JsonElement): ProgramNotification {
            val valueElement = jsonElement.jsonObject["params"]
                ?.jsonObject?.get("result")
                ?.jsonObject?.get("value")

            val accountElement = valueElement?.jsonObject?.get("account")

            return ProgramNotification(
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

    data class Account(
        val data: List<String>,
        val executable: Boolean,
        val lamports: Long,
        val owner: String,
        val rentEpoch: Long,
        val space: Long,
    )
}

data class LogsNotification(
    val err: JsonElement? = null,
    val logs: List<String>? = null,
    val signature: String? = null,
): MethodNotificationValue {
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


@Serializable
data class Params(
    val notificationResult: NotificationResult? = null,
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
                notificationResult = NotificationResult(
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
data class NotificationResult(
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

enum class ProgramNames(val address: String) {
    PUMPFUN("6EF8rrecthR5Dkzon8Nwu78hRvfCKubJ14M5uBEwF6P"),
    SYSTEM_PROGRAM("11111111111111111111111111111111")
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
