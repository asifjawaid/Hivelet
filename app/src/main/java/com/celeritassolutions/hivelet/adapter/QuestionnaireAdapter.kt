package com.celeritassolutions.hivelet.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.celeritassolutions.hivelet.R
import com.celeritassolutions.hivelet.api.ApiInterface
import com.celeritassolutions.hivelet.database.DatabaseHelper
import com.celeritassolutions.hivelet.model.QuestionnireModel
import com.celeritassolutions.hivelet.utils.SP
import kotlinx.android.synthetic.main.arrival_services_item.view.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class QuestionnaireAdapter(val list: List<QuestionnireModel>, val context: Context, val btnlistener: BtnClickListener,val commentlistener: BtnCommentListener) :
    RecyclerView.Adapter<QuestionnaireAdapter.ViewHolder>() {

    companion object {
        var mClickListener: BtnClickListener? = null
        var mCommentListener: BtnCommentListener? = null
    }

    private lateinit var txtQuestionnire: AppCompatTextView
    private lateinit var btnComment: AppCompatImageView
    private lateinit var btnPhoto: AppCompatImageView
    private var dbHelper: DatabaseHelper = DatabaseHelper(context)
    private var getSharedPrefs = SP(context)
    private var previousPostiton: Int = 0

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            txtQuestionnire = itemView.findViewById(R.id.txt_questionnire)
            btnComment = itemView.findViewById(R.id.btn_comment)
            btnPhoto = itemView.findViewById(R.id.btn_photo)
            previousPostiton = Integer.parseInt(getSharedPrefs.getdata("lastPosition"))
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
        mClickListener = btnlistener
        mCommentListener = commentlistener
        holder.itemView.txt_questionnire.text = "${position + 1}. ${item.question}"
        holder.itemView.btn_na.setOnClickListener {
            notifyDataSetChanged()
            if (item.isSelectedNA == "NA" || item.isSelectedNA == "false") {
                holder.itemView.btn_na.setBackgroundResource(R.drawable.selected_left_arrival_btn_bg)
                holder.itemView.btn_na.setTextColor(context.resources.getColor(R.color.white))

                //btn_no
                holder.itemView.btn_no.setBackgroundResource(R.drawable.right_arrival_btn_bg)
                holder.itemView.btn_no.setTextColor(context.resources.getColor(R.color.orange))

                //btn_yes
                holder.itemView.btn_yes.setBackgroundResource(R.drawable.center_arrival_btn_bg)
                holder.itemView.btn_yes.setTextColor(context.resources.getColor(R.color.orange))

                item.isSelectedNA = "true"
                item.isSelectedNo == "false"
                item.isSelectedYes == "false"
                dbHelper.updateSurveyResponse(
                    (previousPostiton + (position + 1)).toString(),
                    getSharedPrefs.getdata("deptTitle"),
                    "NA",
                    "NA",
                    item.isPhotoAdded,
                    getSharedPrefs.getdata("currentDate")
                )
                //postResponseData(position+1)

            } else {
                holder.itemView.btn_na.setBackgroundResource(R.drawable.left_arrival_btn_bg)
                holder.itemView.btn_na.setTextColor(context.resources.getColor(R.color.orange))

                //btn_no
                holder.itemView.btn_no.setBackgroundResource(R.drawable.right_arrival_btn_bg)
                holder.itemView.btn_no.setTextColor(context.resources.getColor(R.color.orange))

                //btn_yes
                holder.itemView.btn_yes.setBackgroundResource(R.drawable.center_arrival_btn_bg)
                holder.itemView.btn_yes.setTextColor(context.resources.getColor(R.color.orange))

                item.isSelectedNA = "false"
                item.isSelectedNo = "true"
                item.isSelectedYes = "true"
            }
        }

        holder.itemView.btn_no.setOnClickListener {
            notifyDataSetChanged()
            if (item.isSelectedNo == "NA" || item.isSelectedNo == "false") {
                holder.itemView.btn_na.setBackgroundResource(R.drawable.left_arrival_btn_bg)
                holder.itemView.btn_na.setTextColor(context.resources.getColor(R.color.orange))

                //btn_no
                holder.itemView.btn_no.setBackgroundResource(R.drawable.selected_right_arrival_btn)
                holder.itemView.btn_no.setTextColor(context.resources.getColor(R.color.white))

                //btn_yes
                holder.itemView.btn_yes.setBackgroundResource(R.drawable.center_arrival_btn_bg)
                holder.itemView.btn_yes.setTextColor(context.resources.getColor(R.color.orange))


                item.isSelectedNA = "false"
                item.isSelectedYes == "false"
                item.isSelectedNo = "true"
                dbHelper.updateSurveyResponse(
                    (previousPostiton + (position + 1)).toString(),
                    getSharedPrefs.getdata("deptTitle"),
                    "NO",
                    "NA",
                    item.isPhotoAdded,
                    getSharedPrefs.getdata("currentDate")
                )

            } else {
                holder.itemView.btn_na.setBackgroundResource(R.drawable.left_arrival_btn_bg)
                holder.itemView.btn_na.setTextColor(context.resources.getColor(R.color.orange))

                //btn_no
                holder.itemView.btn_no.setBackgroundResource(R.drawable.right_arrival_btn_bg)
                holder.itemView.btn_no.setTextColor(context.resources.getColor(R.color.orange))

                //btn_yes
                holder.itemView.btn_yes.setBackgroundResource(R.drawable.center_arrival_btn_bg)
                holder.itemView.btn_yes.setTextColor(context.resources.getColor(R.color.orange))

                item.isSelectedNA = "true"
                item.isSelectedNo = "false"
                item.isSelectedYes = "true"
            }
        }

        holder.itemView.btn_yes.setOnClickListener {
            notifyDataSetChanged()
            if (item.isSelectedYes == "NA" || item.isSelectedYes == "false") {
                holder.itemView.btn_na.setBackgroundResource(R.drawable.left_arrival_btn_bg)
                holder.itemView.btn_na.setTextColor(context.resources.getColor(R.color.orange))

                //btn_no
                holder.itemView.btn_no.setBackgroundResource(R.drawable.right_arrival_btn_bg)
                holder.itemView.btn_no.setTextColor(context.resources.getColor(R.color.orange))

                //btn_yes
                holder.itemView.btn_yes.setBackgroundResource(R.drawable.selected_center_arrival_btn_bg)
                holder.itemView.btn_yes.setTextColor(context.resources.getColor(R.color.white))


                item.isSelectedNA = "false"
                item.isSelectedYes == "true"
                item.isSelectedNo = "false"

                dbHelper.updateSurveyResponse(
                    (previousPostiton + (position + 1)).toString(),
                    getSharedPrefs.getdata("deptTitle"),
                    "YES",
                    "NA",
                    item.isPhotoAdded,
                    getSharedPrefs.getdata("currentDate")
                )

            } else {
                holder.itemView.btn_na.setBackgroundResource(R.drawable.left_arrival_btn_bg)
                holder.itemView.btn_na.setTextColor(context.resources.getColor(R.color.orange))

                //btn_no
                holder.itemView.btn_no.setBackgroundResource(R.drawable.right_arrival_btn_bg)
                holder.itemView.btn_no.setTextColor(context.resources.getColor(R.color.orange))

                //btn_yes
                holder.itemView.btn_yes.setBackgroundResource(R.drawable.center_arrival_btn_bg)
                holder.itemView.btn_yes.setTextColor(context.resources.getColor(R.color.orange))

                item.isSelectedNA = "true"
                item.isSelectedNo = "true"
                item.isSelectedYes = "flase"
            }
        }

        holder.itemView.btn_photo.setOnClickListener {
            if (mClickListener != null){
                mClickListener?.onBtnClick(position)

            }

        }

        holder.itemView.btn_comment.setOnClickListener {
            if (mClickListener != null){
                mCommentListener?.onCommentClick(position)

            }
            holder.itemView.btn_photo.setBackgroundResource(R.drawable.img_photo_true)
        }
        }


    interface BtnClickListener {
        fun onBtnClick(position: Int)
    }

    interface BtnCommentListener {
        fun onCommentClick(position: Int)
    }

    /*private fun postResponseData(pos: Int) {
        var apiService = ApiClient.client?.create(ApiInterface::class.java)
        val listcall = apiService?.responseDemoRC(
            "#" +
                    "${list[pos].assessmentID}", "1", "YES", "Reservations Service", "11-Feb-2019 12:29 PM"
            , "Cars in queue are acknowledged and appropriately handled on arrival ", ".", "deptID", "1", "",
            "Hivelet"
        )
        listcall?.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Log.d("Data Posted", "${response.code()}")

                } else {
                    Log.d("#Response", "${response.code()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("TAG", "" + t.printStackTrace())
            }

        })
    }*/
}
