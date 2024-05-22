package com.elancier.team_j

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.StrictMode
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.cloudinary.Cloudinary
import com.elancier.domdox.Common.CommonFunctions
import com.elancier.team_j.Appconstands.CheckingPermissionIsEnabledOrNot
import com.elancier.team_j.DataClass.OrderDetail
import com.elancier.team_j.retrofit.*
import com.elancier.team_j.service.MediaUtils
import com.github.dhaval2404.imagepicker.ImagePicker
import kotlinx.android.synthetic.main.activity_advance_request.imageView9

import kotlinx.android.synthetic.main.customer_add.img1

import kotlinx.android.synthetic.main.request_add.*
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class Request_Add:AppCompatActivity() {

    internal lateinit var editor: SharedPreferences.Editor
    internal lateinit var pref: SharedPreferences
    var CentresArrays = ArrayList<OrderDetail>()
    val RequestPermissionCode = 7
    internal lateinit var byteArray: ByteArray
    var imagecode=""
    var imagecode1=""
    var imagecode2=""
    internal lateinit var fi: File
    var panimage=""
    var bankimage=""
    internal lateinit var byteArray1: ByteArray
    internal lateinit var fi1: File
    lateinit var pDialog: ProgressDialog
    internal var nmarr: MutableList<String> = ArrayList()
    internal var idarr: MutableList<String> = ArrayList()
    val activity=this
    var api_key="";
    var api_secret="";
    var cloud_name="";
    var frm = ""
    var imgCount = 0
    var imgCountValid = 0
    var image1path = ""
    var image1data = ""


    private var toDatePicker: DatePickerDialog? = null
    private var toDatePickerListener: DatePickerDialog.OnDateSetListener? = null
    internal lateinit var picker: DatePickerDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.request_add)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val ab = supportActionBar
        ab!!.setDisplayHomeAsUpEnabled(true)
        ab!!.setDisplayShowHomeEnabled(true)


        ab!!.title = "Add Expense"

        nmarr = ArrayList<String>()
        idarr = ArrayList<String>()
        pref = applicationContext.getSharedPreferences("MyPref", 0)
        editor = pref.edit()
        pDialog = ProgressDialog(this)
        pDialog.setMessage("Loading...")
        pDialog.show()
        types()


        cityspin.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View,
                position: Int,
                id: Long
            ) {
                // your code here
                if(position!=0){
                    caterr.visibility=View.GONE
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // your code here
            }
        })

        address.setOnClickListener {
            val cldr = Calendar.getInstance()
            val day = cldr.get(Calendar.DAY_OF_MONTH)
            val month = cldr.get(Calendar.MONTH)
            val year = cldr.get(Calendar.YEAR)
            // date picker dialog
            picker = DatePickerDialog(activity!!,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    address.setText(year.toString()+ "-" + (monthOfYear + 1) + "-" + dayOfMonth.toString() )

                }, year, month, day
            )
            picker.show()
        }

        img1.setOnClickListener{
            imgCount=1;
            if(CheckingPermissionIsEnabledOrNot(this)){
                selectImage()
                Handler().postDelayed(Runnable {
                    pDialog = ProgressDialog(this)
                    pDialog.setMessage("Loading...")
                    pDialog.show()
                },2500)
            }
            else{
                RequestMultiplePermission(activity!!)
            }
        }

        img2.setOnClickListener{
            imgCount=2;
            if(CheckingPermissionIsEnabledOrNot(this)){
                selectImage()
            }
            else{
                RequestMultiplePermission(activity!!)
            }
        }

        img3.setOnClickListener{
            imgCount=3;
            if(CheckingPermissionIsEnabledOrNot(this)){
                selectImage()
            }
            else{
                RequestMultiplePermission(activity!!)
            }
        }


        save.setOnClickListener {
            if (mob.text.toString().trim().isNotEmpty()
                && address.text.toString().trim().isNotEmpty() && cityspin.selectedItemPosition != 0
                &&imgCountValid>0
            ) {

                SendLogin("", imagecode, imagecode1)

            } else {

                if (mob.text.toString().trim().isEmpty()) {
                    mob.error = "Required field*"
                }
                if (address.text.toString().trim().isEmpty()) {
                    address.error = "Required field*"
                }
                if (cityspin.selectedItemPosition == 0) {
                    toast("Select Category")
                    caterr.visibility= View.VISIBLE
                }
                if (imgCountValid == 0) {
                    toast("Please Upload Bill Image")
                    //caterr.visibility= View.VISIBLE
                }
                toast("Please fill required fields")
            }
        }

    }


    fun RequestMultiplePermission(context: Activity) {

        // Creating String Array with Permissions.
        ActivityCompat.requestPermissions(context, arrayOf<String>
            (
            android.Manifest.permission.INTERNET,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.ACCESS_NETWORK_STATE,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.CAMERA
        ), Appconstands.RequestPermissionCode)


    }
    fun selectImage(){
        ImagePicker.with(this)
            .saveDir(getExternalFilesDir(null)!!)
            //.crop()	    			//Crop image(Optional), Check Customization for more option
            .compress(1024)			//Final image size will be less than 1 MB(Optional)
            .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
            .start()
    }


    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==2404) {

            if (resultCode != RESULT_CANCELED) {
                if (resultCode == Activity.RESULT_OK && null != data) {
                    var picturePath: String? = null
                    val selectedImage: Uri? = data.data

                    Handler().postDelayed(Runnable {
                        pDialog.dismiss()
                    },2500)

                   /* try {
                        picturePath = getImgPath(selectedImage)
                        fi = File(picturePath!!)
                        val yourSelectedImage = CommonFunctions.decodeFile1(picturePath, 400, 250)
                        Log.i(
                            "pathsizeeeeee",
                            (fi.length() / 1024).toString() + "      " + yourSelectedImage
                        )
                    }
                    catch (e:Exception){
                        Toast.makeText(applicationContext,"Unable to process the file",Toast.LENGTH_LONG).show()
                    }*/
                    //val image1 = CommonFunctions.decodeFile1(picturePath, 0, 0)
                    val bitmap = MediaStore.Images.Media.getBitmap(
                        activity!!.getBaseContext().getContentResolver(),
                        selectedImage
                    )

                    val resizeBitmap =
                        resize(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2);

                    if(imgCount==1) {
                        imgCountValid=1
                        img1!!.setImageBitmap(bitmap)
                        val stream = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream)
                        byteArray = stream.toByteArray()
                        imagecode = Base64.encodeToString(byteArray, Base64.DEFAULT)
                    }
                    else if(imgCount==2){
                        imgCountValid=1
                        img2!!.setImageBitmap(bitmap)
                        val stream = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream)
                        byteArray = stream.toByteArray()
                        imagecode1 = Base64.encodeToString(byteArray, Base64.DEFAULT)
                    }
                    else if(imgCount==3){
                        imgCountValid=1
                        img3!!.setImageBitmap(bitmap)
                        val stream = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream)
                        byteArray = stream.toByteArray()
                        imagecode2 = Base64.encodeToString(byteArray, Base64.DEFAULT)
                    }
                    /*val myBitmap = Uri.fromFile(File(picturePath))
            val bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), myBitmap)
            val stream = ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            val byteArray = stream.toByteArray()*/;
                    /*if (imgCount == 1) {
                        imgCountValid=1
                        image1data = ""//Base64.encodeToString(byteArray, Base64.DEFAULT)
                        image1path = selectedImage.toString()
                        //imageErr.visibility=View.GONE
                        Log.e("image1",image1path)
                        Glide.with(this).load(image1path.toUri()).placeholder(R.drawable.loading_icon).into(img1)//setImageBitmap(modifyOrientation(BitmapFactory.decodeFile(picturePath),picturePath))//(BitmapFactory.decodeFile(picturePath))
                    }*/
                    //println("image Data : "+Base64.encodeToString(byteArray, Base64.DEFAULT))
                }
            }
        }
        if(requestCode==120){

        }
    }
    private fun resize(image: Bitmap, maxWidth:Int, maxHeight:Int): Bitmap {
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


    fun toast(msg:String){
        val t=Toast.makeText(this,msg,Toast.LENGTH_LONG)
        t.setGravity(Gravity.CENTER,0,0)
        t.show()
    }

    fun SendLogin(mobile:String,url:String,url1:String){
        lateinit var call:Call<Resp_otp>
        lateinit var pDialo:ProgressDialog
        pDialo = ProgressDialog(this@Request_Add)
        pDialo.setMessage("Saving...")
        pDialo.setIndeterminate(false)
        pDialo.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialo.setCancelable(false)
        //pDialo.setMax(3)
        pDialo.show()
        var image=""

        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
        builder.addFormDataPart("user_id",pref.getString("empid","").toString())
        builder.addFormDataPart("category",idarr[cityspin.selectedItemPosition])
        builder.addFormDataPart("expense_date",address!!.text.toString())
        builder.addFormDataPart("expense_date",address!!.text.toString())
        builder.addFormDataPart("amount",mob.text.toString().trim())
        builder.addFormDataPart("remark",remarks.text.toString().trim())

        if(imagecode.isNotEmpty()){
            builder.addFormDataPart(
                "expense_image",imagecode
            )
        }
        if(imagecode1.isNotEmpty()){
            builder.addFormDataPart(
                "expense_image2",imagecode1
            )
        }
        if(imagecode2.isNotEmpty()){
            builder.addFormDataPart(
                "expense_image3",imagecode2
            )
        }


       val requestBody = builder.build()
        call = ApproveUtils.Get.addexp(requestBody)
        call.enqueue(object : Callback<Resp_otp> {
            override fun onResponse(call: Call<Resp_otp>, response: Response<Resp_otp>) {
                Log.e("response", response.toString())
                if (response.isSuccessful()) {
                    val example = response.body() as Resp_otp
                    println(example)
                    if (example.status == "Success") {
                        var arr=example.message
                        toast(arr.toString())
                        pDialo.dismiss()
                        finish()
                        //onResume()
                    } else {
                        pDialo.dismiss()

                        Toast.makeText(
                            activity,
                            example.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
              //  pDialog.dismiss()

            }

            override fun onFailure(call: Call<Resp_otp>, t: Throwable) {
                Log.e("Fail response", t.toString())
                pDialo.dismiss()

                if (t.toString().contains("time")) {
                    Toast.makeText(
                        activity,
                        "Poor network connection",
                        Toast.LENGTH_LONG
                    ).show()
                }
                //pDialog.dismiss()

            }
        })


    }

    fun types(){
        if (Appconstants.net_status(this)) {
            nmarr.clear()
            idarr.clear()
            val call:Call<Resp_trip>

            call = ApproveUtils.Get.getexptype()

            call.enqueue(object : Callback<Resp_trip> {
                override fun onResponse(call: Call<Resp_trip>, response: Response<Resp_trip>) {
                    Log.e("areacode responce", response.toString())
                    if (response.isSuccessful()) {
                        val example = response.body() as Resp_trip
                        println(example)
                        if (example.getStatus() == "Success") {


                            nmarr.add("Select Category")
                            idarr.add("0")
                          //  textView23.visibility= View.GONE
                            var otpval=example.getResponse()
                            for(i in 0 until otpval!!.size) {
                            val id=otpval[i].id.toString()
                            val type=otpval[i].name.toString()
                            nmarr.add(type)
                            idarr.add(id)

                            }
                            val spin=ArrayAdapter(this@Request_Add,R.layout.support_simple_spinner_dropdown_item,nmarr)
                            cityspin.adapter=spin
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
                            this@Request_Add,
                            "Poor network connection",
                            Toast.LENGTH_LONG
                        ).show()

                    }
                }
            })
        }
        else{
            Toast.makeText(
                this@Request_Add,
                "You're offline",
                Toast.LENGTH_LONG
            ).show()

        }
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


}