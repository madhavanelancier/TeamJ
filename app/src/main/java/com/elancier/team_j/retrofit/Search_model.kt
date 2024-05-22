package com.elancier.team_j.retrofit


import com.elancier.team_j.DataClass.otpdata
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Search_model {

    @SerializedName("Status")
    @Expose
    var status: String? = null
    @SerializedName("Total")
    @Expose
    var message: String? = null
    @SerializedName("Response")
    @Expose
    var response: List<otpdata>? = null


}
