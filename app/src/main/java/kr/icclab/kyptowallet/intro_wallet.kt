package kr.icclab.kyptowallet

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_create_wallet.*
import kotlinx.android.synthetic.main.fragment_create_wallet.createWalletButton
import kotlinx.android.synthetic.main.fragment_create_wallet.passEditText
import kotlinx.android.synthetic.main.fragment_intro_wallet.*
import kotlinx.android.synthetic.main.fragment_intro_wallet.view.*
import kotlinx.android.synthetic.main.fragment_login_wallet.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [intro_wallet.newInstance] factory method to
 * create an instance of this fragment.
 */
class intro_wallet : Fragment() {
    // TODO: Rename and change types of parameters


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_intro_wallet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createWalletButton.setOnClickListener (mClickListener)
        resetWalletButton.setOnClickListener (mClickListener)

//        Glide.with(this).load(R.raw.ethermove).override(700, 700).into(view.introLogoImage)

    }

    val mClickListener :View.OnClickListener = object : View.OnClickListener{
        override fun onClick(v: View?) {
            when(v?.id){
                R.id.createWalletButton->{
                    val transaction = activity!!.supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.view, create_wallet())
                    transaction.disallowAddToBackStack()
                    transaction.commit()
                }
                R.id.resetWalletButton->{
                    val transaction = activity!!.supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.view, reset_wallet())
                    transaction.disallowAddToBackStack()
                    transaction.commit()
                }
            }
        }
    }

}