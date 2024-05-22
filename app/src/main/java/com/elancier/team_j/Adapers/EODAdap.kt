package com.elancier.team_j.Adapers


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.elancier.team_j.Eod_list
import com.elancier.team_j.Meeting_Add
import com.elancier.team_j.R
import com.elancier.team_j.Schedule_Meeting

class EODAdap(private val context: Context, private val CentreArrays: ArrayList<Eod_list.EOD_data>, private val listener: OnItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{

    interface OnItemClickListener {
        fun OnItemClick(view:View,position:Int,viewType:Int)
    }


    override fun onCreateViewHolder(viewGroup:ViewGroup, viewType:Int): RecyclerView.ViewHolder {
        val productView = LayoutInflater.from(viewGroup.context).inflate(R.layout.meeting_list_item, viewGroup, false)
        return ItemViewHolder(productView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)

        when (viewType) {
            ITEM_CONTENT_VIEW_TYPE -> {
                val itemViewHolder = holder as ItemViewHolder

                holder.nm.setText(CentreArrays[position].cname.toString())//(CentreArrays.getJSONObject(position).getString("cname"))
                holder.mob.setText(CentreArrays[position].mmob.toString())//(CentreArrays.getJSONObject(position).getString("mmob"))
                holder.tval.setText(CentreArrays[position].tval.toString())//(if(CentreArrays.getJSONObject(position).has("tval")) CentreArrays.getJSONObject(position).getString("tval") else "")
                holder.created.setText(CentreArrays[position].sval.toString())//(if(CentreArrays.getJSONObject(position).has("tval")) CentreArrays.getJSONObject(position).getString("tval") else "")
                holder.nm.isSelected=true
                holder.mob.isSelected=true

                holder.edit.setOnClickListener {
                    context.startActivity(Intent(context,Schedule_Meeting::class.java)
                        .putExtra("from","Edit")
                        .putExtra("id",CentreArrays[position].customerID.toString())
                        .putExtra("docid",CentreArrays[position].ctype.toString())
                        .putExtra("dcname",CentreArrays[position].cname.toString())
                        .putExtra("date",CentreArrays[position].mmob.toString())
                        .putExtra("status",CentreArrays[position].sval.toString())
                        .putExtra("lat",CentreArrays[position].intime.toString())
                        .putExtra("long",CentreArrays[position].outtime.toString()))
                }

                holder.feedback.setOnClickListener {
                    context.startActivity(Intent(context,Meeting_Add::class.java)
                        .putExtra("id",CentreArrays[position].customerID.toString())
                        .putExtra("dcname",CentreArrays[position].cname.toString())
                        .putExtra("date",CentreArrays[position].mmob.toString())
                        .putExtra("status",CentreArrays[position].sval.toString()))
                }
            }
            else -> {

            }
        }
    }

    override fun getItemViewType(position: Int): Int {

        //val recyclerViewItem = CentreArrays[position]
        /*if (recyclerViewItem instanceof Confirmmodel) {
            return ITEM_CONFIRM_VIEW_TYPE;
        }*/
        return ITEM_CONTENT_VIEW_TYPE
    }

    override fun getItemCount(): Int {
        return CentreArrays.size
    }

    inner class ItemViewHolder internal constructor(rowView: View) : RecyclerView.ViewHolder(rowView), View.OnClickListener {

        internal lateinit var nm: TextView
        internal lateinit var mob: TextView
        internal lateinit var tval: TextView
        internal lateinit var created: TextView
        internal lateinit var location: TextView
        internal lateinit var pldate: TextView
        internal lateinit var targetval: TextView
        internal lateinit var daysval: TextView
        internal lateinit var budgetval: TextView
        internal lateinit var feedback: ImageButton

        internal lateinit var frmdate: TextView
        internal lateinit var todate: TextView
        internal lateinit var edit: ImageButton
        internal lateinit var newcnval: TextView
        internal lateinit var tidval: TextView
        internal lateinit var textView64: TextView


        init {

            nm = rowView.findViewById(R.id.textView87) as TextView
            mob = rowView.findViewById(R.id.textView88) as TextView
            tval = rowView.findViewById(R.id.textView89) as TextView
            feedback = rowView.findViewById(R.id.feedback) as ImageButton
            edit = rowView.findViewById(R.id.edit) as ImageButton
            created = rowView.findViewById(R.id.status) as TextView
            /*location = rowView.findViewById(R.id.textView52) as TextView
            pldate = rowView.findViewById(R.id.textView57) as TextView
            targetval = rowView.findViewById(R.id.textView57) as TextView
            daysval = rowView.findViewById(R.id.days) as TextView
            budgetval = rowView.findViewById(R.id.budget) as TextView
            frmdate = rowView.findViewById(R.id.textView58) as TextView
            todate = rowView.findViewById(R.id.textView60) as TextView
            excnval = rowView.findViewById(R.id.excn) as TextView
            newcnval = rowView.findViewById(R.id.newcn) as TextView
            tidval = rowView.findViewById(R.id.tid) as TextView
            textView64 = rowView.findViewById(R.id.textView64) as TextView*/

            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            try {
                listener.OnItemClick(
                    view, adapterPosition,
                    ITEM_CONTENT_VIEW_TYPE
                )

            } catch (e: Exception) {

            }


        }

        private var fRecords: Filter? = null


        /*void filter(String text){
        List<Reportssslistbo> temp = new ArrayList<Reportssslistbo>();
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
        /*private inner class RecordFilter : Filter() {
            protected override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()

                //Implement filter logic
                // if edittext is null return the actual list
                if (constraint == null || constraint.length == 0) {
                    //No need for filter
                    results.values = mRecyclerViewItems
                    results.count = mRecyclerViewItems.size()
                } else {
                    //Need Filter
                    // it matches the text  entered in the edittext and set the data in adapter list
                    val fRecords: ArrayList<Reportssslistbo> = ArrayList<Reportssslistbo>()
                    for (s in mRecyclerViewItems) {
                        val s2: Reportssslistbo = s as Reportssslistbo
                        val name: Boolean = s2.getPname().toUpperCase().trim()
                            .contains(constraint.toString().toUpperCase().trim { it <= ' ' })
                        val mob: Boolean = s2.getPhno().toUpperCase().trim()
                            .contains(constraint.toString().toUpperCase().trim { it <= ' ' })
                        val tour: Boolean = s2.getTour_name().toUpperCase().trim()
                            .contains(constraint.toString().toUpperCase().trim { it <= ' ' })
                        if (name || mob || tour) {
                            fRecords.add(s2)
                        }
                    }
                    results.values = fRecords
                    results.count = fRecords.size
                }
                return results
            }

            protected override fun publishResults(
                constraint: CharSequence?,
                results: FilterResults
            ) {

                //it set the data from filter to adapter list and refresh the recyclerview adapter
                mRecyclerViewItems = results.values as ArrayList<Any?>
                notifyDataSetChanged()
            }
        }


    }*/
    }
    companion object {
        internal val ITEM_CONTENT_VIEW_TYPE = 1
    }




}