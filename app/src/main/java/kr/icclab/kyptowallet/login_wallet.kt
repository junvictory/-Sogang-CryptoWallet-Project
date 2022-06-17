package kr.icclab.kyptowallet

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_create_wallet.*
import kotlinx.android.synthetic.main.fragment_create_wallet.passEditText
import kotlinx.android.synthetic.main.fragment_login_wallet.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [login_wallet.newInstance] factory method to
 * create an instance of this fragment.
 */
class login_wallet : Fragment() {
    // TODO: Rename and change types of parameters

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_wallet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginWalletButton.setOnClickListener (mClickListener)
        forgotWalletTextView.setOnClickListener (mClickListener)
        removeWalletTextView.setOnClickListener(mClickListener)
    }



    val mClickListener :View.OnClickListener = object : View.OnClickListener{
        override fun onClick(v: View?) {
            when(v?.id){
                R.id.loginWalletButton ->{
                    if(passEditText.text.toString().equals(MyApp.prefs.getString("password","").toString())){
//                        val intent = Intent(getActivity(), MainActivity::class.java)
//                        startActivity(intent)
                        (activity as LoginActivity).MainLoad()
                    }else{
                        Toast.makeText(context, "비밀번호 오류.", Toast.LENGTH_SHORT).show()

                        Log.e("Login","PasswordFail")
                    }
                }
                R.id.forgotWalletTextView->{
                    val transaction = activity!!.supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.view, reset_wallet())
                    transaction.disallowAddToBackStack()
                    transaction.commit()
                }
                R.id.removeWalletTextView->{
                        val builder = AlertDialog.Builder(activity)
                        builder.setTitle("Krypto Wallet")
                            .setMessage("지갑을 지우시겠습니까?")
                            .setPositiveButton("확인",
                                DialogInterface.OnClickListener { dialog, id ->
                                        MyApp.prefs.clear()

                                        val transaction = activity!!.supportFragmentManager.beginTransaction()
                                        transaction.replace(R.id.view, intro_wallet())
                                        transaction.disallowAddToBackStack()
                                        transaction.commit()
                                })
                            .setNegativeButton("취소",
                                DialogInterface.OnClickListener { dialog, id ->
                                })
                        builder.show()
                }
            }
        }
    }

}