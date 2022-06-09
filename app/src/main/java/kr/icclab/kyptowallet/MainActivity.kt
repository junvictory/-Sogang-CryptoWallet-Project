package kr.icclab.kyptowallet

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import cash.z.ecc.android.bip39.Mnemonics
import cash.z.ecc.android.bip39.Mnemonics.MnemonicCode
import kotlinx.android.synthetic.main.activity_main.*
import org.web3j.crypto.Credentials
import org.web3j.crypto.ECKeyPair
import org.web3j.crypto.Wallet
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameter
import org.web3j.protocol.core.RemoteCall
import org.web3j.protocol.core.methods.response.*
import org.web3j.protocol.http.HttpService
import org.web3j.tx.Transfer
import org.web3j.utils.Convert
import java.math.BigDecimal


class MainActivity : AppCompatActivity() {
//    val okHttpClient = OkHttpClient.Builder()
//                        .connectTimeout(100, TimeUnit.SECONDS)
//                        .readTimeout(100, TimeUnit.SECONDS)
//                        .writeTimeout(100, TimeUnit.SECONDS)
//                        .build()

    var RPC_Server = "http://163.239.24.30:7545/"
    var web3j : Web3j = Web3j.build(HttpService())
    val temp_PRIKey :String = "ea18953de81a4c78a0351b73734650040e3e3b12"
    val temp_PUBKey : String = "43c11d18292d63f4b13e96dad70d37373e32bc683b715985a5c15e419791d486bb500309ee63643b73e33443b2e45c4a76fa1850e7bb9d0ddb2b8b86930efd56"
//    * CPU/Memory cost parameter. Must be larger than 1, a power of 2 and less than 2^(128 * r / 8).
//    */
//    private static final int N = 1 << 9;
//    /**
//     * Parallelization parameter. Must be a positive integer less than or equal to Integer.MAX_VALUE / (128 * r * 8).
//     */
//    private static final int P = 1;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        web3j  = Web3j.build(HttpService(RPC_Server))

        log_TextView.setMovementMethod(ScrollingMovementMethod())

        var versionWeb3j = web3j.web3ClientVersion().sendAsync().get()
        System.out.println(versionWeb3j.web3ClientVersion.toString())

//        WalletUtils.gener
//        web3j.ethAccounts().jsonr
        getGas_Button.setOnClickListener {
//            logRender(getGas()?.gasPrice.toString())
            var crend : Credentials = Credentials.create(temp_PRIKey,temp_PUBKey.toString())
            sendEth(crend,address_EditText.text.toString(),1.0,0.0f)
        }
        getBalance_Button.setOnClickListener {

            var getB = getEthBalance(address_EditText.text.toString())
            if (getB != null) {
//                logRender(getB.balance.toString())
            }
        }

        val ethGasPrice = web3j.ethGasPrice().sendAsync().get()
        textView.text = ethGasPrice.gasPrice.toString()


        //create(String password, ECKeyPair ecKeyPair, int n, int p)
        //Wallet Create
        val password = "password"
        createWallet_Button.setOnClickListener {
            val mnemonicCode: MnemonicCode = MnemonicCode(Mnemonics.WordCount.COUNT_12)


            val seed: ByteArray = mnemonicCode.toEntropy()
            for (word in mnemonicCode) {
                logRender(word.toString()+"\n")
            }
            val eck: ECKeyPair = ECKeyPair.create(seed)

            val createWallet = Wallet.createLight(password,eck)
            val creden : Credentials = Credentials.create(createWallet.address)


            logRender(createWallet.address.toString())
            logRender(creden.address.toString())
            logRender(creden.ecKeyPair.privateKey.toString())
            logRender(creden.ecKeyPair.publicKey.toString())

            Log.d("Key",createWallet.address.toString())
            Log.d("CredenKey",creden.address.toString())
            Log.d("CredenPrivateKey",creden.ecKeyPair.privateKey.toString(16))
            Log.d("CredenPublicKey",creden.ecKeyPair.publicKey.toString(16))
            Log.d("HashKey",hashCode().toString() )
        //            logRender(eck.privateKey.toString())
//            val create = Wallet.create(password,eck ,2,1)
//            logRender(create.address.toString())
//            ECKeyPair.create(ByteArray)
        }



        //MnemonicCode Start



    }

    //(Web3j web3j,
    //                                                       org.web3j.crypto.Credentials credentials,
    //                                                       java.lang.String toAddress,
    //                                                       java.math.BigDecimal value,
    //                                                       org.web3j.utils.Convert.Unit uni)
    override fun hashCode(): Int {
        var result = if (temp_PRIKey != null) temp_PRIKey.hashCode() else 0
        result = 31 * result + if (temp_PUBKey != null) temp_PUBKey.hashCode() else 0
        return result
    }


    fun sendEth(credentials: Credentials,withKey: String,withEth: Double, gas : Float) : RemoteCall<TransactionReceipt>? {
        val result = Transfer.sendFunds(web3j,credentials,withKey, BigDecimal.valueOf(withEth),Convert.Unit.ETHER)
        result.sendAsync()
        return result
    }
    fun getGas() : EthGasPrice?{
        val result = EthGasPrice()
        web3j.ethGasPrice().sendAsync().get()
        return result
    }

    fun getEthBalance(str: String): EthGetBalance? {
        val result = web3j.ethGetBalance(
            str,
            DefaultBlockParameter.valueOf("latest")
        )
            .sendAsync()
            .get()

//        val balance: BigInteger = result.balance


        Log.d("balance", result.balance.toString())

        return result
    }

    fun getTransactionCount(str: String): EthGetTransactionCount? {
        var result: EthGetTransactionCount? = EthGetTransactionCount()
        result = web3j.ethGetTransactionCount(
            str.toString(),
            DefaultBlockParameter.valueOf("latest")
        )
            .sendAsync()
            .get()
        return result
    }

    fun getBlockNumber(): EthBlockNumber? {
        var result: EthBlockNumber? = EthBlockNumber()
        result = web3j.ethBlockNumber()
            .sendAsync()
            .get()
        return result
    }

    fun getEthAccounts(): EthAccounts? {

        var result: EthAccounts? = EthAccounts()
        result = web3j.ethAccounts()
            .sendAsync()
            .get()
        return result
    }

    fun logRender(str : String){
        log_TextView.append(str)
    }

}