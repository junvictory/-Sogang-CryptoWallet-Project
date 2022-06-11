package kr.icclab.kyptowallet

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONObject

class PreferenceUtil(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("prefs_name", Context.MODE_PRIVATE)

    fun getString(key: String, defValue: String): String {
        return prefs.getString(key, defValue).toString()
    }
    fun setString(key: String, str: String) {
        prefs.edit().putString(key, str).apply()
    }

    fun getBoolean(key:String, defValue: Boolean) :Boolean{
        return prefs.getBoolean(key, defValue)
    }
    fun setBoolean(key: String, ble: Boolean) {
        prefs.edit().putBoolean(key, ble).apply()
    }
    //json
    fun getJson(key: String, jsonObj : JSONObject): JSONObject{
        return JSONObject(prefs.getString(key,jsonObj.toString()))
    }
    fun setJson(key: String, jsonObj : JSONObject){
        prefs.edit().putString(key,jsonObj.toString()).apply()
    }

    fun clear() {
        prefs.edit().clear().commit()
    }
}