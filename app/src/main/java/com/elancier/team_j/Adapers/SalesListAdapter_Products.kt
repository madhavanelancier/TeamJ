package com.elancier.team_j.Adapers

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.elancier.team_j.*
import org.json.JSONArray

class SalesListAdapter_Products(private var items: JSONArray, private val context: Activity,private val from:String) : RecyclerView.Adapter<RecyclerView.ViewHolder>(),Filterable
{

    interface OnItemClickListener {
        fun OnExpenseItemClick(view: View, position:Int, viewType:Int)
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType:Int): RecyclerView.ViewHolder {
        when (viewType) {
            ITEM_CONTENT_VIEW_TYPE -> {
                val productView = LayoutInflater.from(viewGroup.context).inflate(R.layout.sale_item, viewGroup, false)
                return ItemViewHolder(productView)
            }
            else -> {
                val productView = LayoutInflater.from(viewGroup.context).inflate(R.layout.sale_item, viewGroup, false)
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
                val id = if (obj.has("id")) obj.getString("id") else ""
                val type = if (obj.has("sid")) obj.getString("sid") else ""
                val brand = if (obj.has("brand")) obj.getString("brand") else ""
                val status = if (obj.has("product")) obj.getString("product") else ""
                val qty = if (obj.has("qty")) obj.getString("qty") else ""
                val price = if (obj.has("price")) obj.getString("price") else ""
                val unit_price = if (obj.has("unit_price")) obj.getString("unit_price") else ""
                val discount = if (obj.has("discount")) obj.getString("discount") else ""
                val discount_amt = if (obj.has("dtime")) obj.getString("dtime") else ""


                if(from=="customer"){
                    holder.orddt.setText("Invoice Date")
                }
                else{
                    holder.orddt.setText("Order Date")

                }

                holder.request_name.visibility=View.GONE
                //holder.request_name.setText(name)
                holder.tours.setText(price)
                holder.brand.setText(brand)


                holder.r_type.setText(status)
                holder.r_amount.setText(qty+" Item(s)")
                holder.expenditure.setText(unit_price.toString())
                holder.billing_approval.setText(discount.toString())
                holder.account_approval.setText(discount_amt)




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
        internal var orddt: TextView
        internal var brand: TextView
        internal var billing_approval: TextView
        internal var account_approval: TextView
        internal var billing_amount: TextView
        internal var account_amount: TextView
        internal var label_amount: TextView
        internal var clicklay: ConstraintLayout




        init {

            request_name=rowView.findViewById(R.id.textView65) as TextView
            r_amount=rowView.findViewById(R.id.textView66) as TextView
            r_type=rowView.findViewById(R.id.textView67) as TextView
            r_desc=rowView.findViewById(R.id.textView68) as TextView
            orddt=rowView.findViewById(R.id.orddt) as TextView
            status=rowView.findViewById(R.id.textView73) as TextView
            appamt=rowView.findViewById(R.id.textView70) as TextView
            tours=rowView.findViewById(R.id.tours) as TextView
            brand=rowView.findViewById(R.id.textViewbrand) as TextView
            expenditure=rowView.findViewById(R.id.expenditure) as TextView
            billing_approval=rowView.findViewById(R.id.billing_approval) as TextView
            account_approval=rowView.findViewById(R.id.account_approval) as TextView
            billing_amount=rowView.findViewById(R.id.billing_amount) as TextView
            account_amount=rowView.findViewById(R.id.account_amount) as TextView
            label_amount=rowView.findViewById(R.id.textView66lbl) as TextView
            clicklay=rowView.findViewById(R.id.clicklay) as ConstraintLayout
            cards=rowView.findViewById(R.id.cards) as CardView
            imageView8=rowView.findViewById(R.id.imageView8) as ImageView
            itemView.setOnClickListener(this)
        }
        override fun onClick(view: View) {
            /*try {
                listener.OnRequestItemClick(view, adapterPosition,
                        ITEM_CONTENT_VIEW_TYPE
                )
                //(context as Customers_List).Editorders(position)
            } catch (e: Exception) {

            }*/
            val obj = items.getJSONObject(position)
            val statusval = if (obj.has("status")) obj.getString("status") else ""
            if (statusval.toString().toLowerCase() == "pending") {
                val yu = Intent(context, AdvanceRequest::class.java)
                yu.putExtra("data", items.getJSONObject(adapterPosition).toString())
                context.startActivity(yu)
            }
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