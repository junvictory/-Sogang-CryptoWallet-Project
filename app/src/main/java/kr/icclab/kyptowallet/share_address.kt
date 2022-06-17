package kr.icclab.kyptowallet

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.android.synthetic.main.fragment_share_address.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
/**
 * A simple [Fragment] subclass.
 * Use the [share_address.newInstance] factory method to
 * create an instance of this fragment.
 */
class share_address : Fragment() {
    // TODO: Rename and change types of parameters
    private var key: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            key = it.getString("key")
        }

//        gasTextView.setText("dkssud")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_share_address, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addressTextView.text = key.toString()
        if (key != null) {
            val ImageQRcode = createQRcode(key.toString())
            img_qr.setImageBitmap(ImageQRcode)
        }
        closeShareButton.setOnClickListener {
            activity?.supportFragmentManager
                ?.beginTransaction()
                ?.remove(this)
                ?.commit()
        }
    }


    fun createQRcode(address: String?): Bitmap {
        val qrcodeEncoder = BarcodeEncoder()
        return qrcodeEncoder.encodeBitmap(address, BarcodeFormat.QR_CODE,512,512)

    }


    companion object {
        @JvmStatic
        fun newInstance(key: String) =
            share_address().apply {
                arguments = Bundle().apply {
                    putString("key", key)
                }
            }
    }
}