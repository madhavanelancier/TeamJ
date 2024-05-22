package com.elancier.team_j

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.elancier.team_j.retrofit.ApproveUtils
import com.elancier.team_j.retrofit.CommonFunction
import com.elancier.team_j.retrofit.Utils
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_navogo_apply_leave.ddselect
import kotlinx.android.synthetic.main.activity_navogo_apply_leave.fromcard
import kotlinx.android.synthetic.main.activity_navogo_apply_leave.ftime
import kotlinx.android.synthetic.main.activity_navogo_apply_leave.hourcard
import kotlinx.android.synthetic.main.activity_navogo_apply_leave.hours
import kotlinx.android.synthetic.main.activity_navogo_apply_leave.hoursspinner
import kotlinx.android.synthetic.main.activity_navogo_apply_leave.imageButton
import kotlinx.android.synthetic.main.activity_navogo_apply_leave.multiple
import kotlinx.android.synthetic.main.activity_navogo_apply_leave.pdate
import kotlinx.android.synthetic.main.activity_navogo_apply_leave.reason
import kotlinx.android.synthetic.main.activity_navogo_apply_leave.signin
import kotlinx.android.synthetic.main.activity_navogo_apply_leave.single
import kotlinx.android.synthetic.main.activity_navogo_apply_leave.textView14
import kotlinx.android.synthetic.main.activity_navogo_apply_leave.tocard
import kotlinx.android.synthetic.main.activity_navogo_apply_leave.todates
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.concurrent.TimeUnit


class Navogo_ApplyLeave : AppCompatActivity() {
    private var mYear = 0
    private var mMonth: Int = 0
    private var mDay: Int = 0
    private var CalendarHour = 0
    private var CalendarMinute: Int = 0
    var format: String? = null
    var calendar: Calendar? = null
    var timepickerdialog: RangeTimePickerDialog? = null
    var activity = this
    var utils: Utils? = null
    var hrs = ""
    var minutes = ""
    lateinit var dialog: ProgressDialog
    var typeArr = ArrayList<String>()
    var typeIDArr = ArrayList<String>()
    var today_array = ArrayList<PickupModel>()
    var empId = ""
    var totDays=""
    internal lateinit var pref: SharedPreferences
    internal lateinit var editor: SharedPreferences.Editor


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navogo_apply_leave)
        val c: Calendar = Calendar.getInstance()
        mYear = c.get(Calendar.YEAR)
        mMonth = c.get(Calendar.MONTH)
        mDay = c.get(Calendar.DAY_OF_MONTH)
        utils = Utils(this)
        dialog = ProgressDialog(activity)
        pref = applicationContext.getSharedPreferences("MyPref", 0)
        editor = pref.edit()
        empId = pref.getString("empid","").toString()
       

        getTypes();


        single.setOnClickListener {
            fromcard.visibility=View.VISIBLE
            tocard.visibility=View.GONE
            hourcard.visibility=View.GONE
            multiple.setChecked(false)
            hours.setChecked(false)
        }

        multiple.setOnClickListener {
            fromcard.visibility=View.VISIBLE
            tocard.visibility=View.VISIBLE
            hourcard.visibility=View.GONE
            multiple.setChecked(true)
            hours.setChecked(false)
            single.setChecked(false)
        }

        hours.setOnClickListener {
            fromcard.visibility=View.VISIBLE
            tocard.visibility=View.GONE
            hourcard.visibility=View.VISIBLE
            multiple.setChecked(false)
            hours.setChecked(true)
            single.setChecked(false)
        }



        ddselect.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                if (position != 0) {
                    //getBalance(position)
                } else {
                   /* CommonFunction.alertDialogShow(
                        this@Navogo_ApplyLeave,
                        "Please select type",
                        "Select Leave",
                        ""
                    )*/
                }
            }
        }

        pdate.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

                if (s!!.length > 0) {
                    if (s.toString().equals("1") || s.toString().equals("0")) {
                        tocard.visibility = View.GONE
                    } else {
                        tocard.visibility = View.VISIBLE

                    }
                } else {
                    tocard.visibility = View.GONE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, star: Int, before: Int, count: Int) {

            }
        })


        hoursspinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View,
                position: Int,
                id: Long
            ) {
                // your code here
                textView14.setText("Total: "+hoursspinner.selectedItem.toString())

            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // your code here
            }
        })




        imageButton.setOnClickListener {
            finish()
        }

        signin.setOnClickListener {
            if (ddselect.selectedItemPosition != 0 && reason.text.toString()
                    .isNotEmpty()
            ) {

                if(single.isChecked){
                    if(ftime.text.toString().isNotEmpty()){
                        save()
                    }
                    else{
                        ftime.setError("Required field")
                        ftime.setError("Required field")
                    }
                }
                else if(multiple.isChecked){
                    if(ftime.text.toString().isNotEmpty()&&todates.text.toString().isNotEmpty()){
                        save()
                    }
                    else{

                        if (ftime.text.toString().isEmpty()) {
                            ftime.setError("Required field")
                            ftime.setError("Required field")

                        }
                        if (todates.text.toString().isEmpty()) {
                            todates.setError("Required field")
                            todates.setError("Required field")

                        }
                    }
                }
                else if(hours.isChecked){
                    if(ftime.text.toString().isNotEmpty()){
                        save()
                    }
                    else{
                        ftime.setError("Required field")

                    }
                }

            } else {

                if (reason.text.toString().isEmpty()) {
                    reason.setError("Required field")
                }
                if (ddselect.selectedItemPosition == 0) {
                    toast("Please select leave")
                }

            }
        }

        ftime.setOnClickListener {

                val datePickerDialog = DatePickerDialog(
                    this,
                    { view, year, monthOfYear, dayOfMonth -> ftime.setText(dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                        if(ftime.text.toString().isNotEmpty()&&todates.text.toString().isNotEmpty()){
                            val dateStr = ftime.text.toString()
                            val sdf = SimpleDateFormat("dd-MM-yyyy")
                            val date = sdf.parse(dateStr)

                            val dateStrTodate = todates.text.toString()
                            val sdfTodate = SimpleDateFormat("dd-MM-yyyy")
                            val dateTodate = sdfTodate.parse(dateStrTodate)

                            val diff: Long = dateTodate.getTime() - date.getTime()
                            System.out.println(
                                "Days: " + TimeUnit.DAYS.convert(
                                    diff,
                                    TimeUnit.MILLISECONDS
                                )
                            )
                            if(multiple.isChecked)
                            {
                                totDays=(TimeUnit.DAYS.convert(
                                    diff,
                                    TimeUnit.MILLISECONDS).toString().toInt()+1).toString()
                            }
                            else{
                                totDays=(TimeUnit.DAYS.convert(
                                    diff,
                                    TimeUnit.MILLISECONDS).toString())
                            }
                            textView14.setText("Total: "+TimeUnit.DAYS.convert(
                                diff,
                                TimeUnit.MILLISECONDS).toString()+" Days")

                        }
                        else{
                            textView14.setText("Total: "+"1 Day")
                            totDays="1"
                        }
                    },
                    mYear,
                    mMonth,
                    mDay
                )
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show()



        }


        todates.setOnClickListener {
                val datePickerDialog = DatePickerDialog(
                    this,
                    { view, year, monthOfYear, dayOfMonth -> todates.setText(dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                        if(ftime.text.toString().isNotEmpty()&&todates.text.toString().isNotEmpty()){
                            val dateStr = ftime.text.toString()
                            val sdf = SimpleDateFormat("dd-MM-yyyy")
                            val date = sdf.parse(dateStr)

                            val dateStrTodate = todates.text.toString()
                            val sdfTodate = SimpleDateFormat("dd-MM-yyyy")
                            val dateTodate = sdfTodate.parse(dateStrTodate)

                            val diff: Long = dateTodate.getTime() - date.getTime()
                            System.out.println(
                                "Days: " + TimeUnit.DAYS.convert(
                                    diff,
                                    TimeUnit.MILLISECONDS
                                )
                            )
                            if(multiple.isChecked)
                            {
                                totDays=(TimeUnit.DAYS.convert(
                                    diff,
                                    TimeUnit.MILLISECONDS).toString().toInt()+1).toString()
                            }
                            else{
                                totDays=(TimeUnit.DAYS.convert(
                                    diff,
                                    TimeUnit.MILLISECONDS).toString())
                            }

                            textView14.setText("Total: "+TimeUnit.DAYS.convert(
                                diff,
                                TimeUnit.MILLISECONDS).toString()+" Days")
                        }
                    },
                    mYear,
                    mMonth,
                    mDay
                )
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show()



        }

        reason.setFocusable(true)
        reason.requestFocus()
        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(reason, InputMethodManager.SHOW_IMPLICIT)


    }

    fun save() {

        CommonFunction.setupProgressDialog(this, "Loading", true, dialog)
        lateinit var call: Call<PickupModel>
        var jsonObject=JsonObject()
        jsonObject.addProperty("user_id",empId)
        jsonObject.addProperty("leave_type",typeIDArr[ddselect.selectedItemPosition])
        jsonObject.addProperty("reason",reason.text.toString())
        if(single.isChecked){
            jsonObject.addProperty("date",ftime.text.toString())
            jsonObject.addProperty("to_date",ftime.text.toString())
            jsonObject.addProperty("duration","Single")
            jsonObject.addProperty("total_days","1")


        }
        else if(multiple.isChecked){
            jsonObject.addProperty("date",ftime.text.toString())
            jsonObject.addProperty("to_date",todates.text.toString())
            jsonObject.addProperty("duration","Multiple")
            jsonObject.addProperty("total_days",totDays)


        }
        else if(hours.isChecked){
            jsonObject.addProperty("date",ftime.text.toString())
            jsonObject.addProperty("to_date",ftime.text.toString())
            jsonObject.addProperty("duration","Hours")
            jsonObject.addProperty("total_days",hoursspinner.selectedItem.toString())

            jsonObject.addProperty("duration",hoursspinner.selectedItem.toString())

        }
        call = ApproveUtils.Get.getAddLeave(jsonObject)


        call.enqueue(object : Callback<PickupModel> {
            override fun onResponse(call: Call<PickupModel>, response: Response<PickupModel>) {
                try {
                    dialog.dismiss()
                    Log.d("response", "Login " + response.body())
                    val loginmodel = response.body() as PickupModel
                    if (loginmodel.status == "Success") {
                        toast(loginmodel.message.toString())
                        finish()

                    } else {
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
                Log.e("throe", t.toString())
                CommonFunction.alertDialogShow(
                    activity,
                    "Something went wrong",
                    "Information",
                    ""
                )
            }
        })
    }


    fun getTypes() {
        dialog.dismiss()
        typeArr.clear()
        typeIDArr.clear()
        today_array.clear()
        typeArr.add("Select Leave type")
        typeIDArr.add("0")

        CommonFunction.setupProgressDialog(this, "Loading", true, dialog)
        val call:Call<PickupModel>

        call = ApproveUtils.Get.getLeaveType()

        call!!.enqueue(object : Callback<PickupModel> {
            override fun onResponse(call: Call<PickupModel>, response: Response<PickupModel>) {
                try {
                    Log.d("response", "Login " + response.body())
                    val loginmodel = response.body() as PickupModel

                    if (loginmodel.status == "Success") {
                        today_array = (loginmodel.response as ArrayList<PickupModel>)
                        for (i in 0 until today_array.size) {
                            typeArr.add(today_array[i].name.toString())
                            typeIDArr.add(today_array[i].id.toString())
                        }
                        val customAdapter =
                            CustomeSpinner(activity, typeArr as java.util.ArrayList<String>)
                        ddselect.adapter = customAdapter

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
                CommonFunction.alertDialogShow(
                    activity,
                    "Something went wrong",
                    "Information",
                    ""
                )
            }
        })
    }




    fun calculate() {
        val simpleDateFormat = SimpleDateFormat("hh:mm a")

        val date1 = simpleDateFormat.parse(ftime.text.toString())
        val date2 = simpleDateFormat.parse(todates.text.toString())

        val difference: Long = date2.getTime() - date1.getTime()
        val days = (difference / (1000 * 60 * 60 * 24)).toInt()
        var hours = ((difference - 1000 * 60 * 60 * 24 * days) / (1000 * 60 * 60))
        val min =
            (difference - 1000 * 60 * 60 * 24 * days - 1000 * 60 * 60 * hours).toInt() / (1000 * 60)
        hours = if (hours < 0) -hours else hours
        Log.i("======= Hours", " :: $hours")
        Log.i("======= min", " :: $min")

        hrs = hours.toString()
        minutes = min.toString()

        if (hours.toInt() == 0 && min.toInt() > 0) {
            textView14.setText("Total Timing : " + hours + " hour " + min + " mins")
        } else if (hours.toInt() > 0 && min.toInt() > 0) {
            textView14.setText("Total Timing : " + hours + " hour " + min + " mins")
        } else if (hours.toInt() > 0 && min.toInt() == 0) {
            textView14.setText("Total Timing : " + hours + " hour")
        }
    }

    /* inner class UpdateInfoTask :
         AsyncTask<String?, Void?, String?>() {
         internal lateinit var pDialo : ProgressDialog
         //var statusval=""

         override fun onPreExecute() {
             // array.clear()
             pDialo = ProgressDialog(activity);
             pDialo.setMessage("Please wait...");
             pDialo.setIndeterminate(false);
             pDialo.setProgressStyle(ProgressDialog.STYLE_SPINNER);
             pDialo.setCancelable(false);
             //pDialo.setMax(3)
             pDialo.show()
         }

         @SuppressLint("WrongThread")
         override fun doInBackground(vararg params: String?): String? {
             var result: String? = null
             val con =
                 Connection()
             //statusval= status.toString()
             try {

                 val json= JSONObject()
                 json.put("user_id", params[0])
                 json.put("from_date", params[1])
                 json.put("to_date", params[2])
                 json.put("type", params[3])
                 json.put("reason", params[4])
                 json.put("types", "add")
                 json.put("days", params[5])

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

         override fun onPostExecute(resp:String?) {

             try {
                 Log.i("tabresp", resp + "")
                 pDialo.dismiss()
                 if (resp != null) {
                     val json = JSONArray(resp)
                     val obj1 = json.getJSONObject(0)
                     if (obj1.getString("Status") == "Success") {
                         pDialo!!.dismiss()
                         toast("Leave applied Successfully.")
                         finish()

                     } else {
                         pDialo.dismiss()
                         Toast.makeText(activity, obj1.getString("Response"), Toast.LENGTH_SHORT).show()
                     }
                 } else {
                     pDialo.dismiss()

                 }
             } catch (e: Exception) {
                 e.printStackTrace()
                 Log.e("err", e.toString())

             }
         }

     }*/


    fun toast(msg: String) {
        val toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }
}