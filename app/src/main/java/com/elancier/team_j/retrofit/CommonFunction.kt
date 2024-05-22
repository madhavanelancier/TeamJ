package com.elancier.team_j.retrofit

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import org.json.JSONObject
import org.json.JSONTokener
import java.io.ByteArrayInputStream
import java.io.InputStream

object CommonFunction {
    fun checkLogin(resp: String): Boolean {
        return try {
            val json = JSONObject(resp)
            Log.i("resp", resp + "")
            if (json.getBoolean("success") == true) {
                true
            } else if (json.getBoolean("success") == false) {
                if (json.has("data")) {
                    val jsonTok = JSONTokener(json.getString("data"))
                    val jObj = JSONObject(jsonTok)
                    if (jObj.has("redirectToLogin") && jObj.getBoolean("redirectToLogin") == true) {
                        false
                    } else {
                        true
                    }
                } else {
                    true
                }
            } else {
                true
            }
        } catch (e: Exception) {
            e.printStackTrace()
            true
        }
    }
    fun setupProgressDialog(context: Context, msg:String, show:Boolean, progress: ProgressDialog) {
        progress.setMessage(msg)
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progress.setCancelable(show)
        progress.show()
    }

    fun alertDialogShow(context: Context?, message: String?, title:String, action:String) {
        val alertDialog = AlertDialog.Builder(context).create()
        alertDialog.setMessage(message)
        alertDialog.setTitle(title)
        alertDialog.setButton(
            "OK"
        ) { dialog, which -> alertDialog.dismiss()


        }
        alertDialog.show()
    }

    @JvmStatic
    fun createfalseJson(): InputStream? {
        try {
            val json = JSONObject()
            json.put("success", false)
            val lo = JSONObject()
            lo.put("redirectToLogin", true)
            json.put("data", lo)
            return ByteArrayInputStream(json.toString().toByteArray(charset("UTF-8")))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}