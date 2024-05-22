package com.elancier.team_j


import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.DialogInterface.OnMultiChoiceClickListener
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ListView
import android.widget.SearchView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnCalendarPageChangeListener
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.elancier.domdox.Common.CommonFunctions
import com.elancier.team_j.Adapers.ExpenseListAdapter
import com.elancier.team_j.Adapers.MyTourAdap
import com.elancier.team_j.Adapers.MyTransactionAdap
import com.elancier.team_j.Adapers.RequestListAdapter
import com.elancier.team_j.Adapers.TransacAdapter_Products
import com.elancier.team_j.DataClass.OrderDetail
import com.elancier.team_j.retrofit.Appconstants
import com.elancier.team_j.retrofit.ApproveUtils
import com.elancier.team_j.retrofit.Resp_otp
import com.elancier.team_j.retrofit.Resp_trip
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_tour__list.add_family
import kotlinx.android.synthetic.main.activity_tour__list.memberslist
import kotlinx.android.synthetic.main.activity_tour__list.myCalendar
import kotlinx.android.synthetic.main.activity_tour__list.shimmer_view_container
import kotlinx.android.synthetic.main.activity_tour__list.textView23
import kotlinx.android.synthetic.main.request_add.cityspin
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Collections


class Tour_List : AppCompatActivity(),RequestListAdapter.OnItemClickListener,ExpenseListAdapter.OnItemClickListener,DatePickerDialog.OnDateSetListener {
    internal lateinit var editor: SharedPreferences.Editor
    internal lateinit var pref: SharedPreferences
    internal lateinit var fname: Spinner
    internal lateinit var fcode: EditText
    internal lateinit var fage: EditText
    var CentresArrays = ArrayList<OrderDetail>()
    var CentresArraysdup = ArrayList<OrderDetail>()
    val activity = this

    var CentresArraysdup1 = ArrayList<OrderDetail>()
    var docidsArr=""
    lateinit var adpdup : MyRequestAdap
    lateinit var adp1dup : MyExpAdap
    lateinit var adp: MyTransactionAdap
    lateinit var adp1: MyExpAdap
    var cusid = ""
    lateinit var click1: MyExpAdap.OnItemClickListener
    var pop: AlertDialog? = null
    var flag=""
    internal lateinit var histlist: ListView
    internal var catidarr: MutableList<String> = ArrayList()
    internal var staffnmarrdup: MutableList<String> = ArrayList()
    internal var imgarr: MutableList<String> = ArrayList()
    internal lateinit var progbar: Dialog
    internal lateinit var checklist: MutableList<String>
    lateinit var searchView: SearchView
    var catid = ""
    var customerid = ""
    var catnm = ""
    var title: TextView? = null
    var save: TextView? = null
    var refed: TextView? = null
    var ordamt: TextView? = null
    var notes: TextInputEditText? = null
    var image1: ImageView? = null
    var image2: ImageView? = null
    var userID = ""

    val RequestPermissionCode = 7
    internal lateinit var byteArray: ByteArray
    var imagecode = ""
    internal lateinit var fi: File
    var panimage = ""
    var bankimage = ""
    var frm = ""
    lateinit var update:Dialog
    internal lateinit var byteArray1: ByteArray
    var imagecode1 = ""
    internal lateinit var fi1: File
    lateinit var pDialog: ProgressDialog
    internal var cdarr: MutableList<String> = ArrayList()

    var Requestlist = ArrayList<OrderDetail>()
    var RequestlistView = ArrayList<OrderDetail>()
    var datelist = ArrayList<String>()
    lateinit var adps : MyTourAdap
    lateinit var adps1 : TransacAdapter_Products
    lateinit var adpe : ExpenseListAdapter
    lateinit var  bottomSheetTeachersDialog:BottomSheetDialog

    var day = 0
    var month = 0
    var year= 0

    var minute= 0
    var filter=""

    var curmonth=""

    internal var nmarr: MutableList<String> = java.util.ArrayList()
    internal var idarr: MutableList<String> = java.util.ArrayList()

    internal var nmarr_city: MutableList<String> = java.util.ArrayList()
    internal var idarr_city: MutableList<String> = java.util.ArrayList()


    internal var nmarr_timing: MutableList<String> = java.util.ArrayList()
    internal var idarr_timimg: MutableList<String> = java.util.ArrayList()

    lateinit var selectedLanguage: BooleanArray
    var langList = ArrayList<Int>()
    var DoctorName = java.util.ArrayList<String>()
    var DoctorIdName = java.util.ArrayList<String>()

    internal lateinit var picker: DatePickerDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tour__list)


        try {
            var frms = intent.extras
            frm = frms!!.getString("cus").toString()

        } catch (e: Exception) {

        }

        shimmer_view_container.visibility = View.VISIBLE
        shimmer_view_container.startShimmer()

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val ab = supportActionBar
        ab!!.setDisplayHomeAsUpEnabled(true)
        ab!!.setDisplayShowHomeEnabled(true)

       add_family.visibility=View.VISIBLE

        ab!!.title = "Tour Schedules"
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        catidarr = ArrayList<String>()
        nmarr = ArrayList<String>()
        idarr = ArrayList<String>()
        nmarr_city = ArrayList<String>()
        idarr_city = ArrayList<String>()
        imgarr = ArrayList<String>()
        cdarr = ArrayList<String>()
        checklist = ArrayList<String>()

        pref = applicationContext.getSharedPreferences("MyPref", 0)
        editor = pref.edit()
        userID = pref!!.getString("empid", "").toString()

        pDialog = ProgressDialog(this)
        pDialog!!.setMessage("Processing...")
        pDialog.show()

        click1 = object : MyExpAdap.OnItemClickListener {
            override fun OnItemClick(view: View, position: Int, viewType: Int) {

            }

        }





        add_family.setOnClickListener {
            showbottomsheet()
        }



        // initializing our text views and image views.

        // initializing our text views and image views.


        myCalendar.setHeaderColor(resources.getColor(R.color.colorPrimary))



        myCalendar.setOnDayClickListener(object : OnDayClickListener {
            override fun onDayClick(eventDay: EventDay) {
                val clickedDayCalendar = eventDay.calendar
                RequestlistView.clear()

                var foramt = SimpleDateFormat("dd-MM-yyyy")
                val date = foramt.format(clickedDayCalendar.time)
                //Toast.makeText(applicationContext,date.toString(),Toast.LENGTH_SHORT).show()

                for(i in 0 until datelist.size){
                    if(date.equals(datelist[i])){
                        var order=OrderDetail()
                        order.user=Requestlist[i].user
                        order.adrs_type=Requestlist[i].adrs_type
                        order.id=Requestlist[i].id
                        order.customer_id=Requestlist[i].customer_id
                        order.created_at=Requestlist[i].created_at
                        order.trip_timing=Requestlist[i].trip_timing
                        order.city=Requestlist[i].city
                        RequestlistView.add(order)

                    }
                }
                if(RequestlistView.size>0) {
                    adps = MyTourAdap(RequestlistView, activity, frm)
                    memberslist.adapter = adps
                    textView23.visibility=View.GONE

                }
                else{
                    try {
                        adps.notifyDataSetChanged()
                    }catch (e:Exception){

                    }
                    textView23.visibility=View.VISIBLE
                }


            }
        })

        myCalendar.setOnPreviousPageChangeListener(object : OnCalendarPageChangeListener {
            override fun onChange() {

                var foramt = SimpleDateFormat("yyyy-MM")
                val date = foramt.format(myCalendar.currentPageDate.time)
                curmonth=date
                RequestlistView.clear()
                adps = MyTourAdap(RequestlistView, activity, frm)
                memberslist.adapter = adps
                //Toast.makeText(this@Tour_List, date.toString(), Toast.LENGTH_SHORT).show()
                Customers()
                flag="prev"
            }
        })

        myCalendar.setOnForwardPageChangeListener(object : OnCalendarPageChangeListener {
            override fun onChange() {
                var foramt = SimpleDateFormat("yyyy-MM")
                val date = foramt.format(myCalendar.currentPageDate.time)
                curmonth=date
                RequestlistView.clear()
                adps = MyTourAdap(RequestlistView, activity, frm)
                memberslist.adapter = adps
                //Toast.makeText(this@Tour_List, date.toString(), Toast.LENGTH_SHORT).show()
                Customers()
                flag="next"


            }
        });


        // Get list of event with detail

        // Get list of event with detail
//        myCalendar.getEventList { }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        //menuInflater.inflate(R.menu.receipt, menu)

        return true
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

    fun types(){
        if (Appconstants.net_status(this)) {
            nmarr.clear()
            idarr.clear()
            val call:Call<Resp_trip>

            call = ApproveUtils.Get.getcattype()

            call.enqueue(object : Callback<Resp_trip> {
                override fun onResponse(call: Call<Resp_trip>, response: Response<Resp_trip>) {
                    Log.e("areacode responce", response.toString())
                    if (response.isSuccessful()) {
                        val example = response.body() as Resp_trip
                        println(example)
                        if (example.getStatus() == "Success") {


                            nmarr.add("Select Type")
                            idarr.add("0")
                            //  textView23.visibility= View.GONE
                            var otpval=example.getResponse()
                            for(i in 0 until otpval!!.size) {
                                val id=otpval[i].id.toString()
                                val type=otpval[i].name.toString()
                                nmarr.add(type)
                                idarr.add(id)

                            }

                            pDialog.dismiss()

                        } else {
                            /*Toast.makeText(this@Request_Add, example.getStatus(), Toast.LENGTH_SHORT)
                                .show()*/
                        }
                    }
                    else{

                    }
                }

                override fun onFailure(call: Call<Resp_trip>, t: Throwable) {
                    Log.e("areacode Fail response", t.toString())
                    if (t.toString().contains("time")) {
                        Toast.makeText(
                            this@Tour_List,
                            "Poor network connection",
                            Toast.LENGTH_LONG
                        ).show()

                    }
                }
            })
        }
        else{
            Toast.makeText(
                this@Tour_List,
                "You're offline",
                Toast.LENGTH_LONG
            ).show()

        }
    }

    fun city(){
        if (Appconstants.net_status(this)) {
            nmarr_city.clear()
            idarr_city.clear()
            val call:Call<Resp_trip>

            var json=JsonObject()
            json.addProperty("city",pref!!.getString("city",""))
            call = ApproveUtils.Get.getCities(json)

            call.enqueue(object : Callback<Resp_trip> {
                override fun onResponse(call: Call<Resp_trip>, response: Response<Resp_trip>) {
                    Log.e("areacode responce", response.toString())
                    if (response.isSuccessful()) {
                        val example = response.body() as Resp_trip
                        println(example)
                        if (example.getStatus() == "Success") {


                            nmarr_city.add("Select City")
                            idarr_city.add("0")
                            //  textView23.visibility= View.GONE
                            var otpval=example.getResponse()
                            for(i in 0 until otpval!!.size) {
                                val id=otpval[i].id.toString()
                                val type=otpval[i].name.toString()
                                nmarr_city.add(type)
                                idarr_city.add(id)

                            }

                            pDialog.dismiss()

                        } else {
                            /*Toast.makeText(this@Request_Add, example.getStatus(), Toast.LENGTH_SHORT)
                                .show()*/
                        }
                    }
                    else{

                    }
                }

                override fun onFailure(call: Call<Resp_trip>, t: Throwable) {
                    Log.e("areacode Fail response", t.toString())
                    if (t.toString().contains("time")) {
                        Toast.makeText(
                            this@Tour_List,
                            "Poor network connection",
                            Toast.LENGTH_LONG
                        ).show()

                    }
                }
            })
        }
        else{
            Toast.makeText(
                this@Tour_List,
                "You're offline",
                Toast.LENGTH_LONG
            ).show()

        }
    }

    fun trips(){
        if (Appconstants.net_status(this)) {
            nmarr_timing.clear()
            idarr_timimg.clear()
            val call:Call<Resp_trip>

            call = ApproveUtils.Get.getTriptiming()

            call.enqueue(object : Callback<Resp_trip> {
                override fun onResponse(call: Call<Resp_trip>, response: Response<Resp_trip>) {
                    Log.e("areacode responce", response.toString())
                    if (response.isSuccessful()) {
                        val example = response.body() as Resp_trip
                        println(example)
                        if (example.getStatus() == "Success") {

                            nmarr_timing.add("Select Timing")
                            idarr_timimg.add("0")
                            //  textView23.visibility= View.GONE
                            var otpval=example.getResponse()
                            for(i in 0 until otpval!!.size) {
                                val id=otpval[i].id.toString()
                                val type=otpval[i].name.toString()
                                nmarr_timing.add(type)
                                idarr_timimg.add(id)

                            }

                            pDialog.dismiss()

                        } else {
                            /*Toast.makeText(this@Request_Add, example.getStatus(), Toast.LENGTH_SHORT)
                                .show()*/
                        }
                    }
                    else{

                    }
                }

                override fun onFailure(call: Call<Resp_trip>, t: Throwable) {
                    Log.e("areacode Fail response", t.toString())
                    if (t.toString().contains("time")) {
                        Toast.makeText(
                            this@Tour_List,
                            "Poor network connection",
                            Toast.LENGTH_LONG
                        ).show()

                    }
                }
            })
        }
        else{
            Toast.makeText(
                this@Tour_List,
                "You're offline",
                Toast.LENGTH_LONG
            ).show()

        }
    }


    fun addTrip(type:String,doctor:String,date:String,city:String,timimg:String){
        if (Appconstants.net_status(this)) {
            pDialog = ProgressDialog(this)
            pDialog!!.setMessage("Creating Trip...")
            pDialog.show()

            val call:Call<Resp_trip>

            var json=JsonObject()
            json.addProperty("user_id",pref!!.getString("empid",""))
            json.addProperty("type",type)
            json.addProperty("doctor",doctor)
            json.addProperty("city",city)
            json.addProperty("trip_date",date)
            json.addProperty("trip_timing",timimg)
            call = ApproveUtils.Get.addTrip(json)

            call.enqueue(object : Callback<Resp_trip> {
                override fun onResponse(call: Call<Resp_trip>, response: Response<Resp_trip>) {
                    Log.e("areacode responce", response.toString())
                    if (response.isSuccessful()) {
                        val example = response.body() as Resp_trip
                        println(example)
                        if (example.getStatus() == "Success") {
                            bottomSheetTeachersDialog.dismiss()
                            Toast.makeText(applicationContext,example.getMessage(),Toast.LENGTH_SHORT).show()
                            pDialog.dismiss()
                            Customers()
                            flag="next"


                        } else {
                            /*Toast.makeText(this@Request_Add, example.getStatus(), Toast.LENGTH_SHORT)
                                .show()*/
                        }
                    }
                    else{

                    }
                }

                override fun onFailure(call: Call<Resp_trip>, t: Throwable) {
                    Log.e("areacode Fail response", t.toString())
                    if (t.toString().contains("time")) {
                        Toast.makeText(
                            this@Tour_List,
                            "Poor network connection",
                            Toast.LENGTH_LONG
                        ).show()

                    }
                }
            })
        }
        else{
            Toast.makeText(
                this@Tour_List,
                "You're offline",
                Toast.LENGTH_LONG
            ).show()

        }
    }




    fun Doctors() {
        if (Appconstants.net_status(this)) {
            CentresArrays.clear()
            DoctorName.clear()
            val call = ApproveUtils.Get.getcustripsnew(userID)
            call.enqueue(object : Callback<Resp_trip> {
                override fun onResponse(call: Call<Resp_trip>, response: Response<Resp_trip>) {
                    Log.e("areacode responce", response.toString())
                    if (response.isSuccessful()) {
                        pDialog.dismiss()
                        val example = response.body() as Resp_trip
                        println(example)
                        if (example.getStatus() == "Success") {
                            var otpval = example.getResponse()
                            for (i in 0 until otpval!!.size) {
                                val data = OrderDetail()
                                data.customer_name = otpval[i].name
                                data.id = otpval[i].id.toString()
                                data.mobile = otpval[i].mobile.toString()

                                DoctorName.add(data.customer_name.toString())
                                DoctorIdName.add(data.id.toString())
                                CentresArrays.add(data)
                                selectedLanguage = BooleanArray(DoctorName.size)




                            }


                        } else {
                            Toast.makeText(
                                this@Tour_List,
                                example.getMessage(),
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            pDialog.dismiss()


                        }
                    } else {

                    }
                }

                override fun onFailure(call: Call<Resp_trip>, t: Throwable) {
                    pDialog.dismiss()
                    Log.e("areacode Fail response", t.toString())
                    if (t.toString().contains("time")) {
                        Toast.makeText(
                            this@Tour_List,
                            "Poor network connection",
                            Toast.LENGTH_LONG
                        ).show()


                    }
                }
            })
        } else {
            Toast.makeText(
                this@Tour_List,
                "You're offline",
                Toast.LENGTH_LONG
            ).show()

        }
    }



    fun showbottomsheet(){
        bottomSheetTeachersDialog=  BottomSheetDialog(this@Tour_List, R.style.BottomSheetDialogTheme)

        // passing a layout file for our bottom sheet dialog.

        // passing a layout file for our bottom sheet dialog.
        val layout: View =LayoutInflater.from(this).inflate(R.layout.dialog_add_trip,null,false)

        // passing our layout file to our bottom sheet dialog.

        // passing our layout file to our bottom sheet dialog.
        bottomSheetTeachersDialog.setContentView(layout)

        // below line is to set our bottom sheet dialog as cancelable.

        // below line is to set our bottom sheet dialog as cancelable.
        bottomSheetTeachersDialog.setCancelable(false)

        // below line is to set our bottom sheet cancelable.

        // below line is to set our bottom sheet cancelable.
        bottomSheetTeachersDialog.setCanceledOnTouchOutside(true)

        // below line is to display our bottom sheet dialog.

        // below line is to display our bottom sheet dialog.
        bottomSheetTeachersDialog.show()


        var close=layout.findViewById<ImageButton>(R.id.close)
        var date=layout.findViewById<TextView>(R.id.exe_name)
        var triptype=layout.findViewById<Spinner>(R.id.state)
        var doctors=layout.findViewById<TextView>(R.id.doctorlist)
        var city=layout.findViewById<Spinner>(R.id.city)
        var triptiming=layout.findViewById<Spinner>(R.id.triptiming)
        var submit=layout.findViewById<TextView>(R.id.submit)

        if(DoctorName.isEmpty()){
            doctors.setHint("No Doctors Available")
            doctors.setEnabled(false)
        }


        submit.setOnClickListener {
            if(date.text.isNotEmpty()&&triptype.selectedItemPosition!=0&&
                doctors.text.isNotEmpty()&&triptiming.selectedItemPosition!=0){
                addTrip(idarr[triptype.selectedItemPosition].toString(),docidsArr,date.text.toString(),idarr_city[city.selectedItemPosition].toString(),idarr_timimg[triptiming.selectedItemPosition])
            }
            else{
                if(date.text.isEmpty()){
                    date.setError("Required field*")
                }
                if(triptype.selectedItemPosition==0){
                    triptype.performClick()
                    Toast.makeText(applicationContext,"Select Trip",Toast.LENGTH_SHORT).show()
                }
                if(doctors.text.isEmpty()){
                    doctors.setError("Required field*")
                }
                if(city.selectedItemPosition==0){
                    city.performClick()
                    Toast.makeText(applicationContext,"Select City",Toast.LENGTH_SHORT).show()
                }
                if(triptiming.selectedItemPosition==0){
                    triptiming.performClick()
                    Toast.makeText(applicationContext,"Select Timing",Toast.LENGTH_SHORT).show()
                }
            }
        }

        close.setOnClickListener {
            bottomSheetTeachersDialog.dismiss()
        }

        date.setOnClickListener {
            val cldr = Calendar.getInstance()
            val day = cldr.get(Calendar.DAY_OF_MONTH)
            val month = cldr.get(Calendar.MONTH)
            val year = cldr.get(Calendar.YEAR)
            // date picker dialog
            picker = DatePickerDialog(this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    date.setText(year.toString()+ "-" +(monthOfYear + 1) + "-"+dayOfMonth.toString())
                    date.setError(null)
                }, year, month, day
            )
            picker.show()
        }

        if(nmarr.isNotEmpty()){
            var adap=ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,nmarr)
            triptype.setAdapter(adap)
        }

        if(nmarr_city.isNotEmpty()){
            var adap=ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,nmarr_city)
            city.setAdapter(adap)
        }

        if(nmarr_timing.isNotEmpty()){
            var adap=ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,nmarr_timing)
            triptiming.setAdapter(adap)
        }



        doctors.setOnClickListener(View.OnClickListener { // Initialize alert dialog
            val builder = AlertDialog.Builder(this@Tour_List)

            // set title
            builder.setTitle("Select Doctor")

            // set dialog non cancelable
            builder.setCancelable(false)

            builder.setMultiChoiceItems(DoctorName.toTypedArray(), selectedLanguage,
                OnMultiChoiceClickListener { dialogInterface, i, b ->
                    // check condition
                    if (b) {
                        // when checkbox selected
                        // Add position  in lang list
                        langList.add(i)
                        // Sort array list
                        Collections.sort(langList)
                    } else {
                        // when checkbox unselected
                        // Remove position from langList
                        langList.remove(Integer.valueOf(i))
                    }
                })
            builder.setPositiveButton(
                "OK"
            ) { dialogInterface, i -> // Initialize string builder
                val stringBuilder = StringBuilder()
                val stringBuilder1 = StringBuilder()
                // use for loop
                for (j in langList.indices) {
                    // concat array value
                    stringBuilder.append(DoctorName.toTypedArray().get(langList[j]))
                    stringBuilder1.append(DoctorIdName.toTypedArray().get(langList[j]))
                    // check condition
                    if (j != langList.size - 1) {
                        // When j value  not equal
                        // to lang list size - 1
                        // add comma
                        stringBuilder.append(", ")
                        stringBuilder1.append(", ")
                    }
                }
                // set text on textView
                doctors.setText(stringBuilder.toString())
                doctors.setError(null)

                docidsArr=stringBuilder1.toString()
                //Toast.makeText(applicationContext,docidsArr,Toast.LENGTH_SHORT).show()
            }
            builder.setNegativeButton(
                "Cancel"
            ) { dialogInterface, i -> // dismiss dialog
                dialogInterface.dismiss()
            }
            builder.setNeutralButton(
                "Clear All"
            ) { dialogInterface, i ->
                // use for loop
                for (j in 0 until selectedLanguage.size) {
                    // remove all selection
                    selectedLanguage[j] = false
                    // clear language list
                    langList.clear()
                    // clear text view value
                    doctors.setText("")
                }
            }
            // show dialog
            builder.show()
        })






    }

    fun toast(msg: String) {
        val toast = Toast.makeText(this@Tour_List, msg, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    /*fun editarea(brcode: String, anm: String, acode: String, aid: String, short: String) {
        var brid = ""
        var nmarr = ArrayList<String>()
        var idarr = ArrayList<String>()
        nmarr.add("Select")
        idarr.add("0")

        val dialogBuilder = AlertDialog.Builder(this@Tour_List)
// ...Irrelevant code for customizing the buttons and title
        val inflater = this@Tour_List.getLayoutInflater()
        val dialogView = inflater.inflate(R.layout.addchannels_popup, null)
        fname = dialogView.findViewById<Spinner>(R.id.fname);
        fcode = dialogView.findViewById<EditText>(R.id.fcode);
        val tit = dialogView.findViewById<TextView>(R.id.tit);
        fage = dialogView.findViewById<EditText>(R.id.fage);
        val cancel = dialogView.findViewById<ImageButton>(R.id.cancel);
        val add = dialogView.findViewById<Button>(R.id.button)
        tit.setText("Edit VIllage")
        dialogBuilder.setView(dialogView)
        dialogBuilder.setCancelable(true)
        add.text = "Update"
        pop = dialogBuilder.create()
        pop!!.show()

        fage.setText(acode)
        //fageshort.setText(short)

        fcode.setText(anm)

        cancel.setOnClickListener {
            pop!!.dismiss();
            //doLcatload(type.toString(), "1")

        }
        add.setOnClickListener {

            if (fcode.text.toString().trim().isNotEmpty() && fage.text.toString().trim()
                    .isNotEmpty()
            ) {
                val type = pref.getString("id", "")
                progbar = Dialog(this@Tour_List)
                progbar.requestWindowFeature(Window.FEATURE_NO_TITLE)
                progbar.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                progbar.setContentView(R.layout.save)
                progbar.setCancelable(false)
                progbar.show()




                if (Appconstants.net_status(this)) {

                } else {
                    Toast.makeText(applicationContext, "You're offline", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(applicationContext, "Please fill all fields", Toast.LENGTH_SHORT)
                    .show()
            }

        }


    }*/



    override fun onResume() {
        super.onResume()
        textView23.visibility=View.GONE
        CentresArrays.clear()
        CentresArraysdup.clear()
        CentresArraysdup1.clear()
        RequestlistView.clear()
        Requestlist.clear()
        datelist.clear()
        try {
            var foramt = SimpleDateFormat("yyyy-MM")
            var date = foramt.format(myCalendar.currentPageDate.time)
            curmonth = date

        }
        catch (e:Exception){

        }
        Customers()
        types()
        city()
        Doctors()
        trips()
        flag="resume"



    }


    fun Customers(){
        if (Appconstants.net_status(this)) {
            val tid = pref.getString("empid", "").toString()
            Requestlist.clear()
            RequestlistView.clear()
            datelist.clear()

            val calls = ApproveUtils.Get.gettourlist(tid,curmonth)
            val objs = JSONObject()
            objs.put("tid",tid)
            println("tourid"+tid)
            calls!!.enqueue(object : Callback<Resp_otp?> {
                override fun onResponse(call: Call<Resp_otp?>, responses: Response<Resp_otp?>) {
                    Log.e(objs.toString(),responses.toString())
                    if (responses.isSuccessful) {
                        val resp = responses.body()
                        val status = resp!!.status
                        if (status == "Success") {
                            var response=resp.response
                            for(i in 0 until response!!.size){
                                datelist.add(response[i].trip_date.toString())
                                var order=OrderDetail()
                                order.id=response[i].id
                                order.customer_id=response[i].user_id
                                order.adrs_type=response[i].type
                                order.city=response[i].city
                                order.created_at=response[i].trip_date
                                order.trip_timing=response[i].trip_timing
                                order.user=response[i].doctor
                                Requestlist.add(order)
                            }

                        } else {
                            //textView23.visibility = View.VISIBLE
                        }
                        /*adps = MyTourAdap(Requestlist, activity,frm)
                        memberslist.adapter = adps*/


                        val events: MutableList<EventDay> = ArrayList()


                        for(i in 0 until datelist.size){
                            val calendar = Calendar.getInstance()

                            var date = datelist[i]
                            var split=date.split("-")
                            var day=split[0]
                            var month=split[1].replace("0","")
                            var year=split[2]


                            if(month.toInt()>1){
                                calendar.set(year.toInt(),month.toInt()-1,day.toInt());

                            }
                            else
                            {
                                calendar.set(year.toInt(),month.toInt(),day.toInt());

                            }
                            events.add(EventDay(calendar, R.drawable.ic_bag, Color.parseColor("#BF1613")))
                        }




                        myCalendar.setEvents(events)


                        var cal=Calendar.getInstance()
                        var foramt = SimpleDateFormat("dd-MM-yyyy")
                        val date = foramt.format(cal.time)
                        //Toast.makeText(applicationContext,date.toString(),Toast.LENGTH_SHORT).show()

                        for(i in 0 until datelist.size){
                            if(date.equals(datelist[i])){
                                var order=OrderDetail()
                                order.user=Requestlist[i].user
                                order.adrs_type=Requestlist[i].adrs_type
                                order.id=Requestlist[i].id
                                order.customer_id=Requestlist[i].customer_id
                                order.created_at=Requestlist[i].created_at
                                order.trip_timing=Requestlist[i].trip_timing
                                order.city=Requestlist[i].city
                                RequestlistView.add(order)

                            }
                        }
                        if(RequestlistView.size>0) {
                            adps = MyTourAdap(RequestlistView, activity, frm)
                            memberslist.adapter = adps
                            textView23.visibility=View.GONE

                        }
                        else{
                            try {
                                adps.notifyDataSetChanged()
                            }catch (e:Exception){

                            }

                            if(flag=="resume") {
                                textView23.visibility = View.VISIBLE
                                textView23.setText("No tours found on today date")
                            }
                        }
                        /*
                        "id": 25,
                        "type": "Advance Request",
                        "name": null,
                        "user": "9894940560",
                        "tid": "V00018",
                        "image": "http://teamdev.co.in/vanitha/public/uploads/request/9209image.png",
                        "image1": "http://teamdev.co.in/vanitha/public/uploads/request/5000image1.png",
                        "description": null,
                        "amount": "6500",
                        "status": "Pending",
                        "approved_amount": "",
                        "notes": "",
                        "approved_date": "",
                        "tour_plan": [],
                        "expenditure_plan": []*/
                    } else {
                        //textView23.visibility = View.VISIBLE
                    }
                    shimmer_view_container.stopShimmer()
                    shimmer_view_container.visibility = View.GONE


                }

                override fun onFailure(call: Call<Resp_otp?>, t: Throwable) {
                    shimmer_view_container.stopShimmer()
                    shimmer_view_container.visibility = View.GONE
                    //textView23.visibility = View.VISIBLE
                }

            })
        }else{
            Toast.makeText(
                this@Tour_List,
                "You're offline",
                Toast.LENGTH_LONG
            ).show()
            shimmer_view_container.visibility=View.GONE
        }
    }

   /* fun Orders(){
        if (Appconstants.net_status(this)) {
            CentresArrays.clear()
            val tid = pref.getString("cusid", "").toString()
            Log.e("tid",tid)

            *//*val call = ApproveUtils.Get.getexp(pref.getString("tid", "").toString())
            call.enqueue(object : Callback<Resp_trip> {
                override fun onResponse(call: Call<Resp_trip>, response: Response<Resp_trip>) {
                    Log.e("areacode responce", response.toString())
                    if (response.isSuccessful()) {
                        val example = response.body() as Resp_trip
                        println(example.getMessage())
                        println(example.getStatus())
                        if (example.getStatus() == "Success") {
                            textView23.visibility = View.GONE
                            var otpval = example.getResponse()
                            for (i in 0 until otpval!!.size) {
                                val data = OrderDetail()
                                data.customer_name = otpval[i].name.toString()
                                data.id = otpval[i].id.toString()
                                data.mobile = otpval[i].amount.toString()
                                data.address = otpval[i].description.toString()
                                data.shopnm = otpval[i].notes.toString()
                                data.city = otpval[i].type

                                data.image = otpval[i].image
                                data.image1 = otpval[i].image1
                                println("image" + data.image)
                                CentresArrays.add(data)
                            }
                            adp1 = MyExpAdap(
                                    CentresArrays,
                                    this@Request_List,
                                    click1
                            )
                            memberslist.adapter = adp1
                            shimmer_view_container.stopShimmer()

                            shimmer_view_container.visibility = View.GONE


                            if (CentresArrays.isEmpty()) {
                                textView23.visibility = View.VISIBLE

                            }

                        } else {
                            *//**//*Toast.makeText(this@Request_List, example.getStatus(), Toast.LENGTH_SHORT)
                                .show()*//**//*
                            shimmer_view_container.visibility = View.GONE
                            textView23.visibility = View.VISIBLE
                            shimmer_view_container.stopShimmer()

                        }
                    } else {
                        shimmer_view_container.visibility = View.GONE
                        textView23.visibility = View.VISIBLE
                        shimmer_view_container.stopShimmer()
                    }
                }

                override fun onFailure(call: Call<Resp_trip>, t: Throwable) {
                    Log.e("areacode Fail response", t.toString())
                    if (t.toString().contains("time")) {
                        Toast.makeText(
                                this@Request_List,
                                "Poor network connection",
                                Toast.LENGTH_LONG
                        ).show()
                        shimmer_view_container.visibility = View.GONE
                        shimmer_view_container.stopShimmer()


                    }
                }
            })*//*

            *//* "id": 32,
            "type": "Stay",
            "name": null,
            "user": "9894940560",
            "tid": "V00018",
            "image": "http://teamdev.co.in/vanitha/public/uploads/expense/3173image.png",
            "image1": "http://teamdev.co.in/vanitha/public/uploads/expense/6457image1.png",
            "hotel_name": "GRT Hotel",
            "city": "",
            "from": null,
            "to": null,
            "by": null,
            "description": null,
            "amount": "2000"*//*

            val calls = ApproveUtils.Get.getorders_list(tid,pref.getString("empid", "").toString())
            val objs = JSONObject()
            objs.put("tid",tid)
            calls.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, responses: Response<ResponseBody>) {
                    Log.e(objs.toString(),responses.toString())
                    if (responses.isSuccessful) {
                        val resp = responses.body()?.string()
                        val obj = JSONObject(resp.toString())
                        val status = obj.getString("status")
                        val message = obj.getString("message")
                        if (status == "Success") {
                            val response = obj.getJSONArray("response")
                            Requestlist = response
                        } else {
                            textView23.visibility = View.VISIBLE
                        }
                        adps = RequestListAdapter(Requestlist, activity, activity,frm)
                        memberslist.adapter = adps
                        *//*
                        "id": 25,
                        "type": "Advance Request",
                        "name": null,
                        "user": "9894940560",
                        "tid": "V00018",
                        "image": "http://teamdev.co.in/vanitha/public/uploads/request/9209image.png",
                        "image1": "http://teamdev.co.in/vanitha/public/uploads/request/5000image1.png",
                        "description": null,
                        "amount": "6500",
                        "status": "Pending",
                        "approved_amount": "",
                        "notes": "",
                        "approved_date": "",
                        "tour_plan": [],
                        "expenditure_plan": []*//*
                    } else {
                        textView23.visibility = View.VISIBLE
                    }
                    shimmer_view_container.stopShimmer()
                    shimmer_view_container.visibility = View.GONE
                    swiperefresh.isRefreshing=false
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    shimmer_view_container.stopShimmer()
                    shimmer_view_container.visibility = View.GONE
                    textView23.visibility = View.VISIBLE
                    swiperefresh.isRefreshing=false
                }

            })
        }
        else{
            Toast.makeText(
                    this@Transaction_List,
                    "You're offline",
                    Toast.LENGTH_LONG
            ).show()
            shimmer_view_container.visibility=View.GONE
            swiperefresh.isRefreshing=false
        }
    }*/






    /*fun Editorders(id: Int){
        customerid=CentresArrays[id].customer_id.toString()
        cusid=CentresArrays[id].id.toString()
        val openwith = Dialog(this)
        openwith.setOnDismissListener {
            println("dismiss")
        }
        openwith.requestWindowFeature(Window.FEATURE_ACTION_MODE_OVERLAY);
        val popUpView = this@Tour_List!!.layoutInflater.inflate(R.layout.customer_order, null)
        var close=popUpView.findViewById<ImageButton>(R.id.imageButton10)
        title=popUpView.findViewById(R.id.textView16) as TextView
        title!!!!.setText(CentresArrays[id].customer_name)
        save=popUpView.findViewById(R.id.save) as TextView
        save!!.setText("UPDATE")
        //var close=popUpView.findViewById<ImageButton>(R.id.imageButton10)
        refed=popUpView.findViewById(R.id.ref) as TextInputEditText
        ordamt=popUpView.findViewById(R.id.ordamt) as TextInputEditText
        notes=popUpView.findViewById(R.id.notes) as TextInputEditText
        refed!!.setText(CentresArrays[id].city)
        ordamt!!.setText(CentresArrays[id].mobile)
        notes!!.setText(CentresArrays[id].address)
        val listval=popUpView.findViewById(R.id.textView84) as TextView
        image1=popUpView.findViewById(R.id.imageView9) as ImageView
        image2=popUpView.findViewById(R.id.imageView10) as ImageView
        val animMoveToTop = AnimationUtils.loadAnimation(this, R.anim.bottom_top)
        popUpView.animation =animMoveToTop

        image1!!.setImageResource(R.mipmap.placeholders)
        image2!!.setImageResource(R.mipmap.placeholders)
        openwith.setContentView(popUpView)
        openwith.setCancelable(true)
        openwith.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        openwith.window!!.setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        openwith.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

        val displaymetrics = DisplayMetrics();
        this!!.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        val width =  (displaymetrics.widthPixels * 1);
        val height =  (displaymetrics.heightPixels * 1);
        openwith.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        openwith.show()

        close.setOnClickListener {
            openwith.dismiss()
        }

        image1!!.setOnClickListener {

            if(CheckingPermissionIsEnabledOrNot(this)&&(panimage.isEmpty()||panimage=="null")){

                selectImage()

            }
            else{
                RequestMultiplePermission(activity!!)
            }

        }

        image2!!.setOnClickListener {

            if(CheckingPermissionIsEnabledOrNot(this)&&(panimage.isEmpty()||panimage=="null")){

                selectImage1()


            }
            else{
                RequestMultiplePermission(activity!!)
            }

        }


        save!!.setOnClickListener {

            if(refed!!.text.toString().isNotEmpty()&&
                ordamt!!.text.toString().isNotEmpty()) {
                openwith.dismiss()
                pDialog = ProgressDialog(this!!);
                pDialog.setMessage("Submitting...")
                pDialog.setCancelable(false)
                pDialog.show()
                Handler().postDelayed({
                    SendLogin(pref.getString("mobile", "").toString())

                }, 2000)

            }
            else{
                if(refed!!.text.toString().isEmpty()){
                    refed!!.setError("Required field")
                }
                if(ordamt!!.text.toString().isEmpty()){
                    ordamt!!.setError("Required field")
                }

            }

        }

    }*/

    fun SendLogin(mobile: String){
        lateinit var call:Call<Resp_otp>
        if(frm=="customer"){
            call = ApproveUtils.Get.getentry(pref.getString("tid", "").toString(),
                    cusid, notes!!.text.toString(), refed!!.text.toString(), ordamt!!.text.toString(),
                    "", "", mobile)
        }
        else if(frm=="order"){
            call= ApproveUtils.Get.getentry_update(pref.getString("tid", "").toString(),
                    customerid, notes!!.text.toString(), refed!!.text.toString(), ordamt!!.text.toString(),
                    "", "", mobile, cusid)
        }

        call.enqueue(object : Callback<Resp_otp> {
            override fun onResponse(call: Call<Resp_otp>, response: Response<Resp_otp>) {
                Log.e("response", response.toString())
                if (response.isSuccessful()) {
                    val example = response.body() as Resp_otp
                    println(example)
                    if (example.status == "Success") {
                        var arr = example.message
                        toast(arr.toString())
                        onResume()
                        //finish()
                    } else {
                        Toast.makeText(
                                activity,
                                example.message,
                                Toast.LENGTH_LONG
                        ).show()
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


    fun delete(id:String){
        val pdialog=ProgressDialog(this)
        pdialog.setMessage("Deleting...")
        pdialog.setCancelable(false)
        pdialog.show()

        val call = ApproveUtils.Get.deleteexp(Appconstands.Domin+"expenseDelete/"+id)
        call.enqueue(object : Callback<Resp_otp> {
            override fun onResponse(call: Call<Resp_otp>, response: Response<Resp_otp>) {
                Log.e("response", response.toString())
                if (response.isSuccessful()) {
                    val example = response.body() as Resp_otp
                    println(example)
                    if (example.status == "Success") {
                        var arr = example.message
                        toast(arr.toString())
                        pdialog.dismiss()
                        onResume()
                        //finish()
                    } else {
                        pdialog.dismiss()

                        Toast.makeText(
                            activity,
                            example.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                else{
                    pdialog.dismiss()
                }
               // pDialog.dismiss()
                //loading_show(activity).dismiss()
            }

            override fun onFailure(call: Call<Resp_otp>, t: Throwable) {
                Log.e("Fail response", t.toString())
                pdialog.dismiss()

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

    /*fun clikffed(title: String,total:String,json:JSONArray) {
        try {

            update = Dialog(this)
            update.requestWindowFeature(Window.FEATURE_NO_TITLE)
            update.window!!.setBackgroundDrawable(
                ColorDrawable(Color.WHITE)
            )
            val v = layoutInflater.inflate(R.layout.feedback_popup, null)
            val prolist = v.findViewById<View>(R.id.feedlist) as RecyclerView
            val scroll = v.findViewById<View>(R.id.sroll) as ScrollView
            val nofeed = v.findViewById<View>(R.id.textView18) as TextView
            val feed = v.findViewById<View>(R.id.textView15) as TextView
            val feedvt = v.findViewById<View>(R.id.textView15vtime) as TextView
            val feedcut = v.findViewById<View>(R.id.textView15vcut) as TextView
            val textView88 = v.findViewById<View>(R.id.textView88) as TextView
            val laterbut = v.findViewById<View>(R.id.agree) as Button
            val viewfile = v.findViewById<View>(R.id.viewfile) as Button
            val download = v.findViewById<View>(R.id.download) as Button
            val close = v.findViewById<ImageButton>(R.id.imageButton11) as ImageButton
            feed.text = "Details"
            feedcut.text = "Paid Amount - $total"

            close.setOnClickListener {
                update.dismiss()
            }
            laterbut.setOnClickListener {
                update.dismiss()

            }

            adps1 = TransacAdapter_Products(json, activity)
            prolist.adapter = adps1
            update.setContentView(v)

            val window = update.window
            window!!.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )
            update.setCancelable(false)
            update.show()

        } catch (e: Exception) {
            Log.e("Log val", e.toString())
            //logger.info("PerformVersionTask error" + e.getMessage());
        }
    }*/

    public fun selectImage() {
        val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Add Photo!")
        builder.setItems(options) { dialog, item ->
            if (options[item] == "Take Photo") {
                val intent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, 102)
            } else if (options[item] == "Choose from Gallery") {
                val i = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                i.type = "image/*"
                startActivityForResult(i, 1)
            } else if (options[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builder.show()
    }


    public fun selectImage1() {
        val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Add Photo!")
        builder.setItems(options) { dialog, item ->
            if (options[item] == "Take Photo") {
                val intent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, 103)
            } else if (options[item] == "Choose from Gallery") {
                val i = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                i.type = "image/*"
                startActivityForResult(i, 2)
            } else if (options[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builder.show()
    }

    fun CheckingPermissionIsEnabledOrNot(context: Activity): Boolean {
        val INTERNET = ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
        val ACCESS_NETWORK_STATEt = ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE)
        //val ACCESS_NOTIFICATION_POLICY = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_NOTIFICATION_POLICY)
        val ACCESS_COARSE_LOCATION = ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        return INTERNET == PackageManager.PERMISSION_GRANTED &&
        ACCESS_NETWORK_STATEt == PackageManager.PERMISSION_GRANTED &&
                //ACCESS_NOTIFICATION_POLICY == PackageManager.PERMISSION_GRANTED&&
                ACCESS_COARSE_LOCATION == PackageManager.PERMISSION_GRANTED

    }

    fun getImgPath(uri: Uri?): String? {
        val result: String?
        val cursor = activity!!.contentResolver.query(uri!!, null, null, null, null)
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = uri.path
        } else {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(idx)
            cursor.close()
        }
        // Log.i("utilsresult", result!! + "")
        return result

    }
    fun saveImage(myBitmap: Bitmap): String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val wallpaperDirectory = File(
                Environment.getExternalStorageDirectory().toString() + "IMAGE_DIRECTORY"
        )
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs()
        }

        try {
            val f = File(
                    wallpaperDirectory, Calendar.getInstance()
                    .timeInMillis.toString() + ".jpg"
            )
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(
                    activity!!,
                    arrayOf(f.path),
                    arrayOf("image/jpeg"), null
            )
            fo.close()
            Log.d("TAG", "File Saved::--->" + f.absolutePath)

            return f.absolutePath
        } catch (e1: IOException) {
            e1.printStackTrace()
        }

        return ""
    }
    private fun resize(image: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
        if (maxHeight > 0 && maxWidth > 0)
        {
            val width = image.getWidth()
            val height = image.getHeight()
            val ratioBitmap = width.toFloat() / height.toFloat()
            val ratioMax = maxWidth.toFloat() / maxHeight.toFloat()
            var finalWidth = maxWidth
            var finalHeight = maxHeight
            if (ratioMax > ratioBitmap)
            {
                finalWidth = (maxHeight.toFloat() * ratioBitmap).toInt()
            }
            else
            {
                finalHeight = (maxWidth.toFloat() / ratioBitmap).toInt()
            }
            val images = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true)
            return images
        }
        else
        {
            return image
        }
    }

    private fun RequestMultiplePermission(context: Activity) {
        ActivityCompat.requestPermissions(context, arrayOf<String>(

                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE

        ), RequestPermissionCode
        )

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == 1) {
                    if (resultCode == Activity.RESULT_OK && null != data) {
                        var picturePath: String? = null
                        var selectedImage = data!!.data
                        picturePath = getImgPath(selectedImage)
                        fi = File(picturePath!!)

                        // user_img.setImageURI(selectedImage);
                        // Picasso.with(MyInfo.this).load(picturePath).placeholder(R.mipmap.userplaceholder).into(user_img);
                        val yourSelectedImage = CommonFunctions.decodeFile1(picturePath, 400, 200)

                        //Log.i("original path1", picturePath + "")

                        //removeimg.setVisibility(View.VISIBLE);
                        // addimgbut.setVisibility(View.GONE);

                        Log.i(
                                "pathsizeeeeee",
                                (fi.length() / 1024).toString() + "      " + yourSelectedImage
                        )
                        //val image1 = CommonFunctions.decodeFile1(picturePath, 0, 0)
                        val bitmap = MediaStore.Images.Media.getBitmap(
                                activity!!.getBaseContext().getContentResolver(),
                                selectedImage
                        )

                        val resizeBitmap =
                            resize(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2);

                        image1!!.setImageBitmap(resizeBitmap)
                        val stream = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream)
                        byteArray = stream.toByteArray()
                        imagecode = Base64.encodeToString(byteArray, Base64.DEFAULT)
                        Log.e("imagecode", imagecode)

                        val path = getImgPath(selectedImage!!)
                        //choose_files.setText("Remove Image")
                        if (path != null) {
                            val f = File(path!!)

                        }

                    } else {

                    }
                } else if (requestCode == 102) {


                    try {

                        var selectedImageUri = data!!.data
                        val bitmap = MediaStore.Images.Media.getBitmap(
                                activity!!.getBaseContext().getContentResolver(),
                                selectedImageUri
                        )
                        val resizeBitmap = resize(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
                        image1!!.setImageBitmap(resizeBitmap)
                        val stream = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, stream)
                        byteArray = stream.toByteArray()
                        imagecode = Base64.encodeToString(byteArray, Base64.DEFAULT)

                        val path = getImgPath(selectedImageUri!!)
                        //getBase64FromPath(path);
                        if (path != null) {
                            val f = File(path!!)

                            selectedImageUri = Uri.fromFile(f)

                        }


                    } catch (e: Exception) {
                        val thumbnail = data!!.extras!!.get("data") as Bitmap?
                        val stream = ByteArrayOutputStream()
                        thumbnail!!.compress(Bitmap.CompressFormat.JPEG, 70, stream)
                        byteArray = stream.toByteArray()

                        imagecode = Base64.encodeToString(byteArray, Base64.DEFAULT)

                        saveImage(thumbnail)

                        //imglin.visibility = View.VISIBLE
                        image1!!.setImageBitmap(thumbnail)
                        //upload.setText("Retake")
                    }

                }
                else if (requestCode ==2) {
                    if (resultCode == Activity.RESULT_OK && null != data) {
                        var picturePath: String? = null
                        var selectedImage = data!!.data
                        picturePath = getImgPath(selectedImage)
                        fi1 = File(picturePath!!)
                        val yourSelectedImage = CommonFunctions.decodeFile1(picturePath, 400, 200)
                        //Log.i("original path1", picturePath + "")
                        Log.i(
                                "pathsizeeeeee",
                                (fi1.length() / 1024).toString() + "      " + yourSelectedImage
                        )
                        //val image1 = CommonFunctions.decodeFile1(picturePath, 0, 0)
                        val bitmap = MediaStore.Images.Media.getBitmap(
                                activity!!.getBaseContext().getContentResolver(),
                                selectedImage
                        )

                        val resizeBitmap =
                            resize(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2);

                        image2!!.setImageBitmap(resizeBitmap)
                        val stream = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream)
                        byteArray1 = stream.toByteArray()
                        imagecode1 = Base64.encodeToString(byteArray1, Base64.DEFAULT)
                        Log.e("imagecode", imagecode1)

                        val path = getImgPath(selectedImage!!)
                        //choose_files.setText("Remove Image")
                        if (path != null) {
                            val f = File(path!!)

                        }

                    } else {

                    }
                } else if (requestCode == 103) {


                    try {

                        var selectedImageUri = data!!.data
                        val bitmap = MediaStore.Images.Media.getBitmap(
                                activity!!.getBaseContext().getContentResolver(),
                                selectedImageUri
                        )
                        val resizeBitmap = resize(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
                        image2!!.setImageBitmap(resizeBitmap)
                        val stream = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, stream)
                        byteArray1 = stream.toByteArray()
                        imagecode1 = Base64.encodeToString(byteArray1, Base64.DEFAULT)

                        val path = getImgPath(selectedImageUri!!)
                        //getBase64FromPath(path);
                        if (path != null) {
                            val f = File(path!!)

                            selectedImageUri = Uri.fromFile(f)

                        }


                    } catch (e: Exception) {
                        val thumbnail = data!!.extras!!.get("data") as Bitmap?
                        val stream = ByteArrayOutputStream()
                        thumbnail!!.compress(Bitmap.CompressFormat.JPEG, 70, stream)
                        byteArray1 = stream.toByteArray()

                        imagecode1 = Base64.encodeToString(byteArray1, Base64.DEFAULT)

                        saveImage(thumbnail)

                        //imglin.visibility = View.VISIBLE
                        image2!!.setImageBitmap(thumbnail)
                        //upload.setText("Retake")
                    }




                }
            }




        }
        catch (e: Exception){
        }
    }

    /*fun imgpo(pos: Int, req: String){
        if(CentresArraysdup1.isEmpty()){
            val openwith = Dialog(this)
            openwith.setOnDismissListener {
                println("dismiss")
            }
            openwith.requestWindowFeature(Window.FEATURE_ACTION_MODE_OVERLAY);
            val popUpView = this@Tour_List!!.layoutInflater.inflate(R.layout.img_pop, null)
            var close=popUpView.findViewById<ImageButton>(R.id.imageButton10)
            title=popUpView.findViewById(R.id.textView16) as TextView
            title!!!!.setText(CentresArrays[pos].customer_name)
            save=popUpView.findViewById(R.id.save) as TextView
            //var close=popUpView.findViewById<ImageButton>(R.id.imageButton10)
            refed=popUpView.findViewById(R.id.desc_lay) as TextView
            ordamt=popUpView.findViewById(R.id.reasonlay) as TextView
            val reasonamt=popUpView.findViewById(R.id.reasonamt) as TextView

            notes=popUpView.findViewById(R.id.notes) as TextInputEditText
            val listval=popUpView.findViewById(R.id.textView84) as TextView
            image1=popUpView.findViewById(R.id.imageView9) as ImageView
            image2=popUpView.findViewById(R.id.imageView10) as ImageView
            val animMoveToTop = AnimationUtils.loadAnimation(this, R.anim.bottom_top)
            popUpView.animation =animMoveToTop

            image1!!.setImageResource(R.mipmap.noimage)
            image2!!.setImageResource(R.mipmap.noimage)

            openwith.setContentView(popUpView)
            openwith.setCancelable(true)
            openwith.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            openwith.window!!.setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            openwith.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

            val displaymetrics = DisplayMetrics();
            this!!.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            val width =  (displaymetrics.widthPixels * 1);
            val height =  (displaymetrics.heightPixels * 1);
            openwith.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            openwith.show()

            refed!!.setText(CentresArrays[pos].address)
            ordamt!!.setText(CentresArrays[pos].shopnm)
            refed!!.setFocusable(false)
            ordamt!!.setFocusable(false)

            if(req=="Expense"){
                ordamt!!.visibility=View.GONE
                reasonamt!!.visibility=View.GONE
            }

            close.setOnClickListener {
                openwith.dismiss()
            }

            if(CentresArrays[pos].image!!.isNotEmpty()){

                Glide.with(this).load(CentresArrays[pos].image).into(image1!!)
                Glide.with(this).load(CentresArrays[pos].image).into(imageView7)
            }
            if(CentresArrays[pos].image1!!.isNotEmpty()){
                Glide.with(this).load(CentresArrays[pos].image1).into(image2!!)
            }

            image1!!.setOnClickListener {
                openwith.dismiss()
                imageView7.visibility=View.VISIBLE
                imageButton7.visibility=View.VISIBLE
            }
            imageButton7.setOnClickListener {
                openwith.dismiss()
                imageView7.visibility=View.GONE
                imageButton7.visibility=View.GONE

            }

        }
        else{
            val openwith = Dialog(this)
            openwith.setOnDismissListener {
                println("dismiss")
            }
            openwith.requestWindowFeature(Window.FEATURE_ACTION_MODE_OVERLAY);
            val popUpView = this@Tour_List!!.layoutInflater.inflate(R.layout.img_pop, null)
            var close=popUpView.findViewById<ImageButton>(R.id.imageButton10)
            title=popUpView.findViewById(R.id.textView16) as TextView
            title!!!!.setText(CentresArrays[pos].customer_name)
            save=popUpView.findViewById(R.id.save) as TextView
            //var close=popUpView.findViewById<ImageButton>(R.id.imageButton10)
            refed=popUpView.findViewById(R.id.desc_lay) as TextView
            ordamt=popUpView.findViewById(R.id.reasonlay) as TextView
            val reasonamt=popUpView.findViewById(R.id.reasonamt) as TextView
            notes=popUpView.findViewById(R.id.notes) as TextInputEditText
            val listval=popUpView.findViewById(R.id.textView84) as TextView
            image1=popUpView.findViewById(R.id.imageView9) as ImageView
            image2=popUpView.findViewById(R.id.imageView10) as ImageView
            val animMoveToTop = AnimationUtils.loadAnimation(this, R.anim.bottom_top)
            popUpView.animation =animMoveToTop

            image1!!.setImageResource(R.mipmap.placeholders)
            image2!!.setImageResource(R.mipmap.placeholders)

            refed!!.setText(CentresArrays[pos].address)
            ordamt!!.setText(CentresArrays[pos].shopnm)
            ordamt!!.visibility=View.INVISIBLE
            reasonamt!!.visibility=View.INVISIBLE
            //refed!!.setFocusable(false)
            //ordamt!!.setFocusable(false)

            openwith.setContentView(popUpView)
            openwith.setCancelable(true)
            openwith.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            openwith.window!!.setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            openwith.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

            val displaymetrics = DisplayMetrics();
            this!!.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            val width =  (displaymetrics.widthPixels * 1);
            val height =  (displaymetrics.heightPixels * 1);
            openwith.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            openwith.show()

            close.setOnClickListener {
                openwith.dismiss()
            }
            image1!!.setOnClickListener {
                openwith.dismiss()
                imageView7.visibility=View.VISIBLE
                imageButton7.visibility=View.VISIBLE
            }
            imageButton7.setOnClickListener {
                openwith.dismiss()
                imageView7.visibility=View.GONE
                imageButton7.visibility=View.GONE

            }
            if(CentresArraysdup1[pos].image!!.isNotEmpty()){
                //view6.visibility=View.VISIBLE
                Glide.with(this).load(CentresArraysdup1[pos].image).into(image1!!)
                Glide.with(this).load(CentresArraysdup1[pos].image).into(imageView7)

            }
            if(CentresArraysdup1[pos].image1!!.isNotEmpty()){
               // view6.visibility=View.VISIBLE
                Glide.with(this).load(CentresArraysdup1[pos].image1).into(image2!!)
            }
        }
    }*/


    override fun onBackPressed() {

        try {
            if (update.isShowing) {
                update.dismiss()
            } else {
                super.onBackPressed()
            }
        }
        catch (e:Exception){
            super.onBackPressed()

        }
    }

    override fun OnRequestItemClick(view: View, position: Int, viewType: Int) {

    }

    override fun OnExpenseItemClick(view: View, position: Int, viewType: Int) {

    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
    }
}
