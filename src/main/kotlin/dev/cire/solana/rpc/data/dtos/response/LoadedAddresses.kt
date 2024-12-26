package dev.cire.solana.rpc.data.dtos.response

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName


@Serializable
data class LoadedAddresses (

  @SerialName("readonly" ) var readonly : ArrayList<String> = arrayListOf(),
  @SerialName("writable" ) var writable : ArrayList<String> = arrayListOf()

)