package com.elancier.team_j

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.elancier.team_j.Adapers.TravelAdap
import com.elancier.team_j.retrofit.*
import kotlinx.android.synthetic.main.activity_eod_list.*
import org.json.JSONObject
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Travel_list : AppCompatActivity(),TravelAdap.OnItemClickListener {
    val activity=this
    lateinit var pDialog : Dialog
    internal  var pref: SharedPreferences?=null
    internal  var editor: SharedPreferences.Editor?=null
    lateinit var adp : TravelAdap
    var eodlists = ArrayList<EOD_data>()//JSONArray()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eod_list)
        val ab = supportActionBar
        ab!!.setDisplayHomeAsUpEnabled(true)
        ab!!.setDisplayShowHomeEnabled(true)
        ab!!.title = "Travel"
        pDialog = Dialog(activity)
        pref = activity.getSharedPreferences("MyPref", 0)
        editor = pref!!.edit()

        val layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        recyclerview.layoutManager = layoutManager
        val eoddata=EOD_data()
        eoddata.cname="Madurai - Chennai"
        eoddata.mmob="01 MAY 2023"
        eoddata.tval="490 KM"
        eoddata.createdAt="Madurai\nDindugal\nTrichy\nVilupuram\nChennai."
        eodlists.add(eoddata)

        val eoddata1=EOD_data()
        eoddata1.cname="Chennai - Madurai"
        eoddata1.mmob="02 MAY 2023"
        eoddata1.tval="490 KM"
        eoddata1.createdAt="Chennai\nVilupuram\nTrichy\nDindugal\nMadurai."
        eodlists.add(eoddata1)

        adp = TravelAdap(activity,eodlists,activity)
        recyclerview.adapter = adp

        add.visibility=View.GONE

      /*  add.setOnClickListener {
            startActivity(
                Intent(activity, Schedule_Meeting::class.java)
            )
        }*/

       /* srchtxt.setOnKeyListener { view, i, keyEvent ->
            if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)) {
                if(srchtxt.text.toString().length>=2) {
                    getEODs(srchtxt.text.toString())
                    return@setOnKeyListener true
                }
                return@setOnKeyListener false
            }
            return@setOnKeyListener false
        }
        imageButton.setOnClickListener {
            srchtxt.setText("")
            getEODs("")
        }
        swiperefresh.setOnRefreshListener {
            Log.i("refresh", "onRefresh called from SwipeRefreshLayout")
            srchtxt.setText("")
            getEODs("")
            // This method performs the actual data-refresh operation.
            // The method calls setRefreshing(false) when it's finished.
        }*/


    }

    override fun onResume() {
        super.onResume()
        //getEODs("")
    }

    /*fun getEODs(search: String){
        eodlists.clear()
        adp()
        if (Appconstants.net_status(this)) {
            //getEOD().execute()
            val objs = JsonObject()
            objs.addProperty("trip",pref!!.getString("tid","")!!)//"9791981428"
            objs.addProperty("status",search)
            objs.addProperty("from",search_date.text.toString().trim())
            objs.addProperty("to",todate.text.toString().trim())
            println("obj : "+objs)
            pDialog =Dialog(activity)
            Appconstands.loading_show(activity, pDialog).show()
            val call = ApproveUtils.Get.getEod(pref!!.getString("tid","")!!,search_date.text.toString().trim(),todate.text.toString().trim(),search)
            call.enqueue(object : Callback<Resp_trip> {
                override fun onResponse(call: Call<Resp_trip>, response: Response<Resp_trip>) {
                    Log.e("responce", response.toString())
                    if (response.isSuccessful()) {
                        val example = response.body() as Resp_trip
                        println(example)
                        if (example.getStatus() == "Success") {
                            //eodlist = JSONArray()

                            for (r in 0 until example.getResponse()!!.size) {
                                val dt = EOD_data()
                                val obj = JSONObject()
                                val persons = JSONArray()
                                val res = example.getResponse()!!.get(r)
                                val id =  res.id!!
                                val state = if (res.state.isNullOrEmpty()) "" else res.state!!
                                val city = if (res.city.isNullOrEmpty()) "" else res.city!!
                                val customer_id = if (res.customer_id.isNullOrEmpty()) "" else res.customer_id!!
                                val cname = if (res.cname.isNullOrEmpty()) "" else res.cname!!
                                val mmob = if (res.mmob.isNullOrEmpty()) "" else res.mmob!!
                                val email = if (res.email.isNullOrEmpty()) "" else res.email!!
                                val maddress = if (res.maddress.isNullOrEmpty()) "" else res.maddress!!
                                val etval = if (res.tval.isNullOrEmpty()) "" else res.tval!!
                                val sval = if (res.sval.isNullOrEmpty()) "" else res.sval!!
                                val sq_val = if (res.sq_val.isNullOrEmpty()) "" else res.sq_val!!
                                val intime = if (res.intime.isNullOrEmpty()) "" else res.intime!!
                                val outtime = if (res.outtime.isNullOrEmpty()) "" else res.outtime!!
                                val magcapacity = if (res.magcapacity.isNullOrEmpty()) "" else res.magcapacity!!
                                val turnover = if (res.turnover.isNullOrEmpty()) "" else res.turnover!!
                                val ctype = if (res.ctype.isNullOrEmpty()) "" else res.ctype!!
                                val cpro = if (res.cpro.isNullOrEmpty()) "" else res.cpro!!
                                val scomm = if (res.scomm.isNullOrEmpty()) "" else res.scomm!!
                                val plike = if (res.plike.isNullOrEmpty()) "" else res.plike!!
                                val pcomnt = if (res.pcomnt.isNullOrEmpty()) "" else res.pcomnt!!
                                val exfeed = if (res.exfeed.isNullOrEmpty()) "" else res.exfeed!!
                                val roomloc = if (res.roomloc.isNullOrEmpty()) "" else res.roomloc!!
                                val spur = if (res.spur.isNullOrEmpty()) "" else res.spur!!
                                val created_at = if (res.createdAt.isNullOrEmpty()) "" else res.createdAt!!
                                //obj.put("trip_id",trip_id)

                                if (!res.persons.isNullOrEmpty()){
                                    for (p in 0 until res.persons!!.size){
                                        val ob = JSONObject()
                                        val data = res.persons!!.get(p)
                                        val ids = data.id!!
                                        val trip_id = if (data.trip_id.isNullOrEmpty())"" else data.trip_id!!
                                        val eod = if (data.eod.isNullOrEmpty())"" else data.eod!!
                                        val name = if (data.name.isNullOrEmpty())"" else data.name!!
                                        val designation = if (data.designation.isNullOrEmpty())"" else data.designation!!
                                        val mobile = if (data.mobile.isNullOrEmpty())"" else data.mobile!!
                                        ob.put("id",ids)
                                        ob.put("trip_id",trip_id)
                                        ob.put("eod",eod)
                                        ob.put("name",name)
                                        ob.put("mob",mobile)
                                        ob.put("desig",designation)
                                        persons.put(ob)
                                    }
                                }
                                obj.put("id",id)
                                obj.put("state",state)
                                obj.put("city",city)
                                obj.put("customer_id",customer_id)
                                obj.put("cname",cname)
                                obj.put("mmob",mmob)
                                obj.put("email",email)
                                obj.put("maddress",maddress)
                                obj.put("tval",etval)
                                obj.put("sval",sval)
                                obj.put("sq_val",sq_val)
                                obj.put("intime",intime)
                                obj.put("outtime",outtime)
                                obj.put("magcapacity",magcapacity)
                                obj.put("turnover",turnover)
                                obj.put("ctype",ctype)
                                obj.put("cpro",cpro)
                                obj.put("scomm",scomm)
                                obj.put("plike",plike)
                                obj.put("pcomnt",pcomnt)
                                obj.put("exfeed",exfeed)
                                obj.put("roomloc",roomloc)
                                obj.put("spur",spur)
                                obj.put("persons",persons)
                                obj.put("created_at",created_at)

                                dt.customerID = customer_id
                                dt.cname = cname
                                dt.mmob = mmob
                                dt.maddress = maddress
                                dt.city = city
                                dt.email = email
                                dt.state = state
                                dt.tval = etval
                                dt.sval = sval
                                dt.intime = intime
                                dt.outtime = outtime
                                dt.magcapacity = magcapacity
                                dt.ctype = ctype
                                dt.cpro = cpro
                                dt.scomm = scomm
                                dt.plike = plike
                                dt.pcomnt = pcomnt
                                dt.exfeed = exfeed
                                dt.roomloc = roomloc
                                dt.spur = spur
                                dt.createdAt = created_at
                                dt.jsonobject = obj
                                eodlists.add(dt)
                                //eodlist.put(obj)
                                adp()
                            }
                        } else {
                            Toast.makeText(activity, example.getMessage(), Toast.LENGTH_SHORT).show()
                        }
                    }
                    else{
                    }
                    adp()
                    pDialog.dismiss()
                    swiperefresh.isRefreshing=false
                }

                override fun onFailure(call: Call<Resp_trip>, t: Throwable) {
                    Log.e("areacode Fail response", t.toString())
                    if (t.toString().contains("time")) {
                        Toast.makeText(
                                activity,
                                "Poor network connection",
                                Toast.LENGTH_LONG
                        ).show()
                    }
                    adp()
                    pDialog.dismiss()
                    swiperefresh.isRefreshing=false
                }
            })
        }
        else{
            swiperefresh.isRefreshing=false
            Toast.makeText(
                    activity,
                    "You're offline",
                    Toast.LENGTH_LONG
            ).show()
        }
    }*/

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        /*if(callin.visibility==View.VISIBLE){
            callin.visibility=View.GONE
            search_date.text.clear()
            todate.text.clear()
            getEODs("")
        }
        else{
            finish()
        }*/
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.profile_page, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == android.R.id.home) {
            onBackPressed()
            return true
        }

        if (id == R.id.action_search){
            var src=""
            val searchViewItem = item //menu.findItem(R.id.action_search)
            val searchView = MenuItemCompat.getActionView(searchViewItem) as SearchView
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    searchView.clearFocus()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    //adapter.getFilter().filter(newText)
                    src = newText!!
                    if(newText!!.length>=3) {
                        //getEODs(newText.toString())
                    }
                    else if(newText.length==0){
                       // getEODs("")
                    }

                    return true
                }
            })

            /*searchView.getChildAt(1).setOnKeyListener { view, i, keyEvent ->
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)) {
                    if(searchView.query.length>=3) {
                        getEODs(searchView.query.toString())
                    }
                    return@setOnKeyListener true
                }
                return@setOnKeyListener false
            }*/
        }
        return super.onOptionsItemSelected(item)
    }

    inner class getEOD : AsyncTask<String, Void, String>() {
        override fun onPreExecute() {
            //progbar.setVisibility(View.VISIBLE);
            pDialog = Dialog(activity)
            Appconstands.loading_show(activity, pDialog).show()
            Log.i("LoginTask", "started")
        }

        override fun doInBackground(vararg param: String): String? {
            var result: String = ""
           // eodlist = JSONArray()
            val con = Connection()
            val obj = JSONObject()
            obj.put("user",pref!!.getString("tid",""))
            try {
                Log.i("eod list", Appconstants.eodList + "  save inputtt      " + obj.toString())
                result = con.sendHttpPostjson(Appconstants.eodList, obj).toString()

            } catch (e: IOException) {
                e.printStackTrace();
            }

            return result
        }

        override fun onPostExecute(resp: String?) {
            pDialog.dismiss()
            try {
                println("resp : " + resp)
                val example = JSONObject(resp!!)
                if (example.getString("Status") == "Success") {
                    //eodlist = example.getJSONArray("Response")
                    adp()
                } else {
                    Toast.makeText(activity, example.getString("message"), Toast.LENGTH_SHORT).show()
                }
            }catch (e: Exception){

            }
        }
    }

    override fun OnItemClick(view: View, position: Int, viewType: Int) {
        val it = Intent(activity,Add_Eod::class.java)
        it.putExtra("edit",eodlists[position].jsonobject.toString())
        startActivity(it)
    }

    fun filter() {
        callin.visibility=View.VISIBLE
        search_date.setOnClickListener {
            val c = Calendar.getInstance()
            val mYear = c.get(Calendar.YEAR)
            val mMonth = c.get(Calendar.MONTH)
            val mDay = c.get(Calendar.DAY_OF_MONTH)

            val cals = Calendar.getInstance()  // or pick another time zone if necessary
            cals.timeInMillis = System.currentTimeMillis()
            cals.set(Calendar.DAY_OF_MONTH, cals.get(Calendar.DAY_OF_MONTH) + 1)
            cals.set(Calendar.HOUR_OF_DAY, 0)
            cals.set(Calendar.MINUTE, 0)
            cals.set(Calendar.SECOND, 0)

            val datePickerDialog = DatePickerDialog(activity,

                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        search_date.setText(
                                dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year
                        )
                    }, mYear, mMonth, mDay
            )

            val simpledateformate = SimpleDateFormat("dd-MM-yyyy");
            // val date = simpledateformate.parse(datebilling.text.toString());
            //datePickerDialog.getDatePicker().setMinDate(cals.timeInMillis);

            datePickerDialog.show()

        }

        todate.setOnClickListener {
            val c = Calendar.getInstance()
            val mYear = c.get(Calendar.YEAR)
            val mMonth = c.get(Calendar.MONTH)
            val mDay = c.get(Calendar.DAY_OF_MONTH)

            val cals = Calendar.getInstance()  // or pick another time zone if necessary
            cals.timeInMillis = System.currentTimeMillis()
            cals.set(Calendar.DAY_OF_MONTH, cals.get(Calendar.DAY_OF_MONTH) + 1)
            cals.set(Calendar.HOUR_OF_DAY, 0)
            cals.set(Calendar.MINUTE, 0)
            cals.set(Calendar.SECOND, 0)

            val datePickerDialog = DatePickerDialog(activity,

                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        todate.setText(
                                dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year
                        )
                        if(search_date.text.toString().isEmpty()){
                            Toast.makeText(applicationContext, "Please select from date", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            //getEODs("")
                        }

                    }, mYear, mMonth, mDay


            )

            val simpledateformate = SimpleDateFormat("dd-MM-yyyy");
            // val date = simpledateformate.parse(datebilling.text.toString());
            //datePickerDialog.getDatePicker().setMinDate(cals.timeInMillis);

            datePickerDialog.show()

        }

    }

    fun adp(){
        //adp = EODAdap(activity,eodlist,activity)
        //recyclerview.adapter = adp
        adp.notifyDataSetChanged()
        if (eodlists.size==0){
            nodata.visibility=View.VISIBLE
        }else{
            nodata.visibility=View.GONE
        }
    }

    inner class EOD_data(){
        var customerID: Any? = null
        var cname: String? = null
        var mmob: String? = null
        var maddress: String? = null
        var city: String? = null
        var email: String? = null
        var state: String? = null
        var tval: String? = null
        var sval: String? = null
        var intime: String? = null
        var outtime: String? = null
        var magcapacity: String? = null
        var ctype: String? = null
        var cpro: String? = null
        var scomm: String? = null
        var plike: String? = null
        var pcomnt: String? = null
        var exfeed: String? = null
        var roomloc: String? = null
        var spur: String? = null
        var createdAt: String? = null
        var jsonobject : JSONObject? = null
    }

}