package com.elancier.team_j.Adapers

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.elancier.team_j.R
import com.elancier.team_j.retrofit.StateData


class StateAdapter(
        private val context: Activity, //to store the list of countries
        private val exe: ArrayList<StateData.StateResponse>) : ArrayAdapter<Any>(context, R.layout.dropdown_item,
    (exe as List<Any>?)!!
) {
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {

        //val inflater = context.layoutInflater
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView = inflater.inflate(R.layout.dropdown_item, null, true)
        val Fid = rowView.findViewById<TextView>(android.R.id.text1)
        val e = exe[position]
        Fid.setText(e.state_name)

        return rowView

    }
}