package kr.icclab.kyptowallet

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_create_wallet.*
import kotlinx.android.synthetic.main.fragment_send_ether.*
import org.json.JSONObject
import org.web3j.abi.Utils
import org.web3j.crypto.Credentials
import org.web3j.crypto.WalletUtils
import org.web3j.protocol.Web3j
import org.web3j.protocol.Web3jService
import org.web3j.protocol.core.DefaultBlockParameter
import org.web3j.protocol.core.RemoteCall
import org.web3j.protocol.core.methods.response.EthGasPrice
import org.web3j.protocol.core.methods.response.EthGetBalance
import org.web3j.protocol.core.methods.response.TransactionReceipt
import org.web3j.tx.Transfer
import org.web3j.utils.Convert
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [send_ether.newInstance] factory method to
 * create an instance of this fragment.
 */
class send_ether : Fragment() {
    var web3j: Web3j? = null
    private var sendKey :String  = ""
    private var crend : Credentials? = null
    private var walletJson: JSONObject? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        walletJson = MyApp.prefs.getJson("wallet", JSONObject("{}"))
        crend = Credentials.create(walletJson!!.getString("privatekey"),walletJson!!.getString("publickey"))

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_send_ether, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sendEtherButton.setOnClickListener(mClickListener)
        cancelButton.setOnClickListener(mClickListener)

        val ethGasPrice  = MyApp.web3j!!.ethGasPrice().sendAsync().get().gasPrice

//        var etherGas = Convert.fromWei(ethGasPrice.toString(), Convert.Unit.ETHER)

//        gasTextView.text = "GAS 금액 (${etherGas})"

        Log.e("GAS",ethGasPrice.toString())

        Log.e("GAS", Convert.fromWei(ethGasPrice.toString(), Convert.Unit.ETHER).toString())
        sendAddressEditText.addTextChangedListener(addressTextChangedListener)

//        sendEtherEditText.addTextChangedListener()

    }

    val addressTextChangedListener = object : TextWatcher{
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }
        override fun afterTextChanged(p0: Editable?) {
            if(WalletUtils.isValidAddress(p0.toString())){
                addressTextView.text = "보내실 주소 (주소 확인)"
                addressTextView.setTextColor(ContextCompat.getColor(activity!!,R.color.textcolor))
                sendKey = p0.toString()
            }else{
                addressTextView.text = "보내실 주소 (주소 불량)"
                addressTextView.setTextColor(Color.RED)
            }
        }
    }


    val mClickListener :View.OnClickListener = object : View.OnClickListener{
        override fun onClick(v: View?) {
            when(v?.id){
                R.id.sendEtherButton ->{
//            logRender(getGas()?.gasPrice.toString())
                    Log.e("test",sendEth(crend!!,sendKey,0.1,0.01f).toString())
                    (activity as MainActivity).closeFragment()

                }
                R.id.cancelButton->{

                    (activity as MainActivity).closeFragment()

                }
            }
        }
    }

    fun getGas(): EthGasPrice? {
        val result = MyApp.web3j.ethGasPrice()
            .sendAsync()
            .get()
        return result
    }

    fun sendEth(
            credentials: Credentials,
            withKey: String,
            withEth: Double,
            gas: Float
        ): RemoteCall<TransactionReceipt>? {
            val result = Transfer.sendFunds(
                MyApp.web3j,
                credentials,
                withKey,
                BigDecimal.valueOf(withEth),
                Convert.Unit.ETHER
            )
            result.sendAsync()
            return result
    }
//    fun getGas(): EthGasPrice? {
//        val result = EthGasPrice()
//            MyApp.web3j.ethGasPrice().sendAsync().get()
//
//        return result
//    }

}