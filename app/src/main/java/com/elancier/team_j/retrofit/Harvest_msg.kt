package com.elancier.team_j.retrofit

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Harvest_msg {

    @SerializedName("data")
    @Expose
    var data: List<Harvest_data>? = null

}