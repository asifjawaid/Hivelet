package com.celeritassolutions.hivelet.adapter

import android.content.Context


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.celeritassolutions.hivelet.R
import com.celeritassolutions.hivelet.model.CategoriesModel

class QuestionnaireSummaryAdapter(val list: List<CategoriesModel>, val context: Context) :
    RecyclerView.Adapter<QuestionnaireSummaryAdapter.ViewHolder>() {

    private lateinit var txtOption: AppCompatTextView
    private lateinit var txtScore: AppCompatTextView

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            txtOption = itemView.findViewById(R.id.txt_options)
            txtScore = itemView.findViewById(R.id.txt_score)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.arrival_summery_item, parent, false)

        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        val item = list[position]
        txtOption.text = item.catName
        txtScore.text = item.catScore
    }
}