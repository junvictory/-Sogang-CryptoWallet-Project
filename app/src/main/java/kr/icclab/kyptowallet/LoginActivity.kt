package kr.icclab.kyptowallet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
    fun MainLoad(){
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)

        finish()
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