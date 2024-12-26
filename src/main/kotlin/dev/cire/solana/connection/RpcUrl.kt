package dev.cire.solana.connection

enum class RpcUrl(val value: String) {
    DEVNET("https://api.devnet.solana.com"),
    MAINNNET("https://api.mainnet-beta.solana.com"),
    TESTNET("https://api.testnet.solana.com"),
}