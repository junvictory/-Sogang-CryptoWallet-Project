package kr.icclab.kyptowallet

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import kotlinx.android.synthetic.main.fragment_item_view_fragment.*
import kr.icclab.kyptowallet.network.NetworkConstants

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [item_view_fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class item_view_fragment() : Fragment() {
    // TODO: Rename and change types of parameters
    private var hash: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            hash = it.getString("hash")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_item_view_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        item_WebView.webChromeClient = WebChromeClient()
        item_WebView.settings.loadWithOverviewMode = true  // WebView 화면크기에 맞추도록 설정 - setUseWideViewPort 와 같이 써야함
        item_WebView.settings.useWideViewPort = true  // wide viewport 설정 - setLoadWithOverviewMode 와 같이 써야함
        item_WebView.settings.builtInZoomControls = false
        item_WebView.settings.javaScriptEnabled = true
//        item_WebView.settings.javaScriptCanOpenWindowsAutomatically = true
        item_WebView.settings.domStorageEnabled = true
        Log.e("URL",NetworkConstants.etherscantx+hash)
        item_WebView.loadUrl(NetworkConstants.etherscantx+hash)

    }

    companion object {

        @JvmStatic
        fun newInstance(hash: String) =
            item_view_fragment().apply {
                arguments = Bundle().apply {
                    putString("hash", hash)
                }
            }
    }
}