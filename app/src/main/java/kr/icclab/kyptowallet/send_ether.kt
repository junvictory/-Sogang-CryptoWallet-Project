package kr.icclab.kyptowallet

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_create_wallet.*
import kotlinx.android.synthetic.main.fragment_send_ether.*
import org.json.JSONObject
import org.web3j.crypto.Credentials
import org.web3j.crypto.WalletUtils
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.RemoteCall
import org.web3j.protocol.core.methods.response.EthGasPrice
import org.web3j.protocol.core.methods.response.TransactionReceipt
import org.web3j.tx.TransactionManager
import org.web3j.tx.Transfer
import org.web3j.utils.Convert
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*
import java.util.concurrent.Callable
import kotlin.time.measureTime

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
    //Check
    private var desAddressValid : Boolean = false
    private var desEthValid : Boolean = false
    private var desAddress : String = ""
    private var desEth : BigDecimal = BigDecimal.ZERO


    var myEth: BigDecimal = BigDecimal.ZERO
    var nowGas : BigDecimal = BigDecimal.ZERO
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
        Log.e("GAS1",ethGasPrice.toString())
        Log.e("GAS2", Convert.fromWei(ethGasPrice.toString(), Convert.Unit.GETHER).toString())

        sendAddressEditText.addTextChangedListener(addressTextChangedListener)

        var getB = MyApp.getEthBalance(walletJson!!.get("address").toString())
        if (getB != null) {
            val wei: BigInteger = getB!!.balance
            myEth = Convert.fromWei(wei.toString(), Convert.Unit.ETHER)
            sendEtherTextView.text = "금액 잔액("+String.format("%.8f", myEth) + " ETH)"
        }
        nowGas =Convert.fromWei(ethGasPrice.toString(), Convert.Unit.ETHER)

        gasPriceTextView.text = String.format("%.10f", nowGas) + " ETH"
//        Log.e("GAS3", )

        sendEtherEditText.addTextChangedListener(etherTextChangedListener)


    }

    val addressTextChangedListener = object : TextWatcher{
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }
        override fun afterTextChanged(p0: Editable?) {
            if(p0!!.isEmpty()) {
                return
            }
            if(WalletUtils.isValidAddress(p0.toString())){
                addressTextView.text = "보내실 주소 (주소 확인)"
                addressTextView.setTextColor(ContextCompat.getColor(activity!!,R.color.textcolor))
                desAddress = p0.toString()
                desAddressValid = true
            }else{
                addressTextView.text = "보내실 주소 (주소 불량)"
                addressTextView.setTextColor(Color.RED)
                desAddressValid = false
            }
        }
    }
    val etherTextChangedListener = object : TextWatcher{
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }
        override fun afterTextChanged(p0: Editable?) {
            if(p0!!.isEmpty()){
                sendEtherTextView.text = "금액 잔액("+String.format("%.8f", myEth) + " ETH)"
                sendEtherTextView.setTextColor(ContextCompat.getColor(activity!!,R.color.textcolor))
                desEthValid = false
                return
            }
            var targetSendEth = p0.toString().toDouble()
            var calSendEth = myEth.toDouble() - targetSendEth
            if(calSendEth <0){
                sendEtherTextView.text = "잔액 부족(-"+String.format("%.8f", calSendEth) + " ETH)"
                sendEtherTextView.setTextColor(Color.RED)
                desEthValid = false
            }else{
                sendEtherTextView.text = "남는 잔액("+String.format("%.8f", calSendEth) + " ETH)"
                sendEtherTextView.setTextColor(ContextCompat.getColor(activity!!,R.color.textcolor))
                desEthValid = true
                desEth = BigDecimal.valueOf(targetSendEth)
            }
        }
    }


    val mClickListener :View.OnClickListener = object : View.OnClickListener{
        override fun onClick(v: View?) {
            when(v?.id){
                R.id.sendEtherButton ->{
//            logRender(getGas()?.gasPrice.toString())
                    if(desAddressValid && desEthValid){
                        val builder = AlertDialog.Builder(activity)
                        builder.setTitle("코인 보내기")
                            .setMessage(String.format("Address: %s\n 보내는: %.10f ETH\n합계: %.10f ETH 보내시겠습니까?\n",desAddress,desEth,desEth+nowGas))
                            .setPositiveButton("확인",
                                DialogInterface.OnClickListener { dialog, id ->
                                    sendEth(crend!!,desAddress,desEth.toDouble(),0.01f)
                                    (activity as MainActivity).closeFragment()
                                })
                            .setNegativeButton("거부",
                                DialogInterface.OnClickListener { dialog, id ->

                                })
                        builder.show()
                    }





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



//    fun sendEth(
//            credentials: Credentials,
//            withKey: String,
//            withEth: Double,
//            gas: Float
//        ): RemoteCall<TransactionReceipt>? {
//            val result = Transfer.sendFunds(
//                MyApp.web3j,
//                credentials,
//                withKey,
//                BigDecimal.valueOf(withEth),
//                Convert.Unit.ETHER
//            )
//            result.sendAsync()
//            return result
//    }
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