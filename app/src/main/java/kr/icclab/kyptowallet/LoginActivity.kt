package kr.icclab.kyptowallet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kr.icclab.kyptowallet.MyApp.Companion.web3j

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        var versionWeb3j = MyApp.web3j.web3ClientVersion().sendAsync().get()
        System.out.println(versionWeb3j.web3ClientVersion.toString())
        var account =web3j.ethAccounts().sendAsync().get()
        Log.e("ACCOUNT",account.accounts.toString())


        if(!MyApp.prefs.getBoolean("check",false)){
            supportFragmentManager.beginTransaction()
                .replace(R.id.view,intro_wallet())
                .commit()
        }else{
            supportFragmentManager.beginTransaction()
                .replace(R.id.view,login_wallet())
                .commit()
        }

    }
}