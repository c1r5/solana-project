package dev.cire.solana.rpc.data.dtos.response.rpc

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName


@Serializable
data class Instructions(

    @SerialName("accounts")
    var accounts: ArrayList<String>? = arrayListOf(),
    @SerialName("data")
    var data: String? = null,
    @SerialName("programIdIndex")
    var programIdIndex: Int? = null,
    @SerialName("parsed")
    var parsed: Parsed? = null,
    @SerialName("stackHeight")
    var stackHeight: Int? = null,
    @SerialName("program")
    var program: String? = null,
    @SerialName("programId")
    var programId: String? = null,
)