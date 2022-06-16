package kr.icclab.kyptowallet
import android.app.Application
import android.content.Context
import kr.icclab.kyptowallet.di.NetworkModule
import kr.icclab.kyptowallet.network.EtherScanService
import okhttp3.OkHttpClient
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService

class MyApp : Application() {

    init {
        instance = this
    }
    val RPC_Server = "https://ropsten.infura.io/v3/7fc074a7f7ec42a7a371259cb039ca69"

    companion object {
        private var instance: MyApp? = null
        lateinit var prefs: PreferenceUtil
        lateinit var web3j : Web3j
        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
        var okhttp :OkHttpClient? = null
        var etherScanService : EtherScanService? = null
    }

    override fun onCreate() {
        super.onCreate()
        // initialize for any

        // Use ApplicationContext.
        // example: SharedPreferences etc...
        val context: Context = MyApp.applicationContext()
        prefs = PreferenceUtil(context)
        web3j = Web3j.build(HttpService(RPC_Server))
        okhttp = NetworkModule().okHttpClient()
        etherScanService = NetworkModule().getEtherScanService(okhttp!!)


//
//        val intent = Intent(this, LoginActivity::class.java)
//
//        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
//    	try {
//            pendingIntent.send()
//        }catch (e: CancellationException){
//            return
//        }

    }
}