package kr.icclab.kyptowallet

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import cash.z.ecc.android.bip39.Mnemonics
import cash.z.ecc.android.bip39.Mnemonics.MnemonicCode
import cash.z.ecc.android.bip39.toSeed
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import org.web3j.crypto.*
import org.web3j.protocol.Web3j
import org.web3j.protocol.Web3jService
import org.web3j.protocol.admin.Admin
import org.web3j.protocol.core.DefaultBlockParameter
import org.web3j.protocol.core.Ethereum
import org.web3j.protocol.core.RemoteCall
import org.web3j.protocol.core.methods.response.*
import org.web3j.protocol.http.HttpService
import org.web3j.tx.Transfer
import org.web3j.utils.Convert
import java.math.BigDecimal
import java.math.BigInteger
import java.security.InvalidAlgorithmParameterException
import java.security.NoSuchAlgorithmException
import java.security.NoSuchProviderException


class MainActivity : AppCompatActivity() {
//    val okHttpClient = OkHttpClient.Builder()
//                        .connectTimeout(100, TimeUnit.SECONDS)
//                        .readTimeout(100, TimeUnit.SECONDS)
//                        .writeTimeout(100, TimeUnit.SECONDS)
//                        .build()

//    var RPC_Server = "http://192.168.1.5:7545/"
var RPC_Server = "https://ropsten.infura.io/v3/7fc074a7f7ec42a7a371259cb039ca69"
    var web3j : Web3j = Web3j.build(HttpService())

    val temp_PRIKey :String = "7179ab4acf2f0b3511beb1be1137587e"
    val temp_PUBKey : String = "ec4514c866471a5d1ab5e240f7469344aa811236f708b1bbf732060b02677082f2c41d7990960e346e58d664b4cce456b72c8bd3725a612751c285ffce4c27a8"
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

        var account =web3j.ethAccounts().sendAsync().get()
        Log.e("ACCOUNT",account.accounts.toString())


//        WalletUtils.gener
//        web3j.ethAccounts().jsonr
        sendBalance.setOnClickListener {
//            logRender(getGas()?.gasPrice.toString())
            var crend : Credentials = Credentials.create(temp_PRIKey,temp_PUBKey)
            sendEth(crend,address_EditText.text.toString(),0.3,0.0f)



        }

        getBalance_Button.setOnClickListener {

            var getB = getEthBalance(address_EditText.text.toString())
            if (getB != null) {
                logRender(getB.balance.toString())
            }

//            logRender(WalletUtils.isValidPrivateKey(address_EditText.text.toString()).toString())
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


//            val createWallet = Wallet.createLight(password,eck)
//            val walletFile : WalletFile = Wallet.createLight(password,eck)
            Log.e("JSON",process(eck,password).toString())
//            val creden : Credentials = Credentials.create(eck)
//            val k = ECKeyPair.create(BigInteger(creden.ecKeyPair.privateKey.toString()))
//            k.privateKey.toString()
//            val test = Credentials.create(k)

//
//            Log.e("Wallet",k.privateKey.toString());
//            Log.e("Wallet",k.privateKey.toString(16));


//            logRender(createWallet.address.toString())

//            logRender(createWallet.address.toString())
//            logRender(eck.privateKey.toString(16))
//            logRender(eck.publicKey.toString(16))
//
//            Log.e("Wallet",createWallet.address.toString());
//            Log.e("Wallet",eck.privateKey.toString(16));

//            logRender(creden.ecKeyPair.publicKey.toString())

//            Log.d("Key",createWallet.address.toString())
//            Log.d("CredenKey",creden.address.toString())
//            Log.d("CredenPrivateKey",creden.ecKeyPair.privateKey.toString(16))
//            Log.d("CredenPublicKey",creden.ecKeyPair.publicKey.toString(16))
//            Log.d("HashKey",hashCode().toString() )
        //            logRender(eck.privateKey.toString())
//            val create = Wallet.create(password,eck ,2,1)
//            logRender(create.address.toString())
//            ECKeyPair.create(ByteArray)
        }



        //MnemonicCode Start



    }

    private fun process(ecKeyPair: ECKeyPair,password:String): JSONObject? {
        val processJson = JSONObject()
        try {
//            val ecKeyPair = Keys.createEcKeyPair()
            val privateKeyInDec = ecKeyPair.privateKey
            val sPrivatekeyInHex = privateKeyInDec.toString(16)
            val aWallet = Wallet.createLight(password, ecKeyPair)
            val sAddress = aWallet.address
//            Sign.SignatureData
            processJson.put("address", "0x$sAddress")
            processJson.put("privatekey", sPrivatekeyInHex)
            processJson.put("publickey", ecKeyPair.publicKey.toString(16))

        } catch (e: CipherException) {
            //
        } catch (e: InvalidAlgorithmParameterException) {
            //
        } catch (e: NoSuchAlgorithmException) {
            //
        } catch (e: NoSuchProviderException) {
            //
        }
        return processJson
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
//        WalletUtils.
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

    fun getBlockNumber(): EthBlockNumber {
        var result: EthBlockNumber = EthBlockNumber()
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