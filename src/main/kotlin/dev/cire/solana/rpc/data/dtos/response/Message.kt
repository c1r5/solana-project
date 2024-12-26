package dev.cire.solana.rpc.data.dtos.response

import dev.cire.solana.rpc.data.dtos.response.Header
import dev.cire.solana.rpc.data.dtos.response.Instructions
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName


@Serializable
data class Message (

    @SerialName("accountKeys"         ) var accountKeys         : ArrayList<String>       = arrayListOf(),
    @SerialName("addressTableLookups" ) var addressTableLookups : ArrayList<String>       = arrayListOf(),
    @SerialName("header"              ) var header              : Header?                 = Header(),
    @SerialName("instructions"        ) var instructions        : ArrayList<Instructions> = arrayListOf(),
    @SerialName("recentBlockhash"     ) var recentBlockhash     : String?                 = null

)