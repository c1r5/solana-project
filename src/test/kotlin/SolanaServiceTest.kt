import dev.cire.solana.connection.Address
import dev.cire.solana.connection.SolanaRpc
import dev.cire.solana.connection.SolanaWebsocket
import dev.cire.solana.helper.transactionFactory
import dev.cire.solana.rpc.data.dtos.request.Commitment
import dev.cire.solana.rpc.data.dtos.request.SubscribeMethod
import dev.cire.solana.rpc.data.dtos.response.rpc.GetTransactionResponse
import dev.cire.solana.rpc.data.dtos.response.ws.LogsSubscribeResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import org.junit.Test

class SolanaServiceTest {
    val ws = SolanaWebsocket()
    val rpc = SolanaRpc(Dispatchers.IO)
    @Test
    fun `test log parser`() {
        val logs = listOf(
            "Program ComputeBudget111111111111111111111111111111 invoke [1]",
            "Program ComputeBudget111111111111111111111111111111 success",
            "Program 11111111111111111111111111111111 invoke [1]",
            "Program 11111111111111111111111111111111 success",
            "Program 6EF8rrecthR5Dkzon8Nwu78hRvfCKubJ14M5uBEwF6P invoke [1]",
            "Program log: Instruction: Buy",
            "Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA invoke [2]",
            "Program log: Instruction: Transfer",
            "Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA consumed 4645 of 578063 compute units",
            "Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA success",
            "Program 11111111111111111111111111111111 invoke [2]",
            "Program 11111111111111111111111111111111 success",
            "Program 11111111111111111111111111111111 invoke [2]",
            "Program 11111111111111111111111111111111 success",
            "Program 6EF8rrecthR5Dkzon8Nwu78hRvfCKubJ14M5uBEwF6P invoke [2]",
            "Program 6EF8rrecthR5Dkzon8Nwu78hRvfCKubJ14M5uBEwF6P consumed 2003 of 565975 compute units",
            "Program 6EF8rrecthR5Dkzon8Nwu78hRvfCKubJ14M5uBEwF6P success",
            "Program data: vdt/007mYe6f3ZTXKxOgcEi8ZszKDVze513B3iBgKp74BGXCw/VibxXwrwAAAAAArIDRzgkAAAABDY1k4yFTXHfQinrnUSmedq6Wge9McA+HGa/1OOsIgKVt0GVnAAAAAAolP9sVAAAAnkqwROA3AQAKeRvfDgAAAJ6ynfhOOQAA",
            "Program 6EF8rrecthR5Dkzon8Nwu78hRvfCKubJ14M5uBEwF6P consumed 37453 of 599700 compute units",
            "Program 6EF8rrecthR5Dkzon8Nwu78hRvfCKubJ14M5uBEwF6P success",
            "Program 11111111111111111111111111111111 invoke [1]",
            "Program 11111111111111111111111111111111 success",
        )
    }

    @Test
    fun `test rpc getTransaction`() = runBlocking {
        val response = rpc.getTransaction(
            signature = "24GP1s6GFGsXFto7CS2LkjpMW6VQQvqTt1zVQD4ndaF8dW5ot2rEUs2xc4NqJnp45nLZqVDKxLqcUcXziKd8D6oJ"
        )

        assert(response.isSuccess)
    }

    @Test
    fun `test serialization`() {
        val methodRequest = SubscribeMethod.from(
            method = "logsSubscribe",
            params = mutableListOf(
                Json.encodeToJsonElement(mapOf("mentions" to listOf(Address.SYSTEM_ADDRESS))),
                Json.encodeToJsonElement(mapOf("commitent" to Commitment.FINALIZED))
            )
        )
        println(methodRequest)
    }

    @Test
    fun `test deserialization`() {
        val response = """
            {"jsonrpc":"2.0","method":"logsNotification","params":{"result":{"context":{"slot":309999820},"value":{"err":{"InstructionError":[3,{"Custom":6001}]},"logs":["Program ComputeBudget111111111111111111111111111111 invoke [1]","Program ComputeBudget111111111111111111111111111111 success","Program ComputeBudget111111111111111111111111111111 invoke [1]","Program ComputeBudget111111111111111111111111111111 success","Program ATokenGPvbdGVxr1b2hvZbsiqW5xWH25efTNsLJA8knL invoke [1]","Program log: CreateIdempotent","Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA invoke [2]","Program log: Instruction: GetAccountDataSize","Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA consumed 1569 of 186295 compute units","Program return: TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA pQAAAAAAAAA=","Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA success","Program 11111111111111111111111111111111 invoke [2]","Program 11111111111111111111111111111111 success","Program log: Initialize the associated token account","Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA invoke [2]","Program log: Instruction: InitializeImmutableOwner","Program log: Please upgrade to SPL Token 2022 for immutable owner support","Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA consumed 1405 of 179708 compute units","Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA success","Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA invoke [2]","Program log: Instruction: InitializeAccount3","Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA consumed 4188 of 175826 compute units","Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA success","Program ATokenGPvbdGVxr1b2hvZbsiqW5xWH25efTNsLJA8knL consumed 20345 of 191700 compute units","Program ATokenGPvbdGVxr1b2hvZbsiqW5xWH25efTNsLJA8knL success","Program 6m2CDdhRgxpH4WjvdzxAYbGxwdGUz5MziiL5jek2kBma invoke [1]","Program log: Instruction: CommissionSplSwap2","Program log: order_id: 105213","Program log: HFf3QAyVtBaFXDAF8A6TgBtZrEyjcCU8BUvp9iL5pump","Program log: 5N112k9j7gV9JTAngv9KwCXM2Cu93ziXc5kEScywpump","Program log: GYyNGnjBp1nUAFj5kMiVSVnfvgpjphLv1SQZvr4uHngY","Program log: GYyNGnjBp1nUAFj5kMiVSVnfvgpjphLv1SQZvr4uHngY","Program log: before_source_balance: 6032711320, before_destination_balance: 0, amount_in: 8653, expect_amount_out: 430, min_return: 428","Program log: Dex::RaydiumSwap amount_in: 8653, offset: 0","Program 675kPX9MHTjS2zt1qfr1NYHuzeLXfQM9H24wFSUt1Mp8 invoke [2]","Program log: ray_log: A80hAAAAAAAAAQAAAAAAAAABAAAAAAAAAJjek2cBAAAAax/3CA8AAABgYGYrRzQBAAEAAAAAAAAA","Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA invoke [3]","Program log: Instruction: Transfer","Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA consumed 4645 of 115387 compute units","Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA success","Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA invoke [3]","Program log: Instruction: Transfer","Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA consumed 4736 of 107761 compute units","Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA success","Program 675kPX9MHTjS2zt1qfr1NYHuzeLXfQM9H24wFSUt1Mp8 consumed 32015 of 133984 compute units","Program 675kPX9MHTjS2zt1qfr1NYHuzeLXfQM9H24wFSUt1Mp8 success","Program data: QMbN6CYIceIEzSEAAAAAAAABAAAAAAAAAA==","Program log: SwapEvent { dex: RaydiumSwap, amount_in: 8653, amount_out: 1 }","Program log: 9aYn7KZ6Mhhv64QAtF7Ewr97WA4Ku68cyv61tmX96nyB","Program log: 2rikd7tzPbmowhUJzPNVtX7fuUGcnBa8jqJnx6HbtHeE","Program log: Dex::RaydiumSwap amount_in: 1, offset: 19","Program 675kPX9MHTjS2zt1qfr1NYHuzeLXfQM9H24wFSUt1Mp8 invoke [2]","Program log: ray_log: AwEAAAAAAAAAAQAAAAAAAAACAAAAAAAAAGanN1sEAAAASQ2SCQAAAAAoAiR+DwAAAJ4BAAAAAAAA","Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA invoke [3]","Program log: Instruction: Transfer","Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA consumed 4736 of 63599 compute units","Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA success","Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA invoke [3]","Program log: Instruction: Transfer","Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA consumed 4645 of 55882 compute units","Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA success","Program 675kPX9MHTjS2zt1qfr1NYHuzeLXfQM9H24wFSUt1Mp8 consumed 30497 of 80677 compute units","Program 675kPX9MHTjS2zt1qfr1NYHuzeLXfQM9H24wFSUt1Mp8 success","Program data: QMbN6CYIceIEAQAAAAAAAACeAQAAAAAAAA==","Program log: SwapEvent { dex: RaydiumSwap, amount_in: 1, amount_out: 414 }","Program log: 2rikd7tzPbmowhUJzPNVtX7fuUGcnBa8jqJnx6HbtHeE","Program log: 2tX48aAScGM8Eyzpxx2hEruv9Jw5iSrFppgkmawngLV1","Program log: after_source_balance: 6032702667, after_destination_balance: 414, source_token_change: 8653, destination_token_change: 414","Program log: AnchorError thrown in programs/dex-solana/src/instructions/common.rs:379. Error Code: MinReturnNotReached. Error Number: 6001. Error Message: Min return not reached.","Program 6m2CDdhRgxpH4WjvdzxAYbGxwdGUz5MziiL5jek2kBma consumed 132507 of 171355 compute units","Program 6m2CDdhRgxpH4WjvdzxAYbGxwdGUz5MziiL5jek2kBma failed: custom program error: 0x1771"],"signature":"3TzYphEnLF8bALZoXahdfeviu111EAdi4RQteHwUttDQTR7mLiJzdcTKXB4wZRqGnP3NY79hoPAfRDLeApFwLEbi"}},"subscription":679731}}
        """.trimIndent()

        val encode = Json.decodeFromString<LogsSubscribeResponse>(response)

        println(encode)
    }

    @Test
    fun `test action detection`() {
        val response = """
            {"jsonrpc":"2.0","method":"logsNotification","params":{"result":{"context":{"slot":310199880},"value":{"err":null,"logs":["Program ComputeBudget111111111111111111111111111111 invoke [1]","Program ComputeBudget111111111111111111111111111111 success","Program ComputeBudget111111111111111111111111111111 invoke [1]","Program ComputeBudget111111111111111111111111111111 success","Program 6EF8rrecthR5Dkzon8Nwu78hRvfCKubJ14M5uBEwF6P invoke [1]","Program log: Instruction: Create","Program 11111111111111111111111111111111 invoke [2]","Program 11111111111111111111111111111111 success","Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA invoke [2]","Program log: Instruction: InitializeMint2","Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA consumed 2780 of 167434 compute units","Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA success","Program 11111111111111111111111111111111 invoke [2]","Program 11111111111111111111111111111111 success","Program ATokenGPvbdGVxr1b2hvZbsiqW5xWH25efTNsLJA8knL invoke [2]","Program log: Create","Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA invoke [3]","Program log: Instruction: GetAccountDataSize","Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA consumed 1595 of 143538 compute units","Program return: TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA pQAAAAAAAAA=","Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA success","Program 11111111111111111111111111111111 invoke [3]","Program 11111111111111111111111111111111 success","Program log: Initialize the associated token account","Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA invoke [3]","Program log: Instruction: InitializeImmutableOwner","Program log: Please upgrade to SPL Token 2022 for immutable owner support","Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA consumed 1405 of 136925 compute units","Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA success","Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA invoke [3]","Program log: Instruction: InitializeAccount3","Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA consumed 4214 of 133041 compute units","Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA success","Program ATokenGPvbdGVxr1b2hvZbsiqW5xWH25efTNsLJA8knL consumed 21990 of 150513 compute units","Program ATokenGPvbdGVxr1b2hvZbsiqW5xWH25efTNsLJA8knL success","Program metaqbxxUerdq28cj1RbAWkYQm3ybzjb6a8bt518x1s invoke [2]","Program log: IX: Create Metadata Accounts v3","Program 11111111111111111111111111111111 invoke [3]","Program 11111111111111111111111111111111 success","Program log: Allocate space for the account","Program 11111111111111111111111111111111 invoke [3]","Program 11111111111111111111111111111111 success","Program log: Assign the account to the owning program","Program 11111111111111111111111111111111 invoke [3]","Program 11111111111111111111111111111111 success","Program metaqbxxUerdq28cj1RbAWkYQm3ybzjb6a8bt518x1s consumed 35524 of 113354 compute units","Program metaqbxxUerdq28cj1RbAWkYQm3ybzjb6a8bt518x1s success","Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA invoke [2]","Program log: Instruction: MintTo","Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA consumed 4492 of 75315 compute units","Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA success","Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA invoke [2]","Program log: Instruction: SetAuthority","Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA consumed 2911 of 68676 compute units","Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA success","Program 6EF8rrecthR5Dkzon8Nwu78hRvfCKubJ14M5uBEwF6P invoke [2]","Program 6EF8rrecthR5Dkzon8Nwu78hRvfCKubJ14M5uBEwF6P consumed 2003 of 61652 compute units","Program 6EF8rrecthR5Dkzon8Nwu78hRvfCKubJ14M5uBEwF6P success","Program data: G3KpTd7rY3YFAAAANDQ0NDQFAAAANDQ0NDRDAAAAaHR0cHM6Ly9pcGZzLmlvL2lwZnMvUW1Xb21BUDhqSkxkNzNaTHlKbXJkSnFGQmhza1VySnkyVnBNR1JlTkJyMWdkR7Zis6PfVu5kKJvAYfPesL55QoFrqaygLpsk7BxbgrgPn0KsXE7lzrgqj1eysgV3XZ83iXIWTx4Xs10C3UjSCdTMg2jM0J1UQPhbqZQaX5IJNYhozv/ivJDyAN6mZt0QGw==","Program 6EF8rrecthR5Dkzon8Nwu78hRvfCKubJ14M5uBEwF6P consumed 121133 of 178931 compute units","Program 6EF8rrecthR5Dkzon8Nwu78hRvfCKubJ14M5uBEwF6P success","Program ATokenGPvbdGVxr1b2hvZbsiqW5xWH25efTNsLJA8knL invoke [1]","Program log: CreateIdempotent","Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA invoke [2]","Program log: Instruction: GetAccountDataSize","Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA consumed 1569 of 50897 compute units","Program return: TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA pQAAAAAAAAA=","Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA success","Program 11111111111111111111111111111111 invoke [2]","Program 11111111111111111111111111111111 success","Program log: Initialize the associated token account","Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA invoke [2]","Program log: Instruction: InitializeImmutableOwner","Program log: Please upgrade to SPL Token 2022 for immutable owner support","Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA consumed 1405 of 44310 compute units","Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA success","Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA invoke [2]","Program log: Instruction: InitializeAccount3","Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA consumed 4188 of 40430 compute units","Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA success","Program ATokenGPvbdGVxr1b2hvZbsiqW5xWH25efTNsLJA8knL consumed 21839 of 57798 compute units","Program ATokenGPvbdGVxr1b2hvZbsiqW5xWH25efTNsLJA8knL success","Program 6EF8rrecthR5Dkzon8Nwu78hRvfCKubJ14M5uBEwF6P invoke [1]","Program log: Instruction: Buy","Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA invoke [2]","Program log: Instruction: Transfer","Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA consumed 4645 of 15816 compute units","Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA success","Program 11111111111111111111111111111111 invoke [2]","Program 11111111111111111111111111111111 success","Program 11111111111111111111111111111111 invoke [2]","Program 11111111111111111111111111111111 success","Program 6EF8rrecthR5Dkzon8Nwu78hRvfCKubJ14M5uBEwF6P invoke [2]","Program 6EF8rrecthR5Dkzon8Nwu78hRvfCKubJ14M5uBEwF6P consumed 2003 of 3728 compute units","Program 6EF8rrecthR5Dkzon8Nwu78hRvfCKubJ14M5uBEwF6P success","Program data: vdt/007mYe62YrOj31buZCibwGHz3rC+eUKBa6msoC6bJOwcW4K4DwAYDY8AAAAACU14uUlIAAABzINozNCdVED4W6mUGl+SCTWIaM7/4ryQ8gDepmbdEBvhBW9nAAAAAADEMIsHAAAA98JfjpmHAwAAGA2PAAAAAPcqTUIIiQIA","Program 6EF8rrecthR5Dkzon8Nwu78hRvfCKubJ14M5uBEwF6P consumed 35959 of 35959 compute units","Program 6EF8rrecthR5Dkzon8Nwu78hRvfCKubJ14M5uBEwF6P success"],"signature":"3PDrzPNDBXNZDJyGvQGRhppZW16rHVUZi5mNjJs2aELydBiYLxtG7WD3JS5skFCGv36WseLzDib1KwayUZpgP3mC"}},"subscription":887487}}
        """.trimIndent()

        val encode = Json.decodeFromString<LogsSubscribeResponse>(response)

        val transactionFactory = transactionFactory(encode)

        println(Json.encodeToString(transactionFactory))
    }
}