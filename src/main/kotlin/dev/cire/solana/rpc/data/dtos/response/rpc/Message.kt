package dev.cire.solana.rpc.data.dtos.response.rpc

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName


@Serializable
data class Message (

    @SerialName("accountKeys"           ) var accountKeys         : ArrayList<AccountKey>       = arrayListOf(),
    @SerialName("addressTableLookups"   ) var addressTableLookups : ArrayList<String>       = arrayListOf(),
    @SerialName("header"                ) var header              : Header?                 = null,
    @SerialName("instructions"          ) var instructions        : ArrayList<Instructions> = arrayListOf(),
    @SerialName("recentBlockhash"       ) var recentBlockhash     : String?                 = null,

)