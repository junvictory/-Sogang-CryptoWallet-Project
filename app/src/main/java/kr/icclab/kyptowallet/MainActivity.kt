package kr.icclab.kyptowallet

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_create_wallet.*
import kr.icclab.kyptowallet.network.EtherScanService
import kr.icclab.kyptowallet.network.models.SearchResponseDto
import kr.icclab.kyptowallet.transactionRecycler.ReCyclerUserAdapter
import kr.icclab.kyptowallet.transactionRecycler.UiTransaction
import org.json.JSONObject
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameter
import org.web3j.protocol.core.methods.response.EthAccounts
import org.web3j.protocol.core.methods.response.EthBlockNumber
import org.web3j.protocol.core.methods.response.EthGetBalance
import org.web3j.protocol.core.methods.response.EthGetTransactionCount
import org.web3j.utils.Convert
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigInteger


class MainActivity : AppCompatActivity() {
//    val okHttpClient = OkHttpClient.Builder()
//                        .connectTimeout(100, TimeUnit.SECONDS)
//                        .readTimeout(100, TimeUnit.SECONDS)
//                        .writeTimeout(100, TimeUnit.SECONDS)
//                        .build()

    var web3j: Web3j? = null
    var walletJson: JSONObject? = null

    private var etherScanService : EtherScanService? = null


    var runningFragment : Fragment? = null
    private var myAddress :String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        scope.launch {
//            getTransactionEtherScan()
//        }
        walletJson = MyApp.prefs.getJson("wallet", JSONObject("{}"))

        myAddress = walletJson!!.get("address").toString()
        etherScanService = MyApp.etherScanService
        loadData()



        refreshButton.setOnClickListener{

            loadData()
        }


        shareButton.setOnClickListener {
            runningFragment = share_address()

            supportFragmentManager.beginTransaction()
                .replace(R.id.view, runningFragment as share_address)
                .commit()

        }
        sendButton.setOnClickListener {
            runningFragment = send_ether()
            supportFragmentManager.beginTransaction()
                .replace(R.id.view, runningFragment as send_ether)
                .commit()
        }



        outButton.setOnClickListener{
            Util.Util.CopyClipboard(applicationContext,walletJson!!.get("address").toString())
//            MyApp.prefs.clear()
//
//            val i = baseContext.packageManager
//                .getLaunchIntentForPackage(baseContext.packageName)
//            i!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//            finish()
//            startActivity(i)
        }


//        val ethGasPrice = MyApp.web3j.ethGasPrice().sendAsync().get()
//        val wei: BigInteger = ethGasPrice.gasPrice
//        val tokenValue = Convert.fromWei(wei.toString(), Convert.Unit.ETHER)
//        val gas = tokenValue.toString()
//        Log.d("GAS",ethGasPrice.gasPrice.toString())




    }

    fun loadData(){
        var getB = MyApp.getEthBalance(walletJson!!.get("address").toString())
        if (getB != null) {
            val wei: BigInteger = getB!!.balance
            val tokenValue = Convert.fromWei(wei.toString(), Convert.Unit.ETHER)
            val strTokenAmount = tokenValue.toString()

            walletBalTextView.text = String.format("%.3f", tokenValue) + " ETH"
        }


        etherScanService!!.getTransactionsForAddress(myAddress).enqueue(object : Callback<SearchResponseDto> {
            override fun onResponse(
                call: Call<SearchResponseDto>,
                response: Response<SearchResponseDto>
            ) {
                Log.e("EtherScanData", "etherscan : ${response.body().toString()}")
                val list = ArrayList<UiTransaction>()

                for(item in response.body()!!.transactions!!){
                    val etherValue = Convert.fromWei(item.value.toString(), Convert.Unit.ETHER)
                    var inout = true //in : true , out : false
                    if(item.from != myAddress){
                        inout = false
                    }
                    list.add(UiTransaction( etherValue.toString() , item.hash.toString(),inout))
                }

                val adapter = ReCyclerUserAdapter(list)
                transactions_recycler_view.adapter = adapter
            }
            override fun onFailure(call: Call<SearchResponseDto>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    fun closeFragment(){
        Log.e("Call", "Call")
//        supportFragmentManager.popBackStack()

        supportFragmentManager.beginTransaction().remove(runningFragment!!).commit()
        runningFragment = null
    }

//fun getTransactionEtherScan(){
//    etherScanService = MyApp.etherScanService
//
//    walletJson = MyApp.prefs.getJson("wallet", JSONObject("{}"))
//
//    etherScanService!!.getTransactionsForAddress(key).enqueue(object : Callback<SearchResponseDto> {
//        override fun onResponse(
//            call: Call<SearchResponseDto>,
//            response: Response<SearchResponseDto>
//        ) {
//            Log.e("EtherScanData", "etherscan : ${response.body().toString()}")
//
//        }
//
//        override fun onFailure(call: Call<SearchResponseDto>, t: Throwable) {
//            TODO("Not yet implemented")
//        }
//    })
////    val adapter = TransactionAdapter()
////    transactions_recycler_view.adapter = adapter
//
//}

    fun getTransactionCount(str: String): EthGetTransactionCount? {
        var result: EthGetTransactionCount? = EthGetTransactionCount()
        result = web3j!!.ethGetTransactionCount(
            str.toString(),
            DefaultBlockParameter.valueOf("latest")
        )
            .sendAsync()
            .get()
        return result
    }

    fun getBlockNumber(): EthBlockNumber {
        var result: EthBlockNumber = EthBlockNumber()
        result = web3j!!.ethBlockNumber()
            .sendAsync()
            .get()
        return result
    }

    fun getEthAccounts(): EthAccounts? {

        var result: EthAccounts? = EthAccounts()
        result = web3j!!.ethAccounts()
            .sendAsync()
            .get()
        return result
    }

    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount == 0) {
            var tempTime = System.currentTimeMillis();
            var intervalTime = tempTime - MyApp.backPressedTime;
            if (0 <= intervalTime && MyApp.FINISH_INTERVAL_TIME >= intervalTime) {
                super.onBackPressed();
            } else {
                MyApp.backPressedTime = tempTime;
                Toast.makeText(this, "'뒤로' 버튼을 한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
                return
            }
        }
        super.onBackPressed();
    }


}

