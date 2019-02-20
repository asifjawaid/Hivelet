package com.celeritassolutions.hivelet.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.celeritassolutions.hivelet.R
import com.celeritassolutions.hivelet.model.BrowseQuestionnaireModel
import com.celeritassolutions.hivelet.model.DepartmentsModel

class positiveCatAdapter(val list: List<BrowseQuestionnaireModel>, val context: Context) :
    RecyclerView.Adapter<positiveCatAdapter.ViewHolder>() {

    private lateinit var txtCat: AppCompatTextView


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {

            txtCat = itemView.findViewById(R.id.txt_cat)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): positiveCatAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_strengths, parent, false)

        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: positiveCatAdapter.ViewHolder, position: Int) {
        val item = list[position]
        txtCat.text = item.response_question
    }
}