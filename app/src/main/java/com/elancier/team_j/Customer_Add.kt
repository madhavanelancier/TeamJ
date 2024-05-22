package com.elancier.team_j

import android.app.Activity
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.elancier.team_j.DataClass.OrderDetail
import com.elancier.team_j.retrofit.Appconstants
import com.elancier.team_j.retrofit.ApproveUtils
import com.elancier.team_j.retrofit.Resp_trip
import com.elancier.team_j.service.MediaUtils
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import kotlinx.android.synthetic.main.customer_add.cardView3
import kotlinx.android.synthetic.main.customer_add.cardView3five
import kotlinx.android.synthetic.main.customer_add.cardView3four
import kotlinx.android.synthetic.main.customer_add.cardView3three
import kotlinx.android.synthetic.main.customer_add.cardView3two
import kotlinx.android.synthetic.main.customer_add.cityErr
import kotlinx.android.synthetic.main.customer_add.doc_contact
import kotlinx.android.synthetic.main.customer_add.hospital_contact
import kotlinx.android.synthetic.main.customer_add.hospitalname
import kotlinx.android.synthetic.main.customer_add.imageErr
import kotlinx.android.synthetic.main.customer_add.img1
import kotlinx.android.synthetic.main.customer_add.img2
import kotlinx.android.synthetic.main.customer_add.img3
import kotlinx.android.synthetic.main.customer_add.img4
import kotlinx.android.synthetic.main.customer_add.img5
import kotlinx.android.synthetic.main.customer_add.pinloc
import kotlinx.android.synthetic.main.customer_add.save
import kotlinx.android.synthetic.main.customer_add.specErr
import kotlinx.android.synthetic.main.customer_add.specialization
import kotlinx.android.synthetic.main.customer_add.statespin
import kotlinx.android.synthetic.main.request_add.address
import kotlinx.android.synthetic.main.request_add.fname
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.create
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File


class Customer_Add : AppCompatActivity() {

    internal lateinit var editor: SharedPreferences.Editor
    internal lateinit var pref: SharedPreferences
    var CentresArrays = ArrayList<OrderDetail>()
    val RequestPermissionCode = 7
    internal lateinit var byteArray: ByteArray
    internal lateinit var fi: File
    internal lateinit var fi1: File
    internal lateinit var fi2: File
    internal lateinit var fi3: File
    internal lateinit var fi4: File

    var panimage = ""
    var bankimage = ""
    internal lateinit var byteArray1: ByteArray
    internal lateinit var byteArray2: ByteArray
    internal lateinit var byteArray3: ByteArray
    internal lateinit var byteArray4: ByteArray
    internal lateinit var byteArray5: ByteArray
    var imagecode = ""
    var imagecode1 = ""
    var imagecode2 = ""
    var imagecode3 = ""
    var imagecode4 = ""
    lateinit var pDialog: ProgressDialog
    internal var nmarr: MutableList<String> = ArrayList()
    internal var citynmarr: MutableList<String> = ArrayList()
    internal var ctyidarr: MutableList<String> = ArrayList()
    internal var idarr: MutableList<String> = ArrayList()
    val activity = this
    var api_key = "";
    var api_secret = "";
    var cloud_name = "";
    var frm = ""
    var imgCount = 0
    var imgCountValid = 0
    var latistr = ""
    var longstr = ""
    var image1path = ""
    var image2path = ""
    var image3path = ""
    var image4path = ""
    var image5path = ""

    var image1data = ""
    var image2data = ""
    var image3data = ""
    var image4data = ""
    var image5data = ""
    var lat = 0.0
    var longi = 0.0
    var from = ""
    var editID = ""
    var editImages = kotlin.collections.ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.customer_add)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)


        val ab = supportActionBar
        ab!!.setDisplayHomeAsUpEnabled(true)
        ab!!.setDisplayShowHomeEnabled(true)


        nmarr = ArrayList<String>()
        idarr = ArrayList<String>()
        citynmarr = ArrayList<String>()
        ctyidarr = ArrayList<String>()

        from = intent.extras!!.getString("from").toString()
        pref = applicationContext.getSharedPreferences("MyPref", 0)
        editor = pref.edit()

        pDialog = ProgressDialog(this)
        pDialog.setMessage("Processing...")
        pDialog.show()

        types()
        citytypes("")

        if (from == "View") {
            editID = intent.extras!!.getString("id").toString()
            save.visibility = View.INVISIBLE
            //getView()
            ab!!.title = "View Doctor"


        } else if (from == "Edit") {
            editID = intent.extras!!.getString("id").toString()
            save.visibility = View.VISIBLE
            save.setText("Update")
           // getViewEdit()
            ab!!.title = "Edit Doctor"
        } else {
            ab!!.title = "Add Doctor"

        }



        specialization.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View,
                position: Int,
                id: Long
            ) {
                if (position != 0) {
                    specErr.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {

            }
        })

        statespin.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View,
                position: Int,
                id: Long
            ) {
                if (position != 0) {
                    cityErr.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {

            }
        })

        pinloc.setOnClickListener {
            startActivityForResult(Intent(this, GetLocation_Admin::class.java), 120)
        }

        save.setOnClickListener {
            if (fname.text.toString().trim().isNotEmpty() && hospitalname.text.toString().trim()
                    .isNotEmpty()
                && pinloc.text.toString().trim().isNotEmpty() && address.text.toString().trim()
                    .isNotEmpty()
                && specialization.selectedItemPosition != 0 && statespin.selectedItemPosition != 0
                && (hospital_contact.text.toString().length == 10) && (doc_contact.text.toString().length == 10) && imgCountValid > 0
            ) {

                if (from == "Add") {
                    uploadMultiFile()

                } else {
                    uploadMultiFileEdit()

                }
            } else {
                if (fname.text.toString().trim().isEmpty()) {
                    fname.error = "Required field*"
                }
                if (hospitalname.text.toString().trim().isEmpty()) {
                    hospitalname.error = "Required field*"
                }
                if (pinloc.text.toString().trim().isEmpty()) {
                    pinloc.error = "Required field*"
                }
                if (address.text.toString().trim().isEmpty()) {
                    address.error = "Required field*"
                }

                if (doc_contact.text.toString().length < 10) {
                    doc_contact.error = "Invalid Mobile Number*"
                }
                if (specialization.selectedItemPosition == 0) {
                    toast("Select Specialization")
                    specErr.visibility = View.VISIBLE
                }
                if (statespin.selectedItemPosition == 0) {
                    toast("Select City")
                    cityErr.visibility = View.VISIBLE

                }

                if (hospital_contact.text.toString().length < 10) {
                    hospital_contact.error = "Invalid Mobile Number*"
                }

                if (imgCountValid == 0) {
                    toast("Please select atleast one image")
                    imageErr.visibility = View.VISIBLE

                }

            }
        }


        cardView3!!.setOnClickListener {
            imgCount = 1;
            if (CheckingPermissionIsEnabledOrNot(this)) {
                if (from == "Add"||from=="Edit") {

                    selectImage()
                } else {
                    if(image1data.isNotEmpty()) {
                        startActivity(
                            Intent(this, FullScreen::class.java)
                                .putExtra("from", hospitalname.text.toString())
                                .putExtra("image", image1data)
                        )
                    }



                }
            } else {
                RequestMultiplePermission(activity!!)
            }

        }

        cardView3two!!.setOnClickListener {
            imgCount = 2;
            if (CheckingPermissionIsEnabledOrNot(this)) {
                if (from == "Add"||from=="Edit") {

                    selectImage()
                } else {
                    if(image2data.isNotEmpty()) {
                        startActivity(
                            Intent(this, FullScreen::class.java)
                                .putExtra("from", hospitalname.text.toString())
                                .putExtra("image", image2data)
                        )
                    }

                }

            } else {
                RequestMultiplePermission(activity!!)
            }

        }
        cardView3three!!.setOnClickListener {
            imgCount = 3;

            if (CheckingPermissionIsEnabledOrNot(this)) {
                if (from == "Add"||from=="Edit") {
                    selectImage()
                } else {
                    if(image3data.isNotEmpty()) {
                        startActivity(
                            Intent(this, FullScreen::class.java)
                                .putExtra("from", hospitalname.text.toString())
                                .putExtra("image",image3data)
                        )
                    }



                }
            } else {
                RequestMultiplePermission(activity)
            }

        }

        cardView3four!!.setOnClickListener {
            imgCount = 4;

            if (CheckingPermissionIsEnabledOrNot(this)) {
                if (from == "Add") {
                    selectImage()
                } else {
                    startActivity(
                        Intent(this, FullScreen::class.java)
                            .putExtra("from", hospitalname.text.toString())
                            .putExtra("image", editImages[3])
                    )
                }
            } else {
                RequestMultiplePermission(activity)
            }

        }

        cardView3five!!.setOnClickListener {
            imgCount = 5;
            if (CheckingPermissionIsEnabledOrNot(this)) {
                if (from == "Add"||from=="Edit") {
                    selectImage()
                } else if(from=="View"){
                    startActivity(
                        Intent(this, FullScreen::class.java)
                            .putExtra("from", hospitalname.text.toString())
                            .putExtra("image", editImages[4])
                    )
                }
            } else {
                RequestMultiplePermission(activity)
            }

        }

    }


    fun CheckingPermissionIsEnabledOrNot(context: Activity): Boolean {

        val INTERNET =
            ContextCompat.checkSelfPermission(context, android.Manifest.permission.INTERNET)
        val WRITE_EXTERNAL_STORAGE = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val READ_EXTERNAL_STORAGE = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )
        val ACCESS_NETWORK_STATE = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_NETWORK_STATE
        )
        val ACCESS_FINE_LOCATION = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
        val ACCESS_COARSE_LOCATION = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
        val CAMERA = ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
        //val CALL_PHONE = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE)


        return INTERNET == PackageManager.PERMISSION_GRANTED &&
                WRITE_EXTERNAL_STORAGE == PackageManager.PERMISSION_GRANTED &&
                READ_EXTERNAL_STORAGE == PackageManager.PERMISSION_GRANTED &&
                ACCESS_NETWORK_STATE == PackageManager.PERMISSION_GRANTED &&
                ACCESS_FINE_LOCATION == PackageManager.PERMISSION_GRANTED &&
                ACCESS_COARSE_LOCATION == PackageManager.PERMISSION_GRANTED && //&&
                CAMERA == PackageManager.PERMISSION_GRANTED //&&
        //CALL_PHONE == PackageManager.PERMISSION_GRANTED
    }

    fun RequestMultiplePermission(context: Activity) {

        // Creating String Array with Permissions.
        ActivityCompat.requestPermissions(
            context, arrayOf<String>
                (
                android.Manifest.permission.INTERNET,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.ACCESS_NETWORK_STATE,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.CAMERA
            ), Appconstands.RequestPermissionCode
        )


    }

    fun selectImage() {
        ImagePicker.with(this)
            .saveDir(getExternalFilesDir(null)!!)
            //.crop()	    			//Crop image(Optional), Check Customization for more option
            .compress(1024)            //Final image size will be less than 1 MB(Optional)
            .maxResultSize(
                1080,
                1080
            )    //Final image resolution will be less than 1080 x 1080(Optional)
            .start()
    }


    private fun uploadMultiFile() {
        var progressDialog = ProgressDialog(this);
        progressDialog.setMessage("Creating Doctor...");
        progressDialog.show()
        val filePaths: java.util.ArrayList<String> = java.util.ArrayList()

        Log.e("filepaths", filePaths.toString())
        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
        builder.addFormDataPart("name", fname.text.toString())
        builder.addFormDataPart("employee", pref!!.getString("empid", "").toString())
        builder.addFormDataPart(
            "specialization",
            idarr[specialization.selectedItemPosition].toString()
        )
        builder.addFormDataPart("clinic", hospitalname.text.toString())
        builder.addFormDataPart("address", address.text.toString())
        builder.addFormDataPart("city", ctyidarr[statespin.selectedItemPosition].toString())
        builder.addFormDataPart("doctor_contact_number", doc_contact.text.toString())
        builder.addFormDataPart("hospital_contact_number", hospital_contact.text.toString())
        builder.addFormDataPart("location_pin", pinloc.text.toString())
        builder.addFormDataPart("lat", latistr)
        builder.addFormDataPart("long", longstr)
        builder.addFormDataPart("clinic_photo", imagecode)
        builder.addFormDataPart("clinic_photo1", imagecode1)
        builder.addFormDataPart("clinic_photo2", imagecode2)


        // Map is used to multipart the file using okhttp3.RequestBody
        // Multiple Images

        val file = File("")
        val requestBody = builder.build()
        Log.e("request", requestBody.parts.toString())
        val call: Call<ResponseBody?>? = ApproveUtils.Get.addDoctor(requestBody)
        call!!.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(
                call: Call<ResponseBody?>,
                response: Response<ResponseBody?>
            ) {
                /* Log.w(
                     "json",
                     Gson().toJson(response)
                 )*/
                if (response.isSuccessful) {
                    Toast.makeText(activity, "Doctor Added Successfully", Toast.LENGTH_SHORT)
                        .show()
                    finish()
                } else {
                    Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT)

                }


                progressDialog.dismiss()
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                progressDialog.dismiss()
                Log.d("failure", "Error " + t.message)
                Toast.makeText(activity, t.toString(), Toast.LENGTH_SHORT)

            }
        })

    }

    private fun uploadMultiFileEdit() {
        var progressDialog = ProgressDialog(this);
        progressDialog.setMessage("Updating Doctor...");
        progressDialog.show()

        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
        builder.addFormDataPart("name", fname.text.toString())
        builder.addFormDataPart("employee", pref!!.getString("empid", "").toString())
        builder.addFormDataPart(
            "specialization",
            idarr[specialization.selectedItemPosition].toString()
        )
        builder.addFormDataPart("clinic", hospitalname.text.toString())
        builder.addFormDataPart("address", address.text.toString())
        builder.addFormDataPart("city", ctyidarr[statespin.selectedItemPosition].toString())
        builder.addFormDataPart("doctor_contact_number", doc_contact.text.toString())
        builder.addFormDataPart("hospital_contact_number", hospital_contact.text.toString())
        builder.addFormDataPart("location_pin", pinloc.text.toString())
        builder.addFormDataPart("lat", latistr)
        builder.addFormDataPart("long", longstr)
        builder.addFormDataPart("clinic_photo", imagecode)
        builder.addFormDataPart("clinic_photo1", imagecode1)
        builder.addFormDataPart("clinic_photo2", imagecode2)

        // Map is used to multipart the file using okhttp3.RequestBody
        // Multiple Images

        val file = File("")
        val requestBody = builder.build()
        Log.e("request", requestBody.parts.toString())
        val call: Call<ResponseBody?>? = ApproveUtils.Get.editDoctor(requestBody, editID)
        call!!.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(
                call: Call<ResponseBody?>,
                response: Response<ResponseBody?>
            ) {
                /* Log.w(
                     "json",
                     Gson().toJson(response)
                 )*/
                if (response.isSuccessful) {
                    Toast.makeText(activity, "Doctor Updated Successfully", Toast.LENGTH_SHORT)
                        .show()
                    finish()
                } else {
                    Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT)

                }


                progressDialog.dismiss()
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                progressDialog.dismiss()
                Log.d("failure", "Error " + t.message)
                Toast.makeText(activity, t.toString(), Toast.LENGTH_SHORT)

            }
        })


    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2404) {
            if (resultCode != RESULT_CANCELED) {
                if (resultCode == Activity.RESULT_OK && null != data) {
                    val selectedImage: Uri? = data.data

                    /*val myBitmap = Uri.fromFile(File(picturePath))
            val bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), myBitmap)
            val stream = ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            val byteArray = stream.toByteArray()*/;
                    val bitmap = MediaStore.Images.Media.getBitmap(
                        activity!!.getBaseContext().getContentResolver(),
                        selectedImage
                    )

                    if (imgCount == 1) {
                        imgCountValid = 1
                        //image1data = ""//Base64.encodeToString(byteArray, Base64.DEFAULT)
                        //image1path = selectedImage.toString()
                        imageErr.visibility = View.GONE
                        img1!!.setImageBitmap(bitmap)

                        val stream = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream)
                        byteArray = stream.toByteArray()
                        imagecode = Base64.encodeToString(
                            byteArray,
                            Base64.DEFAULT
                        )//setImageBitmap(modifyOrientation(BitmapFactory.decodeFile(picturePath),picturePath))//(BitmapFactory.decodeFile(picturePath))
                    } else if (imgCount == 2) {
                        imgCountValid = 2
                        img2!!.setImageBitmap(bitmap)
                        val stream = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream)
                        byteArray = stream.toByteArray()
                        imagecode1 = Base64.encodeToString(
                            byteArray,
                            Base64.DEFAULT
                        )//setImageBitmap(modifyOrientation(BitmapFactory.decodeFile(picturePath),picturePath))//(BitmapFactory.decodeFile(picturePath))
                        imageErr.visibility = View.GONE

                    } else if (imgCount == 3) {
                        imgCountValid = 3
                        img3!!.setImageBitmap(bitmap)
                        val stream = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream)
                        byteArray = stream.toByteArray()
                        imagecode2 = Base64.encodeToString(
                            byteArray,
                            Base64.DEFAULT
                        )//setImageBitmap(modifyOrientation(BitmapFactory.decodeFile(picturePath),picturePath))//(BitmapFactory.decodeFile(picturePath))
                        imageErr.visibility = View.GONE

                    }
                    //println("image Data : "+Base64.encodeToString(byteArray, Base64.DEFAULT))
                }
            }
        }
        if (requestCode == 120) {
            if (resultCode != RESULT_CANCELED) {
                if (resultCode == Activity.RESULT_OK && null != data) {
                    latistr = data.getStringExtra("lattitude").toString()
                    longstr = data.getStringExtra("longitude").toString()
                    address.setText(data.getStringExtra("location").toString())
                    pinloc.setText(data.getStringExtra("pincode").toString())
                    pinloc.setError(null)
                    address.setError(null)
                }
            }
        }
    }


    fun getView() {
        if (Appconstants.net_status(this)) {

            val call: Call<Resp_trip>
            call = ApproveUtils.Get.getDoctor(editID)
            call.enqueue(object : Callback<Resp_trip> {
                override fun onResponse(call: Call<Resp_trip>, response: Response<Resp_trip>) {
                    Log.e("areacode responce", response.toString())
                    if (response.isSuccessful()) {
                        val example = response.body() as Resp_trip
                        println(example)
                        if (example.getStatus() == "Success") {

                            //  textView23.visibility= View.GONE
                            var otpval = example.getResponse()
                            for (i in 0 until otpval!!.size) {
                                val id = otpval[i].id.toString()
                                val type = otpval[i].name.toString()
                                fname.setText(otpval[i].name.toString())
                                fname.setFocusable(false)
                                if (from == "View") {
                                    for (j in 0 until idarr.size) {
                                        if (idarr[j] == otpval[i].specialization) {
                                            specialization.setSelection(j)

                                        }
                                    }
                                    specialization.setEnabled(false)
                                    for (k in 0 until ctyidarr.size) {
                                        if (ctyidarr[k] == otpval[i].city) {
                                            statespin.setSelection(k)
                                        }
                                    }
                                    statespin.setEnabled(false)

                                }

                                hospitalname.setText(otpval[i].clinic)
                                hospitalname.setFocusable(false)

                                pinloc.setText(otpval[i].location_pin)
                                pinloc.setFocusable(false)


                                address.setText(otpval[i].address)
                                address.setFocusable(false)


                                doc_contact.setText(otpval[i].doctor_contact_number)
                                doc_contact.setFocusable(false)


                                hospital_contact.setText(otpval[i].hospital_contact_number)
                                hospital_contact.setFocusable(false)


                                try {
                                    if (otpval[i].clinic_photo!!.isNotEmpty() && otpval[i].clinic_photo != null) {
                                        Glide.with(this@Customer_Add).load(otpval[i].clinic_photo!!)
                                            .placeholder(R.drawable.loading_icon).into(img1)
                                        image1data=otpval[i].clinic_photo!!

                                    }
                                } catch (e: Exception) {

                                }
                                try {
                                    if (otpval[i].clinic_photo1!!.isNotEmpty() && otpval[i].clinic_photo1 != null) {
                                        Glide.with(this@Customer_Add)
                                            .load(otpval[i].clinic_photo1!!)
                                            .placeholder(R.drawable.loading_icon).into(img2)
                                        image2data=otpval[i].clinic_photo1!!
                                    }
                                } catch (e: Exception) {

                                }
                                try {

                                    if (otpval[i].clinic_photo2!!.isNotEmpty() && otpval[i].clinic_photo2 != null) {
                                        Glide.with(this@Customer_Add)
                                            .load(otpval[i].clinic_photo2!!)
                                            .placeholder(R.drawable.loading_icon).into(img3)
                                        image3data=otpval[i].clinic_photo2!!

                                    }
                                } catch (e: Exception) {

                                }

                            }
                            pDialog.dismiss()

                        } else {
                            toast(example.getMessage().toString())
                        }
                    } else {

                    }
                }

                override fun onFailure(call: Call<Resp_trip>, t: Throwable) {
                    Log.e("areacode Fail response", t.toString())
                    if (t.toString().contains("time")) {
                        Toast.makeText(
                            this@Customer_Add,
                            "Poor network connection",
                            Toast.LENGTH_LONG
                        ).show()

                    }
                }
            })
        } else {
            Toast.makeText(
                this@Customer_Add,
                "You're offline",
                Toast.LENGTH_LONG
            ).show()

        }
    }

    fun getViewEdit() {
        if (Appconstants.net_status(this)) {

            val call: Call<Resp_trip>
            call = ApproveUtils.Get.getDoctor(editID)
            call.enqueue(object : Callback<Resp_trip> {
                override fun onResponse(call: Call<Resp_trip>, response: Response<Resp_trip>) {
                    Log.e("areacode responce", response.toString())
                    if (response.isSuccessful()) {
                        val example = response.body() as Resp_trip
                        println(example)
                        if (example.getStatus() == "Success") {

                            //  textView23.visibility= View.GONE
                            var otpval = example.getResponse()
                            for (i in 0 until otpval!!.size) {
                                val id = otpval[i].id.toString()
                                val type = otpval[i].name.toString()
                                fname.setText(otpval[i].name.toString())

                                if (from == "Edit") {
                                    for (j in 0 until idarr.size) {
                                        if (idarr[j] == otpval[i].specialization) {
                                            specialization.setSelection(j)
                                        }
                                    }

                                    for (k in 0 until ctyidarr.size) {
                                        if (ctyidarr[k] == otpval[i].city) {
                                            statespin.setSelection(k)
                                        }
                                    }
                                }

                                hospitalname.setText(otpval[i].clinic)

                                pinloc.setText(otpval[i].location_pin)

                                address.setText(otpval[i].address)

                                doc_contact.setText(otpval[i].doctor_contact_number)

                                hospital_contact.setText(otpval[i].hospital_contact_number)

                                try {
                                    if (otpval[i].clinic_photo!!.isNotEmpty() && otpval[i].clinic_photo != null) {
                                        imgCountValid = 1
                                        Glide.with(this@Customer_Add)
                                            .load(otpval[i].clinic_photo!!)
                                            .placeholder(R.drawable.loading_icon).into(img1)
                                    }
                                } catch (e: Exception) {

                                }
                                try {
                                    if (otpval[i].clinic_photo1!!.isNotEmpty() && otpval[i].clinic_photo1 != null) {
                                        imgCountValid = 2

                                        Glide.with(this@Customer_Add)
                                            .load(otpval[i].clinic_photo1!!)
                                            .placeholder(R.drawable.loading_icon).into(img2)
                                    }
                                } catch (e: Exception) {

                                }
                                try {
                                    if (otpval[i].clinic_photo2!!.isNotEmpty() && otpval[i].clinic_photo2 != null) {
                                        imgCountValid = 3

                                        Glide.with(this@Customer_Add)
                                            .load(otpval[i].clinic_photo2!!)
                                            .placeholder(R.drawable.loading_icon).into(img3)
                                    }
                                } catch (e: Exception) {

                                }


                            }
                            pDialog.dismiss()

                        } else {
                            toast(example.getMessage().toString())
                        }
                    } else {

                    }
                }

                override fun onFailure(call: Call<Resp_trip>, t: Throwable) {
                    Log.e("areacode Fail response", t.toString())
                    if (t.toString().contains("time")) {
                        Toast.makeText(
                            this@Customer_Add,
                            "Poor network connection",
                            Toast.LENGTH_LONG
                        ).show()

                    }
                }
            })
        } else {
            Toast.makeText(
                this@Customer_Add,
                "You're offline",
                Toast.LENGTH_LONG
            ).show()

        }
    }


    fun types() {
        if (Appconstants.net_status(this)) {
            nmarr.clear()
            idarr.clear()
            val call: Call<Resp_trip>
            call = ApproveUtils.Get.getspecialized()
            call.enqueue(object : Callback<Resp_trip> {
                override fun onResponse(call: Call<Resp_trip>, response: Response<Resp_trip>) {
                    Log.e("areacode responce", response.toString())
                    if (response.isSuccessful()) {
                        val example = response.body() as Resp_trip
                        println(example)
                        if (example.getStatus() == "Success") {

                            nmarr.add("Select Specialization")
                            idarr.add("0")
                            //  textView23.visibility= View.GONE
                            var otpval = example.getResponse()
                            for (i in 0 until otpval!!.size) {
                                val id = otpval[i].id.toString()
                                val type = otpval[i].name.toString()
                                nmarr.add(type)
                                idarr.add(id)


                            }
                            val spin = ArrayAdapter(
                                this@Customer_Add,
                                R.layout.support_simple_spinner_dropdown_item,
                                nmarr
                            )
                            specialization.adapter = spin

                            pDialog.dismiss()

                        } else {
                            toast(example.getMessage().toString())
                        }
                    } else {

                    }
                }

                override fun onFailure(call: Call<Resp_trip>, t: Throwable) {
                    Log.e("areacode Fail response", t.toString())
                    if (t.toString().contains("time")) {
                        Toast.makeText(
                            this@Customer_Add,
                            "Poor network connection",
                            Toast.LENGTH_LONG
                        ).show()

                    }
                }
            })
        } else {
            Toast.makeText(
                this@Customer_Add,
                "You're offline",
                Toast.LENGTH_LONG
            ).show()

        }
    }

    fun citytypes(state: String) {
        if (Appconstants.net_status(this)) {
            citynmarr.clear()
            ctyidarr.clear()
            val call: Call<Resp_trip>
            call = ApproveUtils.Get.getcities()
            call.enqueue(object : Callback<Resp_trip> {
                override fun onResponse(call: Call<Resp_trip>, response: Response<Resp_trip>) {
                    Log.e("areacode responce", response.toString())
                    if (response.isSuccessful()) {
                        val example = response.body() as Resp_trip
                        println(example)
                        if (example.getStatus() == "Success") {

                            citynmarr.add("Select City")
                            ctyidarr.add("0")
                            //  textView23.visibility= View.GONE
                            var otpval = example.getResponse()
                            for (i in 0 until otpval!!.size) {
                                val id = otpval[i].id.toString()
                                val type = otpval[i].name.toString()
                                citynmarr.add(type)
                                ctyidarr.add(id)

                            }
                            val spin = ArrayAdapter(
                                this@Customer_Add,
                                R.layout.support_simple_spinner_dropdown_item,
                                citynmarr
                            )
                            statespin.adapter = spin
                            pDialog.dismiss()
                            if (from == "View") {
                                getView()
                            } else if (from == "Edit") {
                                getViewEdit()
                            } else {

                            }

                        } else {
                            toast(example.getMessage().toString())
                        }
                    } else {

                    }
                }

                override fun onFailure(call: Call<Resp_trip>, t: Throwable) {
                    Log.e("areacode Fail response", t.toString())
                    if (t.toString().contains("time")) {
                        Toast.makeText(
                            this@Customer_Add,
                            "Poor network connection",
                            Toast.LENGTH_LONG
                        ).show()


                    }
                }
            })
        } else {
            Toast.makeText(
                this@Customer_Add,
                "You're offline",
                Toast.LENGTH_LONG
            ).show()

        }
    }

    fun toast(msg: String) {
        val t = Toast.makeText(this, msg, Toast.LENGTH_LONG)
        t.setGravity(Gravity.CENTER, 0, 0)
        t.show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        //menuInflater.inflate(R.menu.receipt, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        when (item.itemId) {
            android.R.id.home -> {

                exit()
                return true
            }

            else -> return super.onOptionsItemSelected(item)
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
}

