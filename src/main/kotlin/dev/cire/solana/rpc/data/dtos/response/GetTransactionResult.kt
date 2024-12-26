package dev.cire.solana.rpc.data.dtos.response

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName


@Serializable
data class GetTransactionResult (

  @SerialName("blockTime"   ) var blockTime   : Int?         = null,
  @SerialName("meta"        ) var meta        : Meta?        = Meta(),
  @SerialName("slot"        ) var slot        : Int?         = null,
  @SerialName("transaction" ) var transaction : Transaction? = Transaction(),
  @SerialName("version"     ) var version     : Int?         = null

)