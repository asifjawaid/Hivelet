package com.celeritassolutions.hivelet.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.celeritassolutions.hivelet.R
import com.celeritassolutions.hivelet.model.DepartmentsModel

class DashboardAdapter(val list: List<DepartmentsModel>, val context: Context) :
    RecyclerView.Adapter<DashboardAdapter.ViewHolder>() {

    private lateinit var txtDepart: TextView
    private lateinit var txtMTD: TextView
    private lateinit var txtYTD: TextView
    private lateinit var txtScore: TextView
    private lateinit var txtNext: AppCompatTextView


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            txtDepart = itemView.findViewById(R.id.txt_depart)
            txtYTD = itemView.findViewById(R.id.txt_ytd)
            txtMTD = itemView.findViewById(R.id.txt_mtd)
            txtScore = itemView.findViewById(R.id.txt_score)
            txtNext = itemView.findViewById(R.id.txt_next)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.dashboard_list_item, parent, false)

        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: DashboardAdapter.ViewHolder, position: Int) {
        val item = list[position]
        txtDepart.text = item.deptName
        txtMTD.text = item.assessment_MTD
        txtYTD.text = item.assessment_YTD
        txtScore.text = item.deptScore
    }
}