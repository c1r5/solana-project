package dev.cire.solana.rpc.data.dtos.response.ws

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName


@Serializable
data class Context (
  @SerialName("slot" ) var slot : Int? = null
)