package com.elancier.team_j

import android.Manifest
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Criteria
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.StrictMode
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.elancier.team_j.retrofit.Appconstants
import com.elancier.team_j.retrofit.ApproveUtils
import com.elancier.team_j.retrofit.Resp_otp
import com.elancier.team_j.retrofit.Resp_trip
import com.elancier.vanithamarket.Prolist
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_dashboard.back
import kotlinx.android.synthetic.main.activity_dashboard.eod
import kotlinx.android.synthetic.main.activity_dashboard.front
import kotlinx.android.synthetic.main.activity_dashboard.outstanding
import kotlinx.android.synthetic.main.activity_dashboard.textView49
import kotlinx.android.synthetic.main.activity_main.balancelay
import kotlinx.android.synthetic.main.activity_main.baln
import kotlinx.android.synthetic.main.activity_main.cuslay
import kotlinx.android.synthetic.main.activity_main.cust
import kotlinx.android.synthetic.main.activity_main.firstletter
import kotlinx.android.synthetic.main.activity_main.norders
import kotlinx.android.synthetic.main.activity_main.orderlay
import kotlinx.android.synthetic.main.activity_main.orderval
import kotlinx.android.synthetic.main.activity_main.textView47
import kotlinx.android.synthetic.main.activity_main.textView51
import kotlinx.android.synthetic.main.activity_main.textView53
import kotlinx.android.synthetic.main.activity_main.textView55
import kotlinx.android.synthetic.main.activity_main.textView56cust
import kotlinx.android.synthetic.main.activity_main.textView56stock
import kotlinx.android.synthetic.main.drawer_layout.name
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class Dashboard : AppCompatActivity() {
    var locationmanager: LocationManager? = null
    var lat = 0.0
    var longi= 0.0
    var district=""
    lateinit var pDialog: Dialog
    internal  var pref: SharedPreferences?=null
    internal var editor: SharedPreferences.Editor? = null
    var from=""
    var checkaction="0"

    fun getLocation() {
        try {
            println("locat")
            locationmanager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED) {

                return
            }
            val criteria = Criteria()
            criteria.accuracy = Criteria.ACCURACY_FINE
            //locationmanager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5f, activity)
            locationmanager!!.requestSingleUpdate(
                criteria,
                object : android.location.LocationListener {
                    override fun onLocationChanged(location: Location) {
                        println("singlelocation : " + location.latitude + " , " + location.longitude)

                        lat = location.latitude
                        longi = location.longitude


                        getCompleteAddressString(lat, longi)

                        //location_shimmer.stopShimmer()
                        // location_shimmer.visibility = View.GONE
                        // location_layout.visibility = View.VISIBLE
                        //getCompleteAddressString(location)
                    }

                    override fun onStatusChanged(
                        provider: String,
                        status: Int,
                        extras: Bundle
                    ) {
                    }

                    override fun onProviderEnabled(provider: String) {}
                    override fun onProviderDisabled(provider: String) {}
                },
                null
            )
            // Log.e(“Network”, “Network”);
            if (locationmanager != null) {
                val location = locationmanager!!
                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                if (location != null) {
                    println("lastknown : " + location.latitude + " , " + location.longitude)

                }
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
            println("error : " + e.printStackTrace())
        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        pDialog = Dialog(this)
        pref = this@Dashboard.getSharedPreferences("MyPref", 0)
        editor = pref!!.edit()

        textView51.setText(
            pref!!.getString("cusname", "").toString()
        )


        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        textView55.setOnClickListener {
            startActivity(
                Intent(this@Dashboard, Prolist::class.java)
                    .putExtra("name",textView51.text.toString())
            )
        }

        textView53.setOnClickListener {
            startActivity(
                Intent(this@Dashboard, FullView::class.java)
            )
        }

        outstanding.setOnClickListener {
            startActivity(
                Intent(this@Dashboard, Outstanding_customer::class.java)
                    .putExtra("cus", "customer")
            )
        }

        cust.setOnClickListener {
            Editorders(0)


        }

        textView56cust.setOnClickListener {
            startActivity(
                Intent(this@Dashboard, Credit_List::class.java).putExtra("cus", "customer")
            )
        }

        textView56stock.setOnClickListener {
            startActivity(
                Intent(this@Dashboard, Fish_report_list::class.java))
        }

        textView49.setOnClickListener {
            startActivity(
                Intent(this@Dashboard, Request_List::class.java).putExtra("cus", "order")
            )
        }

        textView47.setOnClickListener {
            startActivity(
                Intent(this@Dashboard, CustomersListbyCity::class.java).putExtra("cus", "customer")
            )

        }

        eod.setOnClickListener {
            startActivity(
                Intent(this@Dashboard, Request_List::class.java).putExtra("cus", "customer")//Eod_list
            )

        }

        front.setOnClickListener {
            if(from=="Employee"){
                startActivity(
                    Intent(this@Dashboard, Customers_List_New::class.java).putExtra("cus", "customer")//Eod_list
                )
            }
            else{
                startActivity(
                    Intent(this@Dashboard, Transaction_List::class.java).putExtra("cus", "customer")//Eod_list
                )
            }


        }

        back.setOnClickListener {
            exit()

        }

    }

    override fun onBackPressed() {
        exit()
    }

    fun exit() {
        val alert = androidx.appcompat.app.AlertDialog.Builder(this)
        alert.setTitle("Exit?")
        alert.setMessage("Are you sure want to exit?")
        alert.setPositiveButton("Yes", DialogInterface.OnClickListener { dialogInterface, i ->
            finish()
        })
        alert.setNegativeButton("No", DialogInterface.OnClickListener { dialogInterface, i ->
            dialogInterface.dismiss()
        })
        val popup = alert.create()
        popup.show()
    }
    fun CheckingPermissionIsEnabledOrNot(): Boolean {

        val INTERNET = ContextCompat.checkSelfPermission(this, android.Manifest.permission.INTERNET)
        val ACCESS_NETWORK_STATE = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_NETWORK_STATE
        )
        val STORAGE = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )

        val STORAGEwrite = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        //val ACCESS_NOTIFICATION_POLICY = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_NOTIFICATION_POLICY)
        val ACCESS_FINE_LOCATION = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
        val ACCESS_COARSE_LOCATION = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
        val CALL_PHONE = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.CALL_PHONE
        )

        return INTERNET == PackageManager.PERMISSION_GRANTED &&
                ACCESS_NETWORK_STATE == PackageManager.PERMISSION_GRANTED &&
                STORAGE == PackageManager.PERMISSION_GRANTED &&
                STORAGEwrite == PackageManager.PERMISSION_GRANTED &&
                //ACCESS_NOTIFICATION_POLICY == PackageManager.PERMISSION_GRANTED &&
                ACCESS_FINE_LOCATION == PackageManager.PERMISSION_GRANTED &&
                ACCESS_COARSE_LOCATION == PackageManager.PERMISSION_GRANTED //&&
        //CALL_PHONE == PackageManager.PERMISSION_GRANTED
    }
    fun RequestMultiplePermission() {

        // Creating String Array with Permissions.
        ActivityCompat.requestPermissions(
            this, arrayOf<String>
                (
                android.Manifest.permission.INTERNET,
                android.Manifest.permission.ACCESS_NETWORK_STATE,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION//,
                //android.Manifest.permission.CALL_PHONE
            ), Appconstands.RequestPermissionCode
        )


    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {

            Appconstands.RequestPermissionCode ->

                if (grantResults.size > 0) {

                    val INTERNET = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    val ACCESS_NETWORK_STATE = grantResults[1] == PackageManager.PERMISSION_GRANTED
                    //val ACCESS_NOTIFICATION_POLICY = grantResults[2] == PackageManager.PERMISSION_GRANTED
                    val ACCESS_FINE_LOCATION = grantResults[2] == PackageManager.PERMISSION_GRANTED
                    val ACCESS_COARSE_LOCATION =
                        grantResults[3] == PackageManager.PERMISSION_GRANTED
                    //val CALL_PHONE = grantResults[4] == PackageManager.PERMISSION_GRANTED

                    if (INTERNET && ACCESS_NETWORK_STATE /*&& ACCESS_NOTIFICATION_POLICY*/ && ACCESS_FINE_LOCATION && ACCESS_COARSE_LOCATION /*&&CALL_PHONE*/) {
                        if (ContextCompat.checkSelfPermission(
                                this,
                                android.Manifest.permission.ACCESS_COARSE_LOCATION
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            /*val mLocationManager =  getSystemService(LOCATION_SERVICE) as (LocationManager);
                            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, this);*/
                            //getLocation()                            getLocation()
                            getLocation()


                        }
                        //Toast.makeText(this@MainFirstActivity,"Permission Granted", Toast.LENGTH_LONG).show()
                    } else {
                        //Toast.makeText(this@MainFirstActivity,"Permission Denied", Toast.LENGTH_LONG).show()

                    }
                }

        }
    }


    fun families(type:String){
        if (Appconstants.net_status(this)) {

            val json=JsonObject()
            if(type=="Employee"){
                json.addProperty("cus_id","0")

            }
            else{
                json.addProperty("cus_id",pref!!.getString("cusid",""))

            }
            json.addProperty("enter_by",pref!!.getString("empid",""))

            if(type=="customer"){
                json.addProperty("type","Customer")

            }
            else
            {
                json.addProperty("type",type)

            }

            val call = ApproveUtils.Get.getdashboard(json)
            call.enqueue(object : Callback<Resp_otp> {
                override fun onResponse(call: Call<Resp_otp>, response: Response<Resp_otp>) {
                    Log.e("areacode responce", response.toString())
                    if (response.isSuccessful()) {
                        val example = response.body() as Resp_otp
                        println(example)
                        if (example.status == "Success") {
                            //textView23.visibility=View.GONE
                            var arr = example.message
                            var otpval = example.response
                            //textView33.setText(arr!!.data?.get(0)?.family.toString())

                            if(type=="Employee"){
                                orderval.setText(otpval?.get(0)!!.customers)
                            }
                            else{
                                baln.setText(Appconstands.rupees+otpval?.get(0)!!.balance)
                                norders.setText(otpval?.get(0)!!.orders)
                                checkaction=otpval?.get(0)!!.check_out.toString()

                                /*if(checkaction=="2"){
                                    var AlertDialog= AlertDialog.Builder(this@MainActivity)
                                    AlertDialog.setTitle("Session Expired")
                                    AlertDialog.setCancelable(false)
                                    AlertDialog.setMessage("Your customer login session expired.\nPlease Check In Again.")
                                    AlertDialog.setPositiveButton("OK",
                                        DialogInterface.OnClickListener { dialogInterface, i ->

                                            dialogInterface.dismiss()
                                            dosavecat(
                                                pref!!.getString("cusname", "").toString(),
                                                pref!!.getString("cusid", "").toString()
                                            )

                                        })
                                    AlertDialog.setNegativeButton("Cancel",
                                        DialogInterface.OnClickListener { dialogInterface, i ->
                                            dialogInterface.dismiss()

                                            //onResume()
                                        })
                                    val pops=AlertDialog.create()
                                    pops.show()
                                }*/
                            }

                        } else {
                            Toast.makeText(
                                this@Dashboard,
                                example.status,
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            //shimmer_view_container.visibility=View.GONE
                            //textView23.visibility=View.VISIBLE
                            //shimmer_view_container.stopShimmer()

                        }
                    } else {
                        //textView23.visibility=View.VISIBLE

                        //shimmer_view_container.stopShimmer()

                        //shimmer_view_container.visibility=View.GONE

                    }
                }

                override fun onFailure(call: Call<Resp_otp>, t: Throwable) {
                    Log.e("areacode Fail response", t.toString())
                    if (t.toString().contains("time")) {
                        Toast.makeText(
                            this@Dashboard,
                            "Poor network connection",
                            Toast.LENGTH_LONG
                        ).show()
                        //shimmer_view_container.visibility=View.GONE
                        //shimmer_view_container.stopShimmer()


                    }
                }
            })
        }
        else{
            Toast.makeText(
                this@Dashboard,
                "You're offline",
                Toast.LENGTH_LONG
            ).show()
            //shimmer_view_container.visibility=View.GONE

        }
    }

    override fun onResume() {
        super.onResume()
        //getVillage()
        //getDashboard(pref!!.getString("mobile", "").toString())
        from=intent.extras!!.getString("from").toString()
        if(from=="customer"){
           /* front.visibility= View.VISIBLE
            textView56cust.visibility= View.GONE
            textView56stock.visibility= View.VISIBLE
            textView49.visibility= View.GONE
            textView55.visibility= View.GONE*/
            front.visibility=View.GONE
            cust.visibility=View.GONE
            textView47.visibility= View.GONE

            textView51.setText(pref!!.getString("cusname", "").toString()/*+ "!"*/)
            firstletter.setText((pref!!.getString("cusname", "").toString().substring(0).capitalize()))
     /*       orderlay.visibility= View.GONE
            balancelay.visibility= View.GONE
            cust.visibility= View.VISIBLE
            cuslay.visibility= View.VISIBLE
            eod.visibility= View.GONE*/
            //textView83.visibility=View.VISIBLE

            families(from)

        }
        else{
            front.visibility= View.VISIBLE
            front.setText("Previous Collections")
            textView56cust.visibility= View.VISIBLE
            textView56stock.visibility= View.GONE
            textView49.visibility= View.VISIBLE
            textView47.visibility= View.VISIBLE
            textView55.visibility= View.VISIBLE
            editor!!.putString("cusid",intent.extras!!.getString("id").toString())
            editor!!.commit()
            textView51.setText(/*"Hello " + */intent.extras!!.getString("name").toString() /*+ "!"*/)
            firstletter.setText((intent.extras!!.getString("name").toString().substring(0).capitalize()))
            orderlay.visibility= View.VISIBLE
            balancelay.visibility= View.VISIBLE
            cuslay.visibility= View.GONE
            eod.visibility= View.VISIBLE
            cust.visibility= View.GONE

            //textView83.visibility=View.GONE
            families(from)

        }
    }




    fun Editorders(id: Int){

        val openwith = Dialog(this)
        openwith.setOnDismissListener {
            println("dismiss")
        }
        openwith.requestWindowFeature(Window.FEATURE_ACTION_MODE_OVERLAY);
        val popUpView = this!!.layoutInflater.inflate(R.layout.customer_add_popup, null)
        var close=popUpView.findViewById<ImageButton>(R.id.imageButton10)
        val cusname=popUpView.findViewById(R.id.ref) as TextInputEditText
        val mob=popUpView.findViewById(R.id.ordamt) as TextInputEditText
        val addressfield=popUpView.findViewById(R.id.address) as EditText
        var save=popUpView.findViewById(R.id.save) as TextView

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

        save.setOnClickListener {
            if(cusname.text.toString().isNotEmpty()&&(mob.text.toString().isNotEmpty()&&mob.text.toString().length==10)&&
                addressfield.text.toString().isNotEmpty()){
                Addcustomer(cusname.text.toString(),mob.text.toString(),addressfield.text.toString(),openwith)

            }
            else{
                if(cusname.text.toString().isEmpty()){
                    cusname.setError("Required field")
                }
                if(mob.text.toString().isEmpty()){
                    mob.setError("Required field")

                }
                if(mob.text.toString().length<10||mob.text.toString().length>10){
                    mob.setError("Please Enter Valid Mobile Number")

                }

                if(addressfield.text.toString().isEmpty()){
                    addressfield.setError("Required field")

                }
            }
        }

        /* image1!!.setOnClickListener {

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

         }*/

    }

    fun Addcustomer(name : String,
                    qty : String,
                    description : String,dialog:Dialog
    ){


        val pDialog= ProgressDialog(this)
        pDialog.setCancelable(false)
        pDialog.setMessage("Creating Customer...")
        pDialog.show()
        /*{"user":"9791981428","tid":"V00002","request_type":"4"
        "amount":"1000",
        "tour_plan":[{"tour_date":"","day":"","tour_city":"","customer":""}],
        "expenditure_plan":[{"expnediture_date":"","expenditure_city":"","travel":"","food":"","stay":"","others":"","total":""}]}*/

        val obj = JsonObject()

        obj.addProperty("customer_name", name)
        obj.addProperty("mobile", qty)
        obj.addProperty("address", description)
        obj.addProperty("enter_by", pref!!.getString("empid",""))

        println("sendExpense : " + obj)

        val call = ApproveUtils.Get.customer_add(obj)//(user,tid,requesttypes[requesttype.selectedItemPosition].id.toString(),total_amount.text.toString(),TourPlans,ExpenditurePlans)
        call.enqueue(object : Callback<Resp_trip> {
            override fun onResponse(call: Call<Resp_trip>, response: Response<Resp_trip>) {
                Log.e("advancereq", response.toString())
                pDialog.dismiss()
                if (response.isSuccessful()) {
                    val example = response.body() as Resp_trip
                    Toast.makeText(applicationContext, example.getMessage(), Toast.LENGTH_SHORT).show()
                    if (example.getStatus() == "Success") {
                        dialog.dismiss()
                    }
                }
            }

            override fun onFailure(call: Call<Resp_trip>, t: Throwable) {
                Log.e("advancereq Fail", t.toString())
                if (t.toString().contains("time")) {
                    Toast.makeText(
                        applicationContext,
                        "Poor network connection",
                        Toast.LENGTH_LONG
                    ).show()

                }
                pDialog.dismiss()
            }

        })
    }


    private fun getCompleteAddressString(LATITUDE: Double, LONGITUDE: Double): String? {
        var strAdd = ""
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1)
            if (addresses != null) {
                val returnedAddress = addresses[0]
                val strReturnedAddress = StringBuilder()
                for (i in 0..returnedAddress.maxAddressLineIndex) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n")
                    break
                }
                strAdd = strReturnedAddress.toString()
                district=strReturnedAddress.toString()
                Log.e("location address", district)
                //Toast.makeText(MainActivity.this, strReturnedAddress.toString(), Toast.LENGTH_SHORT).show();
                Log.e("strReturnedAddress", strReturnedAddress.toString())
            } else {
                Log.e("location address", "No Address returned!")
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            Log.e("location address", "Cannot get Address!")
        }
        return strAdd
    }

}


