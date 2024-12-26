package dev.cire.solana.rpc.data.dtos.response.rpc

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName


@Serializable
data class Header (

  @SerialName("numReadonlySignedAccounts"   ) var numReadonlySignedAccounts   : Int? = null,
  @SerialName("numReadonlyUnsignedAccounts" ) var numReadonlyUnsignedAccounts : Int? = null,
  @SerialName("numRequiredSignatures"       ) var numRequiredSignatures       : Int? = null

)