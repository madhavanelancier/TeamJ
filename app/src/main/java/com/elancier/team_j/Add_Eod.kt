package com.elancier.team_j

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.elancier.domdox.Adapters.PersonsAdapter
import com.elancier.team_j.retrofit.*
import kotlinx.android.synthetic.main.activity_add__eod.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList
import android.widget.AdapterView

import android.widget.AdapterView.OnItemSelectedListener





class Add_Eod : AppCompatActivity(),PersonsAdapter.OnItemClickListener {
    val activity = this
    var persons = JSONArray()
    lateinit var personadap : PersonsAdapter
    lateinit var pDialog : Dialog
    internal  var pref: SharedPreferences?=null
    internal  var editor: SharedPreferences.Editor?=null
    var cid = ""
    var eid = ""
    var estate=""
    var ecity=""
    private var CalendarHour = 0
    private  var CalendarMinute:Int = 0
    private  var statearr=ArrayList<String>()
    private  var statearr_id=ArrayList<String>()
    private  var cityarr=ArrayList<String>()
    var format: String? = null
    var calendar: Calendar? = null
    var timepickerdialog: TimePickerDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add__eod)

        val ab = supportActionBar
        ab!!.setDisplayHomeAsUpEnabled(true)
        ab!!.setDisplayShowHomeEnabled(true)
        ab!!.title = "Add EOD"
        pDialog = Dialog(activity)
        pref = activity.getSharedPreferences("MyPref", 0)
        editor = pref!!.edit()



        val bundle = intent.extras!!
        if (!bundle.getString("edit").isNullOrEmpty()){
            ab!!.title = "Edit EOD"
            val res = JSONObject(bundle.getString("edit"))
            eid = if (res.has("id")) res.getString("id") else ""
             estate = if (res.has("state")) res.getString("state") else ""
             ecity = if (res.has("city")) res.getString("city") else ""
            cid = if (res.has("customer_id")) res.getString("customer_id") else ""
            val ecname = if (res.has("cname")) res.getString("cname") else ""
            val emmob = if (res.has("mmob")) res.getString("mmob") else ""
            val eemail = if (res.has("email")) res.getString("email") else ""
            val emaddress = if (res.has("maddress")) res.getString("maddress") else ""
            val esval = if (res.has("sval")) res.getString("sval") else ""
            val esq_val = if (res.has("sq_val")) res.getString("sq_val") else ""
            val eintime = if (res.has("intime")) res.getString("intime") else ""
            val eouttime = if (res.has("outtime")) res.getString("outtime") else ""
            val emagcapacity = if (res.has("magcapacity")) res.getString("magcapacity") else ""
            val eturnover = if (res.has("turnover")) res.getString("turnover") else ""
            val etval = if (res.has("tval")) res.getString("tval") else ""
            val ectype = if (res.has("ctype")) res.getString("ctype") else ""
            val ecpro = if (res.has("cpro")) res.getString("cpro") else ""
            val escomm = if (res.has("scomm")) res.getString("scomm") else ""
            val eplike = if (res.has("plike")) res.getString("plike") else ""
            val epcomnt = if (res.has("pcomnt")) res.getString("pcomnt") else ""
            val eexfeed = if (res.has("exfeed")) res.getString("exfeed") else ""
            val eroomloc = if (res.has("roomloc")) res.getString("roomloc") else ""
            val espur = if (res.has("spur")) res.getString("spur") else ""
            persons = if (res.has("persons")) res.getJSONArray("persons") else JSONArray()
            val ct = ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, activity.resources.getStringArray(R.array.custtype))
            val cp = ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, activity.resources.getStringArray(R.array.custpros))
            val pur = ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, activity.resources.getStringArray(R.array.season))
            //state.setText(estate)
            //city.setText(ecity)
            cname.setText(ecname)
            mmob.setText(emmob)
            email.setText(eemail)
            maddress.setText(emaddress)
            tval.setText(esval)
            sqval.setText(esq_val)
            intime.setText(eintime)
            outtime.setText(eouttime)
            magcapacity.setText(emagcapacity)
            turnover.setText(etval)
            ctype.setSelection(ct.getPosition(ectype))
            cpro.setSelection(cp.getPosition(ecpro))
            scomm.setText(escomm)
            plike.setText(eplike)
            pcomnt.setText(epcomnt)
            exfeed.setText(eexfeed)
            roomloc.setText(eroomloc)
            spur.setSelection(pur.getPosition(espur))

            personadap = PersonsAdapter(activity, persons, activity)
            person_list.adapter=personadap
        }

        personadap = PersonsAdapter(activity, persons, activity)
        person_list.adapter=personadap
        add_person.setOnClickListener {
            Person_Popup(null)
        }

        state
        city
        cname
        mmob
        email
        maddress
        tval
        sqval
        intime
        outtime
        magcapacity
        turnover
        ctype
        cpro
        scomm
        plike
        pcomnt
        exfeed
        roomloc
        spur

      /*  selectcus.setOnClickListener {

        }*/

        /*
        trip_id
        state
        city
        customer_id
        cname
        mmob
        email
        maddress
        sval
        sq_val
        intime
        outtime
        magcapacity
        turnover
        ctype
        cpro
        scomm
        plike
        pcomnt
        exfeed
        roomloc
        spur
        persons
        */

        println("tid : " + pref!!.getString("tid", ""))
        println("cid : " + cid)
        next.setOnClickListener {
            if (cid.isNotEmpty()) {
                if (eid.isNullOrEmpty()) {
                    if(state.selectedItemPosition!=0&&city.selectedItemPosition!=0) {
                        AddEOD().execute(Appconstants.add_eod)
                    }
                    else{
                        if(state.selectedItemPosition==0) {
                            Toast.makeText(applicationContext, "Select State", Toast.LENGTH_SHORT)
                                .show()
                        }
                        if(city.selectedItemPosition==0) {
                            Toast.makeText(applicationContext, "Select City", Toast.LENGTH_SHORT)
                                .show()
                        }

                    }

                }else{
                    AddEOD().execute(Appconstants.eod_edit)
                }
            }else{
                Toast.makeText(activity, "Select Customer", Toast.LENGTH_SHORT).show()
            }
        }



        state.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View,
                position: Int,
                id: Long
            ) {
                // your code here
                if(position!=0){
                    cityload(statearr_id[position].toString())
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // your code here
            }
        })

        selectcus.setOnClickListener {
            if(state.selectedItemPosition!=0&&city.selectedItemPosition!=0) {
                val its = Intent(activity, EOD_Customers_List::class.java)
                its.putExtra("cus", "eod")
                its.putExtra("state", statearr_id[state.selectedItemPosition])
                its.putExtra("city", cityarr[city.selectedItemPosition])
                startActivityForResult(its, 123)
            }
            else{
                if(state.selectedItemPosition==0){
                    Toast.makeText(applicationContext, "Select State", Toast.LENGTH_SHORT).show()
                }
                if(city.selectedItemPosition==0){
                    Toast.makeText(applicationContext, "Select City", Toast.LENGTH_SHORT).show()
                }
            }

        }

        intime.setOnClickListener {
            calendar = Calendar.getInstance();
            CalendarHour = calendar!!.get(Calendar.HOUR_OF_DAY);
            CalendarMinute = calendar!!.get(Calendar.MINUTE);
            timepickerdialog = TimePickerDialog(this@Add_Eod,
                    { view, hourOfDay, minute ->
                        var hourOfDay = hourOfDay
                        if (hourOfDay == 0) {
                            hourOfDay += 12
                            format = "AM"
                        } else if (hourOfDay == 12) {
                            format = "PM"
                        } else if (hourOfDay > 12) {
                            hourOfDay -= 12
                            format = "PM"
                        } else {
                            format = "AM"
                        }
                        intime.setText("$hourOfDay:$minute$format")
                    }, CalendarHour, CalendarMinute, false)
            timepickerdialog!!.show()

        }

        outtime.setOnClickListener {
            calendar = Calendar.getInstance();
            CalendarHour = calendar!!.get(Calendar.HOUR_OF_DAY);
            CalendarMinute = calendar!!.get(Calendar.MINUTE);
            timepickerdialog = TimePickerDialog(this@Add_Eod,
                    { view, hourOfDay, minute ->
                        var hourOfDay = hourOfDay
                        if (hourOfDay == 0) {
                            hourOfDay += 12
                            format = " AM"
                        } else if (hourOfDay == 12) {
                            format = " PM"
                        } else if (hourOfDay > 12) {
                            hourOfDay -= 12
                            format = " PM"
                        } else {
                            format = " AM"
                        }
                        outtime.setText("$hourOfDay:$minute$format")
                    }, CalendarHour, CalendarMinute, false)
            timepickerdialog!!.show()

        }

        stateload()
    }

    fun Person_Popup(position: Int?){
        val openwith = android.app.AlertDialog.Builder(activity)
        val popUpView = layoutInflater.inflate(R.layout.persondetails_popup, null)
        val fname = popUpView.findViewById(R.id.fname) as EditText
        val mob = popUpView.findViewById(R.id.mmob) as EditText
        val desig = popUpView.findViewById(R.id.desig) as EditText
        val button = popUpView.findViewById(R.id.button) as Button
        openwith.setView(popUpView)
        val person = openwith.create()
        fname.requestFocus()
        person.setCancelable(true)
        person.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        person.show()
        if (position!=null){
            fname.setText(persons.getJSONObject(position).getString("name"))
            mob.setText(persons.getJSONObject(position).getString("mob"))
            desig.setText(persons.getJSONObject(position).getString("desig"))

            /*"id": 4,
                    "trip_id": "V00011",
                    "eod": "9",
                    "name": "Saravana kumar",
                    "designation": "designation",
                    "mobile": "9894940560",*/
        }

        button.setOnClickListener{
            if (fname.text.toString().trim().isEmpty()){
                fname.setError("Enter Name")
                return@setOnClickListener
            }
            if (mob.text.toString().trim().isEmpty()){
                mob.setError("Enter Mobile Number")
                return@setOnClickListener
            }
            if (desig.text.toString().trim().isEmpty()){
                desig.setError("Enter Designation")
                return@setOnClickListener
            }

            val data= JSONObject()
            data.put("pid", if (position != null) position.toString() else persons.length().toString())
            data.put("id", if (position != null) if (!eid.isNullOrEmpty()) persons.getJSONObject(position).getString("id") else position.toString() else persons.length().toString())
            data.put("name", fname.text.toString().trim())
            data.put("mob", mob.text.toString().trim())
            data.put("desig", desig.text.toString().trim())
            data.put("trip_id", pref!!.getString("tid", ""))
            if (eid.isNotEmpty())data.put("eod", eid)
            if (position!=null)persons.put(position, data) else persons.put(data)
            personadap.notifyDataSetChanged()

            person.dismiss()

        }

    }
    fun Validate():Boolean{
        /*if(state.text.toString().trim().isEmpty()){
            return false
        }
        if(city.text.toString().trim().isEmpty()){
            return false
        }*/
        if(cname.text.toString().trim().isEmpty()){
            cname.setError("Fill this field")
            return false
        }
        if(mmob.text.toString().trim().length!=10){
            mmob.setError("Enter 10 digit Mobile Number")
            return false
        }
        if(email.text.toString().trim().isEmpty()){
            email.setError("Fill this field")
            return false
        }
        if(maddress.text.toString().trim().isEmpty()){
            maddress.setError("Fill this field")
            return false
        }
        if(tval.text.toString().trim().isEmpty()){
            tval.setError("Fill this field")
            return false
        }
        if(sqval.text.toString().trim().isEmpty()){
            sqval.setError("Fill this field")
            return false
        }
        if(intime.text.toString().trim().isEmpty()){
            intime.setError("Fill this field")
            return false
        }
        if(outtime.text.toString().trim().isEmpty()){
            outtime.setError("Fill this field")
            return false
        }
        if(magcapacity.text.toString().trim().isEmpty()){
            magcapacity.setError("Fill this field")
            return false
        }
        if(turnover.text.toString().trim().isEmpty()){
            turnover.setError("Fill this field")
            return false
        }
        if(ctype.selectedItem.toString().equals("Select")){
            ctypeerror.setError("Fill this field")
            return false
        }
        if(cpro.selectedItem.toString().equals("Select")){
            cproerror.setError("Fill this field")
            return false
        }
        if(scomm.text.toString().trim().isEmpty()){
            scomm.setError("Fill this field")
            return false
        }
        if(plike.text.toString().trim().isEmpty()){
            plike.setError("Fill this field")
            return false
        }
        if(pcomnt.text.toString().trim().isEmpty()){
            pcomnt.setError("Fill this field")
            return false
        }
        if(exfeed.text.toString().trim().isEmpty()){
            exfeed.setError("Fill this field")
            return false
        }
        if(roomloc.text.toString().trim().isEmpty()){
            roomloc.setError("Fill this field")
            return false
        }
        if(spur.selectedItem.toString().equals("Select")){
            spurerror.setError("Fill this field")
            return false
        }

        return true
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    fun stateload(){
        statearr.clear()
        statearr_id.clear()
        val pDialog= ProgressDialog(this)
        pDialog.setCancelable(false)
        pDialog.setMessage("Loading...")
        pDialog.show()
        // pDialog= Dialog(this)
        //Appconstands.loading_show(this, pDialog).show()

        val call = ApproveUtils.Get.getstate(pref!!.getString("mobile","").toString())
        call.enqueue(object : Callback<Resp_otp> {
            override fun onResponse(call: Call<Resp_otp>, response: Response<Resp_otp>) {
                Log.e("response", response.toString())
                if (response.isSuccessful()) {
                    val example = response.body() as Resp_otp
                    println(example)
                    if (example.status == "1") {
                        statearr.add("Select State")
                        statearr_id.add("0")
                        var arr = example.message
                        var resp = example.response
                        for(i in 0 until resp!!.size){
                            statearr.add(resp[i].state_name.toString())
                            statearr_id.add(resp[i].id.toString())
                        }
                        val adap=ArrayAdapter(this@Add_Eod,R.layout.support_simple_spinner_dropdown_item,statearr)
                        state.adapter=adap

                        if(eid.isNotEmpty()){
                            for(j in 0 until statearr_id.size){
                                if(statearr_id[j]==estate){
                                    state.setSelection(j)
                                }
                            }


                        }

                        //finish()
                    } else {

                    }
                }
                // pDialog.dismiss()
                //loading_show(activity).dismiss()
                pDialog.dismiss()
            }

            override fun onFailure(call: Call<Resp_otp>, t: Throwable) {
                Log.e("Fail response", t.toString())
                pDialog.dismiss()
                if (t.toString().contains("time")) {
                    Toast.makeText(
                        this@Add_Eod,
                        "Poor network connection",
                        Toast.LENGTH_LONG
                    ).show()
                }
                //   pDialog.dismiss()
                //loading_show(activity).dismiss()
            }
        })
    }

    fun cityload(id:String){
        cityarr.clear()
        //statearr_id.clear()
        val pDialog= ProgressDialog(this)
        pDialog.setCancelable(false)
        pDialog.setMessage("Loading...")
        pDialog.show()
        // pDialog= Dialog(this)
        //Appconstands.loading_show(this, pDialog).show()

        val call = ApproveUtils.Get.geteodsatte(Appconstants.Domin+"eodCity/"+id)
        call.enqueue(object : Callback<Resp_otp> {
            override fun onResponse(call: Call<Resp_otp>, response: Response<Resp_otp>) {
                Log.e("response", response.toString())
                if (response.isSuccessful()) {
                    val example = response.body() as Resp_otp
                    println(example)
                    if (example.status == "Success") {
                        cityarr.add("Select City")
                        pDialog.dismiss()
                        var arr = example.message
                        var resp = example.response
                        for(i in 0 until resp!!.size){
                            cityarr.add(resp[i].city.toString())
                        }
                        val adap=ArrayAdapter(this@Add_Eod,R.layout.support_simple_spinner_dropdown_item,cityarr)
                        city.adapter=adap

                            if(eid.isNotEmpty()){
                                for(j in 0 until cityarr.size){
                                    if(cityarr[j]==ecity){
                                        city.setSelection(j)
                                    }
                                }


                            }


                        //finish()
                    } else {
                        pDialog.dismiss()

                    }
                }
                else{
                    pDialog.dismiss()

                }
                // pDialog.dismiss()
                //loading_show(activity).dismiss()
                pDialog.dismiss()
            }

            override fun onFailure(call: Call<Resp_otp>, t: Throwable) {
                Log.e("Fail response", t.toString())
                pDialog.dismiss()
                if (t.toString().contains("time")) {
                    Toast.makeText(
                        this@Add_Eod,
                        "Poor network connection",
                        Toast.LENGTH_LONG
                    ).show()
                }
                //   pDialog.dismiss()
                //loading_show(activity).dismiss()
            }
        })
    }

    override fun OnItemClick(view: View, position: Int, viewType: Int) {
        Person_Popup(position)
    }

    inner class AddEOD : AsyncTask<String, Void, String>() {
        override fun onPreExecute() {
            //progbar.setVisibility(View.VISIBLE);
            pDialog = Dialog(activity)
            Appconstands.loading_show(activity, pDialog).show()
            Log.i("LoginTask", "started")
        }

        override fun doInBackground(vararg param: String): String? {
            var result: String = ""
            val con = Connection()
            val obj = JSONObject()
            /*`trip_id`, `state`, `city`, `customer_name`, `email`, `mobile`, `address`, `sales_value`, `sq_value`, `in_time`, `out_time`,
            `magacine_capacity`, `turnover`, `customer_type`, `customer_prospect`, `sales_communication`, `product_like`, `product_comment`,
            `executive_feedback`, `room_location`, `season_purchase`, */
            if (!eid.isNullOrEmpty()) {obj.put("id", eid)}
            obj.put("trip_id", pref!!.getString("tid", ""))
            obj.put("state", state)
            obj.put("city", city.selectedItem.toString().trim())
            obj.put("customer_id", cid)
            obj.put("cname", cname.text.toString().trim())
            obj.put("mmob", mmob.text.toString().trim())
            obj.put("email", email.text.toString().trim())
            obj.put("maddress", maddress.text.toString().trim())
            obj.put("sval", tval.text.toString().trim())
            obj.put("sq_val", sqval.text.toString().trim())
            obj.put("intime", intime.text.toString().trim())
            obj.put("outtime", outtime.text.toString().trim())
            obj.put("magcapacity", magcapacity.text.toString().trim())
            obj.put("turnover", turnover.text.toString().trim())
            obj.put("tval", turnover.text.toString().trim())
            obj.put("ctype", ctype.selectedItem.toString())
            obj.put("cpro", cpro.selectedItem.toString())
            obj.put("scomm", scomm.text.toString().trim())
            obj.put("plike", plike.text.toString().trim())
            obj.put("pcomnt", pcomnt.text.toString().trim())
            obj.put("exfeed", exfeed.text.toString().trim())
            obj.put("roomloc", roomloc.text.toString().trim())
            obj.put("spur", spur.selectedItem.toString())
            obj.put("persons", persons)
            try {
                Log.i(
                        "add eod",
                        param[0] + "  save inputtt      " + obj.toString()
                )
                result = con.sendHttpPostjson(param[0], obj).toString()

            } catch (e: IOException) {
                e.printStackTrace();
            }

            return result
        }

        override fun onPostExecute(resp: String?) {
            pDialog.dismiss()
            try {
                println("resp : " + resp)
                /*{"status":"Success","message":"Customers added successfully.","response":[]}*/
                val example = JSONObject(resp!!)
                if (example!!.getString("status") == "Success") {
                    finish()
                    Toast.makeText(activity, example!!.getString("message"), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(activity, example!!.getString("message"), Toast.LENGTH_SHORT).show()
                }
            }catch (e: Exception){

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(resultCode){
            Activity.RESULT_OK -> {
                if (requestCode == 123) {
                    val customer_name = data!!.getStringExtra("customer_name")
                    cid = data!!.getStringExtra("id")!!
                    val mobile = data!!.getStringExtra("mobile")!!
                    val address = data!!.getStringExtra("address")!!
                    val ct = data!!.getStringExtra("city")!!
                    val stat = data!!.getStringExtra("state")!!

                    cname.setText(customer_name)
                    mmob.setText(mobile)
                    maddress.setText(address)
                    //city.setText(ct)
                    //state.setText(stat)
                }
            }
            else -> {
                /*if (requestCode==123) {
                    cname.setText("")
                    mmob.setText("")
                    maddress.setText("")
                    city.setText("")
                    state.setText("")
                }*/
            }
        }
    }
}