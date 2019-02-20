package com.celeritassolutions.hivelet.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.celeritassolutions.hivelet.R
import com.celeritassolutions.hivelet.model.ResponsesModel
import com.celeritassolutions.hivelet.utils.Utils

class AssessmentAdapter(val list: List<ResponsesModel>, val context: Context) :
    RecyclerView.Adapter<AssessmentAdapter.ViewHolder>() {

    private lateinit var txtAssessment: AppCompatTextView
    private lateinit var txtDate: AppCompatTextView
    private lateinit var txtTrainer: AppCompatTextView
    private lateinit var txtScore: AppCompatTextView
    private lateinit var txtStatus: AppCompatTextView
    private lateinit var nextImg: AppCompatImageView
    private var utils = Utils()


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            txtAssessment = itemView.findViewById(R.id.txt_assessment)
            txtDate = itemView.findViewById(R.id.txt_date)
            txtTrainer = itemView.findViewById(R.id.txt_trainer)
            txtScore = itemView.findViewById(R.id.txt_score)
            txtStatus = itemView.findViewById(R.id.txt_status)
            nextImg = itemView.findViewById(R.id.img_explore)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssessmentAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.office_front_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: AssessmentAdapter.ViewHolder, position: Int) {
        val item = list[position]
        txtAssessment.text = item.respose_id
        txtDate.text = utils.parseDateToddMMyyyy(item.survey_date)
        txtTrainer.text = item.survey_trainer
        txtScore.text = item.total_dept_score
        txtStatus.text = item.survey_status
    }

}