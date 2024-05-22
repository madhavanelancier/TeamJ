package com.elancier.team_j.Adapers

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.elancier.team_j.*
import org.json.JSONArray

class MyTransactionAdap(private var items: JSONArray, private val context: Activity,val from:String) : RecyclerView.Adapter<RecyclerView.ViewHolder>(),Filterable
{



    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType:Int): RecyclerView.ViewHolder {
        when (viewType) {
            ITEM_CONTENT_VIEW_TYPE -> {
                val productView = LayoutInflater.from(viewGroup.context).inflate(R.layout.transaction_list_item, viewGroup, false)
                return ItemViewHolder(productView)
            }
            else -> {
                val productView = LayoutInflater.from(viewGroup.context).inflate(R.layout.transaction_list_item, viewGroup, false)
                return ItemViewHolder(productView)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)

        when (viewType) {
            ITEM_CONTENT_VIEW_TYPE -> {
                val itemViewHolder = holder as ItemViewHolder
                val obj = items.getJSONObject(position)
                var discount=""
                var total=""
                var sub_total=""
                val id = if (obj.has("id")) obj.getString("id") else ""
                val type = if (obj.has("amount")) obj.getString("amount") else ""
                val status = if (obj.has("expense_date")) obj.getString("expense_date") else ""
                val qty = if (obj.has("category")) obj.getString("category") else ""
                val remarks = if (obj.has("remark")) obj.getString("remark") else ""

                if(remarks.isNotEmpty()&&remarks!="null"){
                    holder.remarkslay.visibility=View.VISIBLE
                    holder.remarks.setText(remarks)
                }
                else{
                    holder.remarkslay.visibility=View.GONE
                }

                holder.r_type.setText(qty)
                holder.tours.setText(Appconstands.rupees+type)
                holder.expenditure.setText(status)






                /*holder.pldate.setText(Appconstands.rupees+CentreArrays[position].mobile)
                holder.targetval.setText(CentreArrays[position].city)
                holder.daysval.setText(CentreArrays[position].address)
                holder.status.setText(CentreArrays[position].status)
                holder.appamt.setText(Appconstands.rupees+CentreArrays[position].tnx)*/
                // Glide.with(context).load(CentreArrays[position].image).into(holder.imageView8)

                /*holder.cards.setOnClickListener {
                    (context as Request_List).imgpo(position,"Request")
                }*/
            }
            else -> {

            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return ITEM_CONTENT_VIEW_TYPE
    }

    override fun getItemCount(): Int {
        return items.length()
    }

    inner class ItemViewHolder internal constructor(rowView: View) : RecyclerView.ViewHolder(rowView), View.OnClickListener {

        internal var request_name: TextView
        internal var r_amount: TextView
        internal var r_type: TextView
        internal var r_desc: TextView
        internal var imageView8: ImageView
        internal var status: TextView
        internal var appamt: TextView
        internal var cards: CardView
        internal var tours: TextView
        internal var expenditure: TextView
        internal var billing_approval: TextView
        internal var account_approval: TextView
        internal var billing_amount: TextView
        internal var remarks: TextView
        internal var account_amount: TextView
        internal var clicklay: ConstraintLayout
        internal var sublay: LinearLayout
        internal var totallay: LinearLayout
        internal var remarkslay: LinearLayout




        init {

            request_name=rowView.findViewById(R.id.textView65) as TextView
            r_amount=rowView.findViewById(R.id.textView66) as TextView
            r_type=rowView.findViewById(R.id.textView67) as TextView
            r_desc=rowView.findViewById(R.id.textView68) as TextView
            status=rowView.findViewById(R.id.textView73) as TextView
            appamt=rowView.findViewById(R.id.textView75) as TextView
            tours=rowView.findViewById(R.id.tours) as TextView
            expenditure=rowView.findViewById(R.id.expenditure) as TextView
            billing_approval=rowView.findViewById(R.id.billing_approval) as TextView
            account_approval=rowView.findViewById(R.id.account_approval) as TextView
            billing_amount=rowView.findViewById(R.id.billing_amount) as TextView
            account_amount=rowView.findViewById(R.id.account_amount) as TextView
            remarks=rowView.findViewById(R.id.remarks) as TextView
            clicklay=rowView.findViewById(R.id.clicklay) as ConstraintLayout
            sublay=rowView.findViewById(R.id.sublay) as LinearLayout
            totallay=rowView.findViewById(R.id.totlay) as LinearLayout
            remarkslay=rowView.findViewById(R.id.remarkslay) as LinearLayout
            cards=rowView.findViewById(R.id.cards) as CardView
            imageView8=rowView.findViewById(R.id.imageView8) as ImageView
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {

        }


    }

    /*fun filter(text:String){
        val temp = JSONArray();
        for(Object d: mRecyclerViewItems){
            Reportssslistbo item = (Reportssslistbo) d;
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if(item.getTour_name().contains(text)){
                temp.add(item);
            }
        }
        //update recyclerview
        disp_adapter.updateList(temp);
    }*/
    //filter class
    private inner class RecordFilter : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val results = FilterResults()

            //Implement filter logic
            // if edittext is null return the actual list
            if (constraint == null || constraint.length == 0) {
                //No need for filter
                results.values = items
                results.count = items.length()
            } else {
                //Need Filter
                // it matches the text  entered in the edittext and set the data in adapter list
                val fRecords: JSONArray = JSONArray()
                for(s in 0 until items.length()){
                    val s2 = items.getJSONObject(s)//s
                    val name: Boolean = s2.getString("name").toUpperCase().trim()
                        .contains(constraint.toString().toUpperCase().trim { it <= ' ' })
                    /*val approved_date: Boolean = s2.getString("approved_date").toUpperCase().trim()
                            .contains(constraint.toString().toUpperCase().trim { it <= ' ' })
                    val tour: Boolean = s2.getString("").toUpperCase().trim()
                            .contains(constraint.toString().toUpperCase().trim { it <= ' ' })*/
                    if (name/* || approved_date || tour*/) {
                        fRecords.put(s2)
                    }
                }
                results.values = fRecords
                results.count = fRecords.length()
            }
            return results
        }

        override fun publishResults(
            constraint: CharSequence?,
            results: FilterResults
        ) {

            //it set the data from filter to adapter list and refresh the recyclerview adapter
            items = results.values as JSONArray
            notifyDataSetChanged()
        }
    }

    companion object {
        internal val ITEM_CONTENT_VIEW_TYPE = 1
    }

    override fun getFilter(): Filter {
        return RecordFilter()
    }
}