package dev.cire.solana.rpc.data.dtos.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.encodeToJsonElement

@Serializable
data class SubscribeMethod (
    @SerialName("jsonrpc" ) var jsonrpc : String?           = null,
    @SerialName("id"      ) var id      : Int?              = null,
    @SerialName("method"  ) var method  : String?           = null,
    @SerialName("params"  ) var params  : List<JsonElement> = arrayListOf(),
) {
    companion object {
        fun from(method: String, params: MutableList<JsonElement> = mutableListOf()): SubscribeMethod {
            if (params.isEmpty()) params.add(Json.encodeToJsonElement("all"))
            return SubscribeMethod(
                jsonrpc = "2.0",
                id = 1,
                method = method,
                params = params,
            )
        }
    }
}


