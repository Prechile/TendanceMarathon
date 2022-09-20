package com.tendance.marathon.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.tendance.marathon.R
import com.tendance.marathon.models.AgentTicket
import java.util.*

class HistoriqueAdapter (var mCtx: Context,
                    var operationData: ArrayList<AgentTicket>
) : BaseAdapter() {

    override fun getCount(): Int {
        return operationData.size
    }

    override fun getItem(position: Int): Any {
        return operationData[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = mCtx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView: View = inflater.inflate(R.layout.item_all_av, parent, false)
        val textView = rowView.findViewById<View>(R.id.eventname) as TextView
        val textView1 = rowView.findViewById<View>(R.id.name) as TextView
        val textView2 = rowView.findViewById<View>(R.id.ref) as TextView
        val textView3 = rowView.findViewById<View>(R.id.phone) as TextView
        val textView4 = rowView.findViewById<View>(R.id.dossar) as TextView

        val data = operationData[position]
        textView.text = data.eventName
        textView1.text = data.clientName
        textView2.text = data.clientPhoneNumber
        textView3.text = data.number
        textView4.text = data.dossar

        return rowView
    }
}
