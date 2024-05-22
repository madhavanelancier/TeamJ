package com.elancier.team_j.retrofit

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Edit_Message {

    @SerializedName("data")
    @Expose
    var data: List<Edit_Datum>? = null

}