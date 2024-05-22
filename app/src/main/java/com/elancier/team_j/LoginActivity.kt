package com.elancier.team_j

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.elancier.team_j.retrofit.ApproveUtils
import com.elancier.team_j.retrofit.Resp_otp
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_username_pass.eye2
import kotlinx.android.synthetic.main.activity_username_pass.password
import kotlinx.android.synthetic.main.activity_username_pass.signin
import kotlinx.android.synthetic.main.activity_username_pass.uname
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {
    lateinit var pDialog: Dialog
    internal  var pref: SharedPreferences?=null
    var religion=""
    internal  var editor: SharedPreferences.Editor?=null
    var sitelogo=""
    val activity = this
    var dialog: ProgressDialog?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        setContentView(R.layout.activity_username_pass)

        pref = this.getSharedPreferences("MyPref", 0)

        editor = pref!!.edit()
        //mobile.requestFocus()

        if(pref!!.getString("van_login","").toString().isEmpty()){

        }
        else if(pref!!.getString("van_login","").toString().isNotEmpty()){
            startActivity(Intent(activity,MainActivity::class.java)
                .putExtra("from","Employee"))
            finish()
        }


        var visible=false
      eye2.setOnClickListener {
            if(visible==false){
                visible=true
                password.transformationMethod = HideReturnsTransformationMethod.getInstance()
                eye2.setImageDrawable(resources.getDrawable(R.drawable.invisible))
            } else{
                visible=false
                password.transformationMethod = PasswordTransformationMethod.getInstance()
                eye2.setImageDrawable(resources.getDrawable(R.drawable.baseline_remove_red_eye_24))
            }
        }

        signin.setOnClickListener {
           /* startActivity(Intent(activity,MainActivity::class.java)
                .putExtra("from","Employee"))
            finish()*/
            if(uname.text.toString().trim().isNotEmpty()&&password.text.toString().trim().isNotEmpty()){
                    if (Appconstands.net_status(this)) {
                        SendLogin(uname.text.toString().trim(),password.text.toString())
                    }
            }
            else{

                if(uname.text.isNullOrBlank()){
                    uname.setError("Enter Username.")

                }

                if(password.text.isNullOrBlank()){
                    password.setError("Enter password.")

                }
            }
        }

    }
    fun SendLogin(mobiles:String,pass:String){

        pDialog= Dialog(activity)
        var token=FirebaseInstanceId.getInstance().token
        Appconstands.loading_show(activity, pDialog).show()

                val device_id = Settings.Secure.getString(
                    getApplicationContext().getContentResolver(),
                    Settings.Secure.ANDROID_ID
                )

        var jsonobj=JsonObject()
        jsonobj.addProperty("username",mobiles)
        jsonobj.addProperty("password",pass)
        jsonobj.addProperty("device_id",device_id)
        jsonobj.addProperty("token",token)
        Log.e("json",jsonobj.toString())
        val call = ApproveUtils.Get.getlogin(jsonobj)
        call.enqueue(object : Callback<Resp_otp> {
            override fun onResponse(call: Call<Resp_otp>, response: Response<Resp_otp>) {
                Log.e("response", response.toString())
                if (response.isSuccessful()) {
                    val example = response.body() as Resp_otp
                    println(example)
                    if (example.status == "Success") {
                        var arr=example.message
                        println("otp"+arr)
                        toast(arr.toString())
                        editor!!.putString("van_login", "true")
                        editor!!.putString("mobile", example.response?.get(0)!!.mobile)
                        editor!!.putString("fname", example.response!![0]!!.first_name)
                        editor!!.putString("empid", example.response!![0]!!.id)
                        editor!!.putString("city", example.response!![0]!!.city)
                        editor!!.putString("emptype", "Employee")

                        editor!!.commit()
                        startActivity(Intent(activity,MainActivity::class.java)
                            .putExtra("from","Employee"))
                        finish()
                        //finish()
                    } else {
                        Toast.makeText(
                            activity,
                            example.message,
                            Toast.LENGTH_LONG
                        ).show()
                        uname.setText("")
                        password.setText("")
                    }
                }
                pDialog.dismiss()
                //loading_show(activity).dismiss()
            }

            override fun onFailure(call: Call<Resp_otp>, t: Throwable) {
                Log.e("Fail response", t.toString())
                if (t.toString().contains("time")) {
                    Toast.makeText(
                        activity,
                        "Poor network connection",
                        Toast.LENGTH_LONG
                    ).show()
                }
                pDialog.dismiss()
                //loading_show(activity).dismiss()
            }
        })


    }


    fun toast(msg:String){
        val toast=Toast.makeText(activity,msg,Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER,0,0)
        toast.show()
    }


    @SuppressLint("StaticFieldLeak")
    inner class Areacodes : AsyncTask<String, Void, String>() {
        override fun onPreExecute() {
            //progbar.setVisibility(View.VISIBLE);


            Log.i("Areacodes", "started")
        }

        override fun doInBackground(vararg param: String): String? {


            //load()

            return null
        }

        override fun onPostExecute(resp: String?) {

        }
    }



    override fun onBackPressed() {
        editor!!.putString("reg","")
        editor!!.commit()
        val intent = Intent()
        setResult(1, intent)
        finish()
    }

}
