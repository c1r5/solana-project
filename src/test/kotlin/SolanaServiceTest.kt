import dev.cire.solana.connection.Rpc
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Test

class SolanaServiceTest {

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
        val response = Rpc.getTransaction(
            signature = "24GP1s6GFGsXFto7CS2LkjpMW6VQQvqTt1zVQD4ndaF8dW5ot2rEUs2xc4NqJnp45nLZqVDKxLqcUcXziKd8D6oJ"
        ).first()

        assert(response.result != null)
    }
}