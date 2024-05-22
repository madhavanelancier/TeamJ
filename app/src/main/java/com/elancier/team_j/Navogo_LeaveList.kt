package com.elancier.team_j

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.elancier.team_j.Adapers.Navogo_leave_Adapter
import com.elancier.team_j.retrofit.ApproveUtils
import com.elancier.team_j.retrofit.CommonFunction
import com.elancier.team_j.retrofit.Resp_otp
import com.elancier.team_j.retrofit.Utils
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_navogo_leaves.fab1
import kotlinx.android.synthetic.main.activity_navogo_leaves.fromdt
import kotlinx.android.synthetic.main.activity_navogo_leaves.header
import kotlinx.android.synthetic.main.activity_navogo_leaves.progressBar7
import kotlinx.android.synthetic.main.activity_navogo_leaves.recycler
import kotlinx.android.synthetic.main.activity_navogo_leaves.textView3
import kotlinx.android.synthetic.main.activity_navogo_leaves.textView4
import kotlinx.android.synthetic.main.activity_navogo_leaves.todt
import kotlinx.android.synthetic.main.header_lay.back
import kotlinx.android.synthetic.main.header_lay.titlehead
import kotlinx.android.synthetic.main.header_lay.view.back

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class Navogo_LeaveList : AppCompatActivity() {
    val activity=this
    var today_array=ArrayList<PickupModel>()
    var adp: Navogo_leave_Adapter?=null
    private var mYear = 0
    private  var mMonth:Int = 0
    private  var mDay:Int = 0
    private  var mHour:Int = 0
    private  var mMinute:Int = 0
    private var loading = true
    var pastVisiblesItems = 0
    var visibleItemCount:Int = 0
    var totalItemCount:Int = 0
    lateinit var dialog: ProgressDialog
    var empId = ""

    var start=0
    var limit=10
    var utils: Utils?=null
    internal lateinit var pref: SharedPreferences
    internal lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navogo_leaves)

        dialog = ProgressDialog(activity)
        utils=Utils(this)
        pref = applicationContext.getSharedPreferences("MyPref", 0)
        editor = pref.edit()
        empId = pref.getString("empid","").toString()

       back.setOnClickListener {
            finish()
        }
        titlehead.setText("Leaves List")

        val mLayoutManager: LinearLayoutManager
        mLayoutManager = LinearLayoutManager(this)
        recycler!!.setLayoutManager(mLayoutManager)

        val c: Calendar = Calendar.getInstance()
        mYear = c.get(Calendar.YEAR)
        mMonth = c.get(Calendar.MONTH)
        mDay = c.get(Calendar.DAY_OF_MONTH)
        utils=Utils(this)

        val format=SimpleDateFormat("dd-MM-yyyy")
        val frdt=format.format(c.time)
        (c.set(Calendar.DAY_OF_MONTH,1))
        val todts=format.format(c.time)
        textView3.setText(todts)
        textView4.setText(frdt)



        fab1.setOnClickListener {
            startActivity(Intent(this,Navogo_ApplyLeave::class.java))
        }

        fromdt.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this,
                { view, year, monthOfYear, dayOfMonth -> textView3.setText(dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)

                    if(textView4.text.toString()!="To Date"){

                        today_array.clear()
                        start=0
                        limit=10
                        getLeaves()

                        /*Get_todayreport().execute(utils!!.getid(),binding.textView3.text.toString(),binding.textView4.text.toString(),start.toString(),
                            limit.toString())*/
                    }
                },
                mYear,
                mMonth,
                mDay
            )
            datePickerDialog.show()
        }

        todt.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this,
                { view, year, monthOfYear, dayOfMonth -> textView4.setText(dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                    today_array.clear()
                    start=0
                    limit=10
                    getLeaves()
                    /*Get_todayreport().execute(utils!!.getid(),binding.textView3.text.toString(),binding.textView4.text.toString(),start.toString(),
                        limit.toString())*/
                },
                mYear,
                mMonth,
                mDay
            )
            datePickerDialog.show()
        }


        recycler!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) { //check for scroll down
                    visibleItemCount = mLayoutManager.getChildCount()
                    totalItemCount = mLayoutManager.getItemCount()
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition()
                    Log.v("visibleItemCount",visibleItemCount.toString())
                    Log.v("pastVisiblesItems", pastVisiblesItems.toString())
                    Log.v("totalItemCount", totalItemCount.toString())
                    Log.v("loading", loading.toString())

                    if (loading) {
                        if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                            loading = false
                            Log.v("...", "Last Item Wow!")
                            Log.v("visibleItemCount", visibleItemCount.toString())
                            Log.v("totalItemCount", totalItemCount.toString())
                            //progressBar7.visibility = View.VISIBLE
                            start = totalItemCount
                            //limit=totalItemCount+totalItemCount;
                            Log.v("start", start.toString())
                            Log.v("limit", limit.toString())
                            //today_array.clear()
                            /*Get_todayreport().execute(utils!!.getid(),binding.textView3.text.toString(),binding.textView4.text.toString(),start.toString(),
                                limit.toString())*/
                        }
                    }
                }
            }
        })

    }

    override fun onResume() {
        super.onResume()
        today_array.clear()
        start=0
        limit=10
        val c: Calendar = Calendar.getInstance()
        val format=SimpleDateFormat("dd-MM-yyyy")
        val frdt=format.format(c.time)
        (c.set(Calendar.DAY_OF_MONTH,1))
        val todts=format.format(c.time)
        textView3.setText(todts)
        textView4.setText(frdt)

        getLeaves()

        /*Get_todayreport().execute(utils!!.getid(),binding.textView3.text.toString(),binding.textView4.text.toString(),start.toString(),
            limit.toString())*/
    }

    fun getLeaves() {
        dialog.dismiss()
        today_array.clear()
        CommonFunction.setupProgressDialog(this, "Loading", true, dialog)
        var call: Call<PickupModel>? = null
        call = ApproveUtils.Get.getLeaves(empId,textView3.text.toString(),textView4.text.toString())

        call!!.enqueue(object : Callback<PickupModel> {
            override fun onResponse(call: Call<PickupModel>, response: Response<PickupModel>) {
                try {
                    Log.d("response", "Login " + response.body())
                    val loginmodel = response.body() as PickupModel

                    if (loginmodel.status == "Success") {
                        today_array = (loginmodel.response as ArrayList<PickupModel>)

                        adp = Navogo_leave_Adapter(activity!!, today_array)
                        recycler!!.adapter = adp
                        recycler!!.itemAnimator = DefaultItemAnimator()
                        dialog.dismiss()

                    } else {
                        dialog.dismiss()

                        CommonFunction.alertDialogShow(
                            activity,
                            loginmodel.message,
                            "Information",
                            ""
                        )
                    }


                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<PickupModel>, t: Throwable) {
                Log.e("throw",t.toString())
                CommonFunction.alertDialogShow(
                    activity,
                    "Something went wrong",
                    "Information",
                    ""
                )
            }
        })
    }


    fun CancelLeave(id:String) {
        dialog.dismiss()
        today_array.clear()
        CommonFunction.setupProgressDialog(this, "Loading", true, dialog)
        var json=JsonObject()
        json.addProperty("id",id)
        var call: Call<Resp_otp>? = null
        call = ApproveUtils.Get.cancelLeave(json)

        call!!.enqueue(object : Callback<Resp_otp> {
            override fun onResponse(call: Call<Resp_otp>, response: Response<Resp_otp>) {
                try {
                    Log.d("response", "Login " + response.body())
                    val loginmodel = response.body() as Resp_otp

                    if (loginmodel.status == "Success") {
                        Toast.makeText(applicationContext,loginmodel.message,Toast.LENGTH_LONG).show()
                        getLeaves()
                        dialog.dismiss()

                    } else {
                        dialog.dismiss()

                        CommonFunction.alertDialogShow(
                            activity,
                            loginmodel.message,
                            "Information",
                            ""
                        )
                    }


                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<Resp_otp>, t: Throwable) {
                Log.e("throw",t.toString())
                CommonFunction.alertDialogShow(
                    activity,
                    "Something went wrong",
                    "Information",
                    ""
                )
            }
        })
    }


  /*  inner class Get_todayreport :
        AsyncTask<String?, Void?, String?>() {
        internal lateinit var pDialo : ProgressDialog
        //var statusval=""

        override fun onPreExecute() {
            // array.clear()
            //today_array.clear()
            *//*pDialo = ProgressDialog(activity);
            pDialo.setMessage("Please wait...");
            pDialo.setIndeterminate(false);
            pDialo.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialo.setCancelable(false);
            //pDialo.setMax(3)
            pDialo.show()*//*
            if(start==0) {
                binding.recycler.visibility=View.INVISIBLE
                binding.shimmer_view_container.visibility = View.VISIBLE
                binding.shimmer_view_container.startShimmer()
            }
            if(start!=0) {
                binding.progressBar7.visibility = View.VISIBLE
            }

        }

        @SuppressLint("WrongThread")
        override fun doInBackground(vararg params: String?): String? {
            var result: String? = null
            val con =
                Connection()
            //statusval= status.toString()
            try {
                println("devid" + params.get(0))
                println("token" + params.get(1))
                val deviceId = Settings.Secure.getString(
                    activity!!.getContentResolver(),
                    Settings.Secure.ANDROID_ID
                )
                val json= JSONObject()
                json.put("user_id", params[0])
                json.put("from", params[1])
                json.put("to", params[2])
                json.put("start", params[3])
                json.put("skip", params[4])
                json.put("types", "list")

                result = con.sendHttpPostjson(
                    Appconstands.Domin + "leaveRequest.php",
                    json
                )
                Log.e("input", json.toString())
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("input", e.toString())

            }
            return result
        }

        override fun onPostExecute(resp: String?) {

            try {
                Log.i("tabresp", resp + "")
                //pDialo.dismiss()
                if (resp != null) {
                    val json = JSONArray(resp)
                    val obj1 = json.getJSONObject(0)
                    if (obj1.getString("Status") == "Success") {
                        //pDialo!!.dismiss()
                        val jsonarr=obj1.getJSONArray("Response")

                        for(i in 0 until jsonarr.length()){
                            val data=People()
                            data.username=utils!!.getname()
                            data.time=jsonarr.getJSONObject(i).getString("from_date")
                            data.date=jsonarr.getJSONObject(i).getString("days")
                            data.print_user=jsonarr.getJSONObject(i).getString("reason")
                            var tot=jsonarr.getJSONObject(i).getString("to_date")

                            if(tot.isNullOrEmpty()||tot=="null"){
                                data.logout=""
                            }
                            else{
                                data.logout=tot
                            }

                            data.ip_address=jsonarr.getJSONObject(i).getString("type")


                            today_array.add(data)
                        }
                        nodata.visibility=View.GONE
                        recycler.visibility=View.VISIBLE
                        shimmer_view_container.visibility= View.GONE
                        shimmer_view_container.stopShimmer()
                        progressBar7.visibility = View.GONE
                        loading=true

                        adp = Leave_Report_Adapter(activity!!, today_array)
                        recycler!!.adapter = adp


                    } else {
                        //pDialo.dismiss()
                        shimmer_view_container.visibility= View.GONE
                        shimmer_view_container.stopShimmer()
                        if(start==0) {
                            nodata.visibility = View.VISIBLE
                            recycler.visibility=View.INVISIBLE
                            progressBar7.visibility = View.GONE
                        }
                        else{
                            progressBar7.visibility = View.GONE

                        }
                        Toast.makeText(activity, obj1.getString("Response"), Toast.LENGTH_SHORT).show()
                    }
                } else {
                    //pDialo.dismiss()
                    shimmer_view_container.visibility= View.GONE
                    shimmer_view_container.stopShimmer()
                    if(start==0) {
                        nodata.visibility = View.VISIBLE
                        recycler.visibility=View.INVISIBLE
                        progressBar7.visibility = View.GONE
                    }
                    else{
                        progressBar7.visibility = View.GONE

                    }

                }
            } catch (e: Exception) {
                shimmer_view_container.visibility= View.GONE
                shimmer_view_container.stopShimmer()
                nodata.visibility=View.VISIBLE
                nodata.setText(e.toString())
                recycler.visibility=View.INVISIBLE

                e.printStackTrace()
                Log.e("err", e.toString())

            }
        }
    }*/
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()

        }
        return true
    }
}