package com.elancier.team_j.retrofit

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Message_Home {

    @SerializedName("data")
    @Expose
     var data: List<Datum_Home>? = null



}