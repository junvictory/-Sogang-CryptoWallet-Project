package kr.icclab.kyptowallet

import android.content.ActivityNotFoundException
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.link.LinkClient
import com.kakao.sdk.link.WebSharerClient
import com.kakao.sdk.template.model.*
import kotlinx.android.synthetic.main.fragment_share_address.*
import com.kakao.sdk.auth.*
import com.kakao.sdk.common.KakaoSdk
// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [share_address.newInstance] factory method to
 * create an instance of this fragment.
 */
class share_address : Fragment() {
    // TODO: Rename and change types of parameters
    private var key: String? = null
   // lateinit var mainActivity: MainActivity

//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        mainActivity = context as MainActivity
//
//    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        KakaoSdk.init(requireContext(),"430088ccec0aafb2395697de59e37d9c")
        arguments?.let {
            key = it.getString("key")
        }

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
        addressShareButton.setOnClickListener { kakaolink(requireContext())}
    }
    fun createQRcode(address: String?): Bitmap {
        val qrcodeEncoder = BarcodeEncoder()
        return qrcodeEncoder.encodeBitmap(address, BarcodeFormat.QR_CODE,512,512)

    }

    fun kakaolink(context: Context) {

        val defaultFeed = FeedTemplate(
            content = Content(
                title = "Kryoto 지갑 주소",
                description = key,
                imageUrl = "https://mud-kage.kakao.com/dn/Q2iNx/btqgeRgV54P/VLdBs9cvyn8BJXB3o7N8UK/kakaolink40_original.png",
                link = Link(
                    webUrl = "https://developers.kakao.com",
                    mobileWebUrl = "https://developers.kakao.com"
                )
            ),
            )

        // 피드 메시지 보내기

// 카카오톡 설치여부 확인
        if (LinkClient.instance.isKakaoLinkAvailable(context)) {
            // 카카오톡으로 카카오톡 공유 가능
            LinkClient.instance.defaultTemplate(context, defaultFeed) { linkResult, error ->
                if (error != null) {
                    Log.e(TAG, "카카오톡 공유 실패", error)
                }
                else if (linkResult != null) {
                    Log.d(TAG, "카카오톡 공유 성공 ${linkResult.intent}")
                    startActivity(linkResult.intent)

                    // 카카오톡 공유에 성공했지만 아래 경고 메시지가 존재할 경우 일부 컨텐츠가 정상 동작하지 않을 수 있습니다.
                    Log.w(TAG, "Warning Msg: ${linkResult.warningMsg}")
                    Log.w(TAG, "Argument Msg: ${linkResult.argumentMsg}")
                }
            }
        } else {
            // 카카오톡 미설치: 웹 공유 사용 권장
            // 웹 공유 예시 코드
            val sharerUrl = WebSharerClient.instance.defaultTemplateUri(defaultFeed)

            // CustomTabs으로 웹 브라우저 열기

            // 1. CustomTabs으로 Chrome 브라우저 열기
            try {
                KakaoCustomTabsClient.openWithDefault(context, sharerUrl)
            } catch(e: UnsupportedOperationException) {
                // Chrome 브라우저가 없을 때 예외처리
            }

            // 2. CustomTabs으로 디바이스 기본 브라우저 열기
            try {
                KakaoCustomTabsClient.open(context, sharerUrl)
            } catch (e: ActivityNotFoundException) {
                // 인터넷 브라우저가 없을 때 예외처리
            }
        }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment share_address.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic

        fun newInstance(key: String) =
            share_address().apply {
                arguments = Bundle().apply {
                    putString("key", key)
                }
            }
    }
}