import dev.cire.data.WsNotification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json
import org.junit.Test

class SolanaServiceTest  {
    private val coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    @Test
    fun testContentSerialization() {
        val text = """
            {
               "jsonrpc":"2.0",
               "method":"programNotification",
               "params":{
                  "result":{
                     "context":{
                        "slot":308370512
                     },
                     "value":{
                        "account":{
                           "data":[
                              "F7f4N2DYrGC51jcK4kICAM11d8YLAAAAuT4lvlBEAQDNyVPKBAAAAACAxqR+jQMAAA==",
                              "base64"
                           ],
                           "executable":false,
                           "lamports":20575589885,
                           "owner":"6EF8rrecthR5Dkzon8Nwu78hRvfCKubJ14M5uBEwF6P",
                           "rentEpoch":18446744073709552000,
                           "space":49
                        },
                        "pubkey":"Hmd2s7SgPdmjqS9Z8w4DCHyomCnVRStZ6KE265rn7CDf"
                     }
                  },
                  "subscription":182184
               }
            }
        """.trimIndent()

        println(Json.decodeFromString<WsNotification>(text))
    }
}