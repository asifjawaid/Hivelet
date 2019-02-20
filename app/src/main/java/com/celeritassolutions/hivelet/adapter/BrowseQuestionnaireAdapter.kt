package com.celeritassolutions.hivelet.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.celeritassolutions.hivelet.R
import com.celeritassolutions.hivelet.database.DatabaseHelper
import com.celeritassolutions.hivelet.model.BrowseQuestionnaireModel
import com.celeritassolutions.hivelet.utils.SP
import kotlinx.android.synthetic.main.arrival_services_item.view.*


class BrowseQuestionnaireAdapter(val list: List<BrowseQuestionnaireModel>, val context: Context) :
    RecyclerView.Adapter<BrowseQuestionnaireAdapter.ViewHolder>() {

    private lateinit var txtQuestionnire: AppCompatTextView
    private lateinit var btnComment: AppCompatImageView
    private lateinit var btnPhoto: AppCompatImageView
    private var dbHelper: DatabaseHelper = DatabaseHelper(context)
    private var getSharedPrefs = SP(context)
    private lateinit var listResponse: List<BrowseQuestionnaireModel>
    private var answer = ""

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            txtQuestionnire = itemView.findViewById(R.id.txt_questionnire)
            btnComment = itemView.findViewById(R.id.btn_comment)
            btnPhoto = itemView.findViewById(R.id.btn_photo)
            listResponse = dbHelper.browseQuestionnaire(getSharedPrefs.getdata("pastAssessmentResponse"))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.arrival_services_item, parent, false)

        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemId(position: Int) = position.toLong()
    override fun getItemViewType(position: Int) = position

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        holder.itemView.txt_questionnire.text = "${position + 1}. ${item.response_question}"
        holder.itemView.btn_na.isEnabled = false
        holder.itemView.btn_yes.isEnabled = false
        holder.itemView.btn_no.isEnabled = false

        answer = listResponse[position].response_answer

        if (answer == "NA" && listResponse[position].survey_comment == "NA"){

            holder.itemView.btn_na.setBackgroundResource(R.drawable.selected_left_browse_btn)
            holder.itemView.btn_na.setTextColor(context.resources.getColor(R.color.white))

            holder.itemView.btn_no.setBackgroundResource(R.drawable.right_browse_btn_bg)
            holder.itemView.btn_no.setTextColor(context.resources.getColor(R.color.txt_browse))

            holder.itemView.btn_yes.setBackgroundResource(R.drawable.center_browse_btn)
            holder.itemView.btn_yes.setTextColor(context.resources.getColor(R.color.txt_browse))



        }
        if( answer == "NO"){

            holder.itemView.btn_na.setBackgroundResource(R.drawable.left_browse_btn)
            holder.itemView.btn_na.setTextColor(context.resources.getColor(R.color.txt_browse))

            holder.itemView.btn_no.setBackgroundResource(R.drawable.selected_right_browse_btn)
            holder.itemView.btn_no.setTextColor(context.resources.getColor(R.color.white))

            holder.itemView.btn_yes.setBackgroundResource(R.drawable.center_browse_btn)
            holder.itemView.btn_yes.setTextColor(context.resources.getColor(R.color.txt_browse))

        }

        if(answer == "YES"){

            holder.itemView.btn_na.setBackgroundResource(R.drawable.left_browse_btn)
            holder.itemView.btn_na.setTextColor(context.resources.getColor(R.color.txt_browse))

            holder.itemView.btn_no.setBackgroundResource(R.drawable.right_browse_btn_bg)
            holder.itemView.btn_no.setTextColor(context.resources.getColor(R.color.txt_browse))

            holder.itemView.btn_yes.setBackgroundResource(R.drawable.selected_center_browse_btn)
            holder.itemView.btn_yes.setTextColor(context.resources.getColor(R.color.white))

        }
    }
}
