package kr.icclab.kyptowallet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameter
import org.web3j.protocol.core.methods.response.EthGasPrice
import org.web3j.protocol.core.methods.response.EthGetBalance
import org.web3j.protocol.http.HttpService
import java.math.BigInteger
import org.web3j.protocol.core.methods.response.EthGetTransactionCount
import org.web3j.protocol.core.methods.response.EthBlockNumber
import org.web3j.protocol.core.methods.response.EthAccounts



class MainActivity : AppCompatActivity() {
//    val okHttpClient = OkHttpClient.Builder()
//                        .connectTimeout(100, TimeUnit.SECONDS)
//                        .readTimeout(100, TimeUnit.SECONDS)
//                        .writeTimeout(100, TimeUnit.SECONDS)
//                        .build()

    var RPC_Server = "http://192.168.1.5:7545/"
    var web3j : Web3j = Web3j.build(HttpService())



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        web3j  = Web3j.build(HttpService(RPC_Server))



        var versionWeb3j = web3j.web3ClientVersion().sendAsync().get()
        System.out.println(versionWeb3j.web3ClientVersion.toString())



        getGas_Button.setOnClickListener {
            logRender(getGas()?.gasPrice.toString())
        }
        getBalance_Button.setOnClickListener {
            var getB = getEthBalance(address_EditText.text.toString())

            if (getB != null) {
                logRender(getB.balance.toString())
            }
        }

        val ethGasPrice = web3j.ethGasPrice().sendAsync().get()
        textView.text = ethGasPrice.gasPrice.toString()


    }


    fun getGas() : EthGasPrice?{
        val result = EthGasPrice()
        web3j.ethGasPrice().sendAsync().get()
        return result
    }

    fun getEthBalance(str: String): EthGetBalance? {
        val result = EthGetBalance()
        web3j.ethGetBalance(
            str.toString(),
            DefaultBlockParameter.valueOf("latest")
        )
            .sendAsync()
            .get()

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