package com.elancier.team_j.retrofit

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class HouseImage {

    @SerializedName("image")
    @Expose
    var image: String? = null

}