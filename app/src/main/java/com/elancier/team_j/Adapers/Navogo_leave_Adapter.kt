package com.elancier.team_j.Adapers

import android.app.Dialog
import android.content.Context
import android.view.*
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.elancier.team_j.Navogo_LeaveList
import com.elancier.team_j.PickupModel
import com.elancier.team_j.R


class Navogo_leave_Adapter(context: Context, items: List<PickupModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items: List<PickupModel> = ArrayList<PickupModel>()
    private val ctx: Context

    init {
        this.items = items
        ctx = context
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        // each data item is just a string in this case
        var date: TextView
        var date_header: TextView? = null
        var name: TextView
        var intime: TextView
        var layout: CardView? = null
        var lvtype: TextView
        var days: TextView? = null
        var apprsts: TextView? = null
        var rejsts: TextView? = null
        var duration: TextView
        var durationLBL: TextView
        var cancelLeave: TextView
        var more: ImageButton? = null
        var lyt_parent: View? = null

        init {
            //image = (CircularImageView) v.findViewById(R.id.image);
            name = v.findViewById<View>(R.id.textView5) as TextView
            lvtype = v.findViewById<View>(R.id.textView5s) as TextView
            days = v.findViewById<View>(R.id.textView9) as TextView
            date = v.findViewById<View>(R.id.view) as TextView
            apprsts = v.findViewById<View>(R.id.textView10) as TextView
            rejsts = v.findViewById<View>(R.id.textView11) as TextView
            //date_header = (TextView) v.findViewById(R.id.textView13);
            // more = (ImageButton) v.findViewById(R.id.more);
            duration = v.findViewById<View>(R.id.textView7) as TextView
            durationLBL = v.findViewById<View>(R.id.textView63dur) as TextView
            cancelLeave = v.findViewById<View>(R.id.textView8) as TextView
            //lyt_parent = (View) v.findViewById(R.id.lyt_parent);
            intime = v.findViewById<View>(R.id.textView6) as TextView
            // layout=(CardView) v.findViewById(R.id.lay);
            //ip=(TextView) v.findViewById(R.id.ip);
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder
        val v: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.navogo_leavelist_item, parent, false)
        vh = ViewHolder(v)
        return vh
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            val view = holder
            val item = items[position]

            view.lvtype.setText(": " + item.leave_type!!)

            if(item.leave_type=="Permission"){
                view.durationLBL.setText("Total Hours")
            }
            else{
                view.durationLBL.setText("Total Days")

            }

            view.duration.setText(": " + item.total_days)
            view.days!!.setText(": " + item.fdate)

            view.apprsts!!.setText(": " + item.to_date)
            view.rejsts!!.setText(": " + item.deps_reason)


            view.cancelLeave.setOnClickListener {
                (ctx as Navogo_LeaveList).CancelLeave(item.id.toString())
            }




            if (item.status == "1") {
                view.intime.setText("Processing")
                view.cancelLeave.visibility=View.VISIBLE
                view.intime.setBackgroundResource(R.drawable.orange_border)
            } else if (item.status == "2") {
                view.intime.setText("Approved")
                view.intime.setBackgroundResource(R.drawable.green_border)
                view.cancelLeave.visibility=View.GONE

            } else if (item.status == "3") {
                view.intime.setText("Rejected")
                view.intime.setBackgroundResource(R.drawable.red_border)
                view.cancelLeave.visibility=View.GONE

            }
            else if (item.status == "4") {
                view.intime.setText("Cancelled")
                view.intime.setBackgroundResource(R.drawable.red_border)
                view.cancelLeave.visibility=View.GONE

            }

        }
    }


    /*  private void onMoreButtonClick(final View view, final People people) {
        PopupMenu popupMenu = new PopupMenu(ctx, view);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onMoreButtonClickListener.onItemClick(view, people, item);

                return true;
            }
        });
        popupMenu.inflate(R.menu.menu_people_more);
        popupMenu.show();
    }*/
    /* private void onMoreButtonClick1(final View view, final People people) {
        PopupMenu popupMenu = new PopupMenu(ctx, view);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onMoreButtonClickListener.onItemClick(view, people, item);

                return true;
            }
        });
        popupMenu.inflate(R.menu.menu_people_more_de);
        popupMenu.show();
    }*/
    override fun getItemCount(): Int {
        return items.size
    }


}