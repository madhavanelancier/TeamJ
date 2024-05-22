package com.elancier.team_j

import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elancier.team_j.Adapers.MyFamilyAdap_report_plant
import com.elancier.team_j.DataClasses.CentresData
import com.elancier.team_j.DataClasses.SpinnerPojo
import com.elancier.team_j.Family.Family_Main
import com.elancier.team_j.Family.SpinAdapter
import com.elancier.team_j.retrofit.Appconstants
import com.elancier.team_j.retrofit.ApproveUtils
import com.elancier.team_j.retrofit.Resp
import kotlinx.android.synthetic.main.activity_family__list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Plant_Harvest_report : AppCompatActivity() {
    lateinit var pDialog: Dialog
    var villagearr=ArrayList<SpinnerPojo>()
    var CentresArrays = java.util.ArrayList<CentresData>()
    lateinit var adp : MyFamilyAdap_report_plant
    lateinit var click : MyFamilyAdap_report_plant.OnItemClickListener
    internal  var pref: SharedPreferences?=null

    internal  var editor: SharedPreferences.Editor?=null

    var posid=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_family_report)

        pDialog = Dialog(this@Plant_Harvest_report)
        pref = this@Plant_Harvest_report.getSharedPreferences("MyPref", 0)

        shimmer_view_container.visibility= View.VISIBLE

        shimmer_view_container.startShimmer()

        //toolbar.title = "Application Form"
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        //toolbar.setTextAlignment(View.TEXT_ALIGNMENT_CENTER)
        //}
        val ab=supportActionBar
        ab!!.setTitle("Plant Harvest Report");
        ab!!.title="Plant Harvest Report"
        ab!!.setDisplayShowHomeEnabled(true)
        ab!!.setDisplayHomeAsUpEnabled(true)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        addfam.setOnClickListener {
            editor = pref!!.edit()
            editor!!.putString("unitlist","")
            editor!!.commit()
            startActivity(Intent(this@Plant_Harvest_report, Family_Main::class.java))
        }

        click = object : MyFamilyAdap_report_plant.OnItemClickListener{
            override fun OnItemClick(view: View, position: Int, viewType: Int) {

            }


        }
        list_item.setLayoutManager(LinearLayoutManager(this) as RecyclerView.LayoutManager?);
        adp = MyFamilyAdap_report_plant(CentresArrays, this@Plant_Harvest_report, click)
        list_item.adapter = adp

        getVillage()
        families()


    }
    fun getVillage(){

        pDialog= Dialog(this)
        Appconstands.loading_show(this@Plant_Harvest_report, pDialog).show()

        //Toast.makeText(activity, token, Toast.LENGTH_SHORT).show()
        val call = ApproveUtils.Get.getvillage()
        call.enqueue(object : Callback<Resp> {
            override fun onResponse(call: Call<Resp>, response: Response<Resp>) {

                if (response.isSuccessful()) {
                    val example = response.body() as Resp
                    println(example)
                    if (example.getStatus() == "Success") {
                        var arr=example.getMessage()
                        var otpval= arr!!.data
                        for(i in 0 until otpval!!.size) {
                            val data=SpinnerPojo()
                            data.name = otpval[i].name
                            data.id = otpval[i].id
                            data.code = otpval[i].villageCode
                            data.status = otpval[i].status
                            if(otpval[i].status=="on") {
                                villagearr.add(data)

                            }
                        }
                        spinner2.adapter = SpinAdapter(this@Plant_Harvest_report!!,R.layout.tax_spinner_list_item,villagearr)
                        try{
                            spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                                    println("position : "+position)
                                    //Groupcodes().execute(spindata[position].id)
                                    posid=villagearr.get(position).id.toString()
                                    println("positionid : "+posid)
                                    families()

                                }

                                override fun onNothingSelected(parent: AdapterView<*>) {

                                }
                            }
                        }
                        catch (e:Exception){

                        }

                    } else {
                        /*Toast.makeText(
                            this@Plant_Harvest_report,
                            example.getStatus(),
                            Toast.LENGTH_LONG
                        ).show()*/
                    }
                }
                pDialog.dismiss()
                //loading_show(activity).dismiss()
            }

            override fun onFailure(call: Call<Resp>, t: Throwable) {
                //Log.e("$tag Fail response", t.toString())
                if (t.toString().contains("time")) {
                    Toast.makeText(
                        this@Plant_Harvest_report,
                        "Poor network connection",
                        Toast.LENGTH_LONG
                    ).show()
                }
                pDialog.dismiss()
                //loading_show(activity).dismiss()
            }
        })


    }

    fun families(){
        if (Appconstants.net_status(this)) {

            CentresArrays.clear()

            val call = ApproveUtils.Get.getMem2(Appconstants.Domin+"/"+"plant_report_list/"+posid)
            call.enqueue(object : Callback<Resp> {
                override fun onResponse(call: Call<Resp>, response: Response<Resp>) {
                    Log.e("areacode responce", response.toString())
                    if (response.isSuccessful()) {
                        val example = response.body() as Resp
                        println(example)
                        if (example.getStatus() == "Success") {
                            textView23.visibility=View.GONE
                            var arr=example.getMessage()
                            var otpval= arr!!.data
                            //textView33.setText(arr!!.data?.get(0)?.family.toString())
                            //textView36.setText(arr!!.data?.get(0)?.aquaponics.toString())

                            for(i in 0 until otpval!!.size) {
                                val data= CentresData()
                                data.CName = otpval[i].familyheadName
                                data.Cid = otpval[i].id
                                data.CMembers = "Village Name - "+otpval[i].village_name
                                data.CStatus = "Unit Code - "+otpval[i].unitCode
                                data.CLocation = "Harvest - "+otpval[i].totalHarvest

                                //db.CentreInsert(data)
                                CentresArrays.add(data)

                            }
                            adp = MyFamilyAdap_report_plant(
                                CentresArrays,
                                this@Plant_Harvest_report,
                                click
                            )
                            list_item.adapter = adp
                            textView23.visibility=View.GONE

                            shimmer_view_container.stopShimmer()

                            shimmer_view_container.visibility=View.GONE


                            if(CentresArrays.isEmpty()){
                                textView23.visibility=View.VISIBLE
                                shimmer_view_container.stopShimmer()

                                shimmer_view_container.visibility=View.GONE


                            }

                        } else {
                            Toast.makeText(this@Plant_Harvest_report, example.getStatus(), Toast.LENGTH_SHORT)
                                .show()
                            shimmer_view_container.visibility=View.GONE
                            textView23.visibility=View.VISIBLE
                            shimmer_view_container.stopShimmer()

                        }
                    }
                    else{
                        textView23.visibility=View.VISIBLE

                        shimmer_view_container.stopShimmer()

                        shimmer_view_container.visibility=View.GONE

                    }
                }

                override fun onFailure(call: Call<Resp>, t: Throwable) {
                    Log.e("areacode Fail response", t.toString())
                    if (t.toString().contains("time")) {
                        Toast.makeText(
                            this@Plant_Harvest_report,
                            "Poor network connection",
                            Toast.LENGTH_LONG
                        ).show()
                        shimmer_view_container.visibility=View.GONE
                        shimmer_view_container.stopShimmer()


                    }
                }
            })
        }
        else{
            Toast.makeText(
                this@Plant_Harvest_report,
                "You're offline",
                Toast.LENGTH_LONG
            ).show()
            shimmer_view_container.visibility=View.GONE

        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        //menuInflater.inflate(R.menu.receipt, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        when (item.itemId) {
            android.R.id.home ->{
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }


    fun toast(msg:String){
        val toast= Toast.makeText(this@Plant_Harvest_report,msg, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER,0,0)
        toast.show()
    }
}
