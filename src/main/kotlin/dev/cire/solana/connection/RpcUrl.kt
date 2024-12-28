package dev.cire.solana.connection

enum class RpcUrl(val value: String) {
    DEVNET("https://api.devnet.solana.com"),
    MAINNNET("https://solana-rpc.publicnode.com"),
    TESTNET("https://api.testnet.solana.com"),
    WSNODE("wss://solana-rpc.publicnode.com")
}