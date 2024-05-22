package com.elancier.team_j.retrofit


import com.elancier.team_j.DataClass.otpdata
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Resp_otps {

    @SerializedName("status")
    @Expose
    var status:String? = null

    @SerializedName("message")
    @Expose
    var message:String? = null

    @SerializedName("response")
    @Expose
    var response:otpdata? = null


}
