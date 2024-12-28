package dev.cire.solana.helper

import org.bitcoinj.core.Base58

typealias AddressValidator = (String) -> Boolean

val solanaAddressValidator: AddressValidator = {address ->
   runCatching {
       val decodeb58 = Base58.decode(address)
       decodeb58.size == 32
   }.getOrNull() ?: false
}