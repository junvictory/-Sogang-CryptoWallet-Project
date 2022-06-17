package kr.icclab.kyptowallet

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import cash.z.ecc.android.bip39.Mnemonics
import kotlinx.android.synthetic.main.fragment_create_wallet.*
import kotlinx.android.synthetic.main.fragment_create_wallet.passCheckEditText
import kotlinx.android.synthetic.main.fragment_create_wallet.passEditText
import kotlinx.android.synthetic.main.fragment_create_wallet.view.*
import kotlinx.android.synthetic.main.fragment_reset_wallet.*
import kotlinx.android.synthetic.main.fragment_reset_wallet.view.*
import org.json.JSONObject
import org.web3j.crypto.CipherException
import org.web3j.crypto.ECKeyPair
import org.web3j.crypto.Wallet
import java.security.InvalidAlgorithmParameterException
import java.security.NoSuchAlgorithmException
import java.security.NoSuchProviderException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private var mnemonicStr = ""
private lateinit var seed : ByteArray


/**
 * A simple [Fragment] subclass.
 * Use the [reset_wallet.newInstance] factory method to
 * create an instance of this fragment.
 */
class reset_wallet : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var mnemonicStr = ""
    private lateinit var seed : ByteArray
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reset_wallet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.resetWalletButton.setOnClickListener(mClickListener)
        view.outResetWalletTextView.setOnClickListener(mClickListener)

    }


    val mClickListener :View.OnClickListener = object : View.OnClickListener{
        override fun onClick(v: View?) {
            when(v?.id){
                R.id.resetWalletButton -> {
                    if(!PasswordIsValid()){
                        Toast.makeText(view!!.context, "비밀번호를 확인해 주세요", Toast.LENGTH_SHORT).show()
                    }else{
                        seed = Mnemonics.MnemonicCode(nmemonicEditText.text.toString()).toEntropy()
                        createWallet(seed,passEditText.text.toString())
                    }
                }
                R.id.outResetWalletTextView->{
                    var targetFrag : Fragment = login_wallet()

                    if(!MyApp.prefs.getBoolean("check",false)){
                        targetFrag = intro_wallet()
                    }

                    val transaction = activity!!.supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.view, targetFrag)
                    transaction.disallowAddToBackStack()
                    transaction.commit()
                }

            }
        }
    }

    fun createWallet(seed: ByteArray,password:String){
        val eck: ECKeyPair = ECKeyPair.create(seed)
        MyApp.prefs.setJson("wallet",createWalletProcess(eck,password))
        MyApp.prefs.setBoolean("check",true)
        MyApp.prefs.setString("password",password)

        Log.e("save",createWalletProcess(eck,password).toString())
        Toast.makeText(context, "재설정 완료!", Toast.LENGTH_SHORT).show()

        (activity as LoginActivity).MainLoad()

    }




    private fun createWalletProcess(ecKeyPair: ECKeyPair, password:String): JSONObject {
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

    fun PasswordIsValid():Boolean{
        var res: Boolean = false
        if(!TextUtils.isEmpty(passEditText.text)&&!TextUtils.isEmpty(passCheckEditText.text)&&(passEditText.text.toString() == passCheckEditText.text.toString())){
            res = true
        }
        return res
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment reset_wallet.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            reset_wallet().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}