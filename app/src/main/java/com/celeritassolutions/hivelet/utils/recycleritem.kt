package com.celeritassolutions.hivelet.utils

import android.content.Context
import android.os.Build
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

open class RecyclerItemClickListener(
    context: Context,
    recyclerView: RecyclerView,
    private val mListener: OnItemClickListener?
) : RecyclerView.OnItemTouchListener {

    internal var mGestureDetector: GestureDetector

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)

        fun onLongItemClick(view: View?, position: Int)
    }

    init {
        mGestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                return true
            }

            override fun onLongPress(e: MotionEvent) {
                val child = recyclerView.findChildViewUnder(e.x, e.y)
                if (child != null && mListener != null) {
                    mListener.onLongItemClick(child, recyclerView.getChildAdapterPosition(child))
                }
            }
        })
    }

    override fun onInterceptTouchEvent(view: RecyclerView, e: MotionEvent): Boolean {
        val childView = view.findChildViewUnder(e.x, e.y)
        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            mListener.onItemClick(childView, view.getChildAdapterPosition(childView))
            return true
        }
        return false
    }

    override fun onTouchEvent(view: RecyclerView, motionEvent: MotionEvent) {}

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}

}

class Utils{
    fun getCurrentDate(): String{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            var answer: String =  current.format(formatter)
            answer
        } else {
            var date = Date();
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            val answer: String = formatter.format(date)
            answer
        }
    }

    fun getStartOfMonth():String{
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")

        var c = Calendar.getInstance()
        c.set(Calendar.DAY_OF_MONTH, 1)
        val monthStart = dateFormat.format(c.time)

        c = Calendar.getInstance() // reset
        dateFormat.format(c.time)


        return monthStart
    }

    fun postCurrentDate(): String{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy hh:mm a")
            var answer: String =  current.format(formatter)
            answer
        } else {
            var date = Date();
            val formatter = SimpleDateFormat("dd-MMM-yyyy hh:mm a")
            val answer: String = formatter.format(date)
            answer
        }
    }

    fun postStartOfMonth():String{
        val dateFormat = SimpleDateFormat("dd-MMM-yyyy hh:mm a")

        var c = Calendar.getInstance()
        c.set(Calendar.DAY_OF_MONTH, 1)
        val monthStart = dateFormat.format(c.time)

        c = Calendar.getInstance() // reset
        dateFormat.format(c.time)


        return monthStart
    }

    fun getDay(): String {
        val sdf = SimpleDateFormat("EEEE")
        val d = Date()
        return sdf.format(d)
    }

    fun getYear(): String{
        return Calendar.getInstance().get(Calendar.YEAR).toString()
    }

    fun getTime(): String{
        val sdf = SimpleDateFormat("h:mm a")
        return sdf.format(Date())
    }

    fun generateSurveyID(): String{
        val dNow = Date()
        val ft = SimpleDateFormat("MMddyyhhmmss")
        return ft.format(dNow)
    }

    fun parseDateToddMMyyyy(time: String): String? {
        val inputPattern = "yyyy-MM-dd"
        val outputPattern = "dd-MMM-yyyy"
        val inputFormat = SimpleDateFormat(inputPattern)
        val outputFormat = SimpleDateFormat(outputPattern)

        var date: Date? = null
        var str: String? = null

        try {
            date = inputFormat.parse(time)
            str = outputFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return str
    }
}

