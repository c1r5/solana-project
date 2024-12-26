package dev.cire.solana.rpc.data.dtos.response

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName


@Serializable
data class Status (

  @SerialName("Ok" ) var ok : String? = null

)