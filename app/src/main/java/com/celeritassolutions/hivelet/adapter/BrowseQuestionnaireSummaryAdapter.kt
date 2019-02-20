package com.celeritassolutions.hivelet.adapter

import android.content.Context
import android.os.Bundle


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.celeritassolutions.hivelet.R
import com.celeritassolutions.hivelet.model.CatReviewModel
import com.celeritassolutions.hivelet.model.CategoriesModel
import java.text.DecimalFormat

class BrowseQuestionnaireSummaryAdapter(val item:List<CategoriesModel>, val context: Context) :
    RecyclerView.Adapter<BrowseQuestionnaireSummaryAdapter.ViewHolder>() {

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
        return item.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        /*val catReviewItem = list[position]
        val catItem = item[position]

        txtOption.text = catItem.deptName
        if (position == 0){
            txtScore.text = catReviewItem.CAT_AMBIANCE
        }
        if (position == 1){
            txtScore.text = catReviewItem.CAT_CLEANLINESS
        }
        if (position == 2){
            txtScore.text = catReviewItem.CAT_COURTESY
        }
        if (position == 3){
            txtScore.text = catReviewItem.CAT_EFFECIENCY
        }
        if (position == 4){
            txtScore.text = catReviewItem.CAT_ELEMENT
        }
        if (position == 5){
            txtScore.text = catReviewItem.CAT_FOOD
        }
        if (position == 6){
            txtScore.text = catReviewItem.CAT_GRACIOUS
        }
        if (position == 7){
            txtScore.text = catReviewItem.CAT_COMFORT
        }
        if (position == 8){
            txtScore.text = catReviewItem.CAT_CLASSIFICATION
        }
        if (position == 9){
            txtScore.text = catReviewItem.CAT_SERVICE
        }
        if (position == 10){
            txtScore.text = catReviewItem.CAT_STAFF
        }
        if (position == 11){
            txtScore.text = catReviewItem.CAT_TECHNICAL
        }
*/
        txtOption.text = item[position].catName
        txtScore.text = "${ DecimalFormat("##").format(item[position].catScore.toDouble())}%"

    }
}