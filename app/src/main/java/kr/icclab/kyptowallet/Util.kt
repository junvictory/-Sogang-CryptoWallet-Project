package kr.icclab.kyptowallet

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.widget.Toast


class Util {
    object Util{
        fun CopyClipboard(context : Context,str : String){
            val clipboard: ClipboardManager =
                context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("label", str)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(context, "클립보드에 저장 되었습니다.", Toast.LENGTH_SHORT).show()
        }

    }


}