package dev.cire.solana.rpc.data.dtos.response.rpc

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName


@Serializable
data class Transaction (

    @SerialName("message"    ) var message    : Message?          = Message(),
    @SerialName("signatures" ) var signatures : ArrayList<String> = arrayListOf()

)