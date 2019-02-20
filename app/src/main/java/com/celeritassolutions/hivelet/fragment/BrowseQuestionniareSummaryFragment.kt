package com.celeritassolutions.hivelet.fragment

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.celeritassolutions.hivelet.R
import com.celeritassolutions.hivelet.adapter.BrowseQuestionnaireSummaryAdapter
import com.celeritassolutions.hivelet.database.DatabaseHelper
import com.celeritassolutions.hivelet.model.CategoriesModel
import com.celeritassolutions.hivelet.utils.RecyclerItemClickListener
import com.celeritassolutions.hivelet.utils.SP
import com.celeritassolutions.hivelet.utils.Utils
import kotlinx.android.synthetic.main.activity_comments.view.*
import kotlinx.android.synthetic.main.employee_warning_dialog.view.*

class BrowseQuestionniareSummaryFragment : Fragment() {
    private var categoriesList: List<CategoriesModel> = ArrayList()
    //private var categoriesReviewList: List<CatReviewModel> = ArrayList()
    private var arrivalSummaryRecycler: RecyclerView? = null
    private lateinit var btnSubmit: AppCompatButton
    private lateinit var btnSave: AppCompatButton
    private var btnEditToolbar: AppCompatImageView? = null
    private var btnHomeToolbar: AppCompatImageView? = null
    private var btnBackToolbar: AppCompatImageView? = null
    private var titleToolbar: AppCompatTextView? = null
    private lateinit var txtScore: AppCompatTextView
    private lateinit var btnEmail: AppCompatImageView
    private lateinit var ratingBar: RatingBar
    private lateinit var scoreProgress: ContentLoadingProgressBar
    private var dbHelper: DatabaseHelper? = null
    private var htmlString: String? = null
    private var extraTrianer: String = ""
    private var extraID: String = ""
    private var extraTitle: String = ""
    private var pExtraTitle: String = ""
    private var pExtraID: String = ""
    private var responseID: String = ""
    private var utils: Utils = Utils()
    private var summaryScore = 0
    private lateinit var getSharedPrefs: SP
    private var positiveResponses: Int = 0
    private var negativeResponses: Int = 0
    private var responsesSum: Int = 0
    private var TotalScore: Double = 0.0
    var totalCourtesyScore: Double = 0.0
    var totalAmbianceScore: Double = 0.0
    var totalCleanScore: Double = 0.0
    var totalEffeciencyScore: Double = 0.0
    var totalElementsScore: Double = 0.0
    var totalFoodScore: Double = 0.0
    var totalGraciousScore: Double = 0.0
    var totalGuestScore: Double = 0.0
    var totalClassificationScore: Double = 0.0
    var totalServiceScore: Double = 0.0
    var totalStaffScore: Double = 0.0
    var totalTechnicalScore: Double = 0.0
    private lateinit var pastResponseID: String
    private var pos:Int = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootview = inflater.inflate(R.layout.activity_summary_arrival_services, container, false)
        arrivalSummaryRecycler = rootview.findViewById(R.id.arrival_summary_recycler)
        btnSubmit = rootview.findViewById(R.id.btn_submit)
        btnSave = rootview.findViewById(R.id.btn_save)
        getSharedPrefs = SP(context!!.applicationContext)
        btnHomeToolbar = rootview.findViewById(R.id.home_toolbar)
        btnEditToolbar = rootview.findViewById(R.id.edit_toolbar)
        btnBackToolbar = rootview.findViewById(R.id.btn_back_toolbar)
        titleToolbar = rootview.findViewById(R.id.title_toolbar)
        btnEmail = rootview.findViewById(R.id.btn_email)
        txtScore = rootview.findViewById(R.id.ScorePercent)
        scoreProgress = rootview.findViewById(R.id.scoreProgress)
        ratingBar = rootview.findViewById(R.id.rating_bar)
        responseID = getSharedPrefs.getdata("surveyID")
        pastResponseID = getSharedPrefs.getdata("pastAssessmentResponse")
        dbHelper = DatabaseHelper(this.context!!)

        initCalculations()

       /* if (arguments != null) {
            extraTitle = arguments?.getString("title")!!
            extraID = arguments?.getString("id")!!.toString()
            extraTrianer = arguments?.getString("trainer")!!.toString()
            pExtraID = arguments?.getString("pExtraID")!!.toString()
            pExtraTitle = arguments?.getString("pExtraTitle")!!.toString()
            titleToolbar?.text = getSharedPrefs.getdata("pastAssessmentResponse")
        }*/

        positiveResponses = dbHelper?.countPositiveResponse(responseID)!!
        negativeResponses = dbHelper?.countNegativeResponse(responseID)!!
        responsesSum = positiveResponses + negativeResponses
        if (!responsesSum.equals(0)) {
            TotalScore = ((positiveResponses * 100) / responsesSum).toDouble()
            val s = "$TotalScore"
            val parts = s.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray() // escape .
            val part1 = parts[0]
            txtScore.text = "$part1%"
            summaryScore = part1.toInt()
            scoreProgress.scaleY = 3f
            scoreProgress.progress = part1.toInt()
            getSharedPrefs.savedata("totalScore", "$TotalScore")
        }



        initializeRatingBar()

        arrivalSummaryRecycler?.layoutManager = LinearLayoutManager(this.context)
        arrivalSummaryRecycler?.adapter = BrowseQuestionnaireSummaryAdapter(categoriesList, this.context!!)
        arrivalSummaryRecycler?.addOnItemTouchListener(
            RecyclerItemClickListener(
                context!!,
                arrivalSummaryRecycler!!,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        pos = position
                        getSharedPrefs.savedata("catTitle", categoriesList[position].catName)
                        addFragment(BrowseCategoryDetailFragment(),true,"Category Details")
                    }

                    override fun onLongItemClick(view: View?, position: Int) {

                    }

                })
        )
        btnSubmit.setOnClickListener {

           /* dbHelper!!.insertCatResponse(
                responseID,
                totalAmbianceScore.toString(),
                totalCleanScore.toString(),
                totalCourtesyScore.toString(),
                totalEffeciencyScore.toString(),
                totalElementsScore.toString(),
                totalFoodScore.toString(),
                totalGraciousScore.toString(),
                totalGuestScore.toString(),
                totalClassificationScore.toString(),
                totalServiceScore.toString(),
                totalStaffScore.toString(),
                totalTechnicalScore.toString(),
                summaryScore.toString()
            )*/

            Log.d("#responseID",responseID)
            Log.d("#totalAmbianceScore",totalAmbianceScore.toString())
            Log.d("#totalCleanScore",totalCleanScore.toString())
            Log.d("#totalCourtesyScore",totalCourtesyScore.toString())
            Log.d("#totalEffeciencyScore",totalEffeciencyScore.toString())
            Log.d("#totalElementsScore",totalElementsScore.toString())
            Log.d("#totalFoodScore",totalFoodScore.toString())
            Log.d("#totalGraciousScore", totalGraciousScore.toString())
            Log.d("#totalGuestScore",totalGuestScore.toString())
            Log.d("#totalClassific",totalClassificationScore.toString())
            Log.d("#totalServiceScore",totalServiceScore.toString())
            Log.d("#totalStaffScore",totalStaffScore.toString())
            Log.d("#totalTechnicalScore",totalTechnicalScore.toString())

            getSharedPrefs.savedata("surveyStatus", "Submitted")
            showDialog()
        }
        btnSave.setOnClickListener {
            getSharedPrefs.savedata("surveyStatus", "Saved")
            showDialog()
        }
        btnEditToolbar?.setOnClickListener {
            showCommentDialog()
        }
        btnHomeToolbar?.setOnClickListener {
            addFragment(DashboardFragment(), false, "Dashboard")
        }
        btnBackToolbar?.setOnClickListener {
            fragmentManager?.popBackStackImmediate()
        }

        htmlString = Html.fromHtml(
            SpannableStringBuilder()
                .append(
                    "<p>Trainer Name: $extraTrianer <br/> Date: ${utils.getCurrentDate()}<br/> Day: ${utils.getDay()}<br/> Time: ${utils.getTime()}<br/>Summary: $pExtraTitle<br/> Room no: - <br/> Meal Duration: -</p>"
                )
                .append("<html><br/><br/>EMPLOYEES ASSESSED:<br/><table><tr><td>\n</td></tr></table></html>")
                .append("<html><p>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp SCORE &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp 5 STAR/DIAMOND</p></html>")
                .toString()
        ).toString()

        /* htmlString = Html.fromHtml(SpannableStringBuilder()
             .append("<p>Trainer Name: $extraTrianer<p>")
             .append("<p>Date: ${utils.getCurrentDate()}<p>")
             .append("<p>Day: ${utils.getDay()}<p>")
             .append("<p>Time: ${utils.getTime()}<p>")
             .append("<p>Summary: $pExtraTitle<p>")
             .append("<p>Room no: -<p>")
             .append("<p>Meal Duration: -<p>")
             .append("<br><p>EMPLOYEES ASSESSED:<p>")
             .append("<p><td>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp</th>SCORE<td>&nbsp&nbsp&nbsp&nbsp&nbsp</th><td>&nbsp&nbsp&nbsp&nbsp&nbsp</th><td>&nbsp&nbsp&nbsp&nbsp&nbsp</th><td>&nbsp&nbsp&nbsp&nbsp&nbsp</th><td>&nbsp&nbsp&nbsp&nbsp&nbsp</th><td>&nbsp&nbsp&nbsp&nbsp&nbsp</th>5 STAR/DIAMOND</p>")
             .append("<br><p>        0                               NO</p>")
             .toString()
         ).toString()*/

        btnEmail.setOnClickListener {
            composeEmail()
        }

        return rootview
    }

    private fun initCalculations() {
        categoriesList = dbHelper!!.browseCatScores(pastResponseID)
        Log.d("#ResponseID", pastResponseID)
    }

    private fun initializeRatingBar() {
        if (TotalScore <= 20) {
            ratingBar.rating = 1f
        }

        if (TotalScore > 20 && TotalScore <= 40) {
            ratingBar.rating = 2f
        }

        if (TotalScore > 40 && TotalScore <= 60) {
            ratingBar.rating = 3f

        }

        if (TotalScore > 60 && TotalScore <= 80) {
            ratingBar.rating = 4f
        }

        if (TotalScore > 80) {
            ratingBar.rating = 5f
        }

    }

    /*fun composeEmail(addresses: Array<String>) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:") // only email apps should handle this
            putExtra(Intent.EXTRA_EMAIL, addresses)
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }*/

    fun composeEmail() {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:") // only email apps should handle this
            putExtra(Intent.EXTRA_SUBJECT, "$pExtraTitle - $extraTitle")
            putExtra(Intent.EXTRA_TEXT, htmlString)
        }
        if (intent.resolveActivity(context!!.packageManager) != null) {
            startActivity(intent)
        }
    }

    private fun showCommentDialog() {
        // Inflates the dialog with custom view
        val dialogView = LayoutInflater.from(activity!!.applicationContext).inflate(R.layout.activity_comments, null)
        val builder = AlertDialog.Builder(context!!)
            .setView(dialogView)

        val dialog = builder.show()
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        //dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialogView.btn_close.setOnClickListener {
            dialog.dismiss()
        }

        dialogView.btn_save.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun showDialog() {
        val dialogView =
            LayoutInflater.from(activity!!.applicationContext).inflate(R.layout.employee_warning_dialog, null)
        val builder = AlertDialog.Builder(context!!)
            .setView(dialogView)

        val dialog = builder.show()
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogView.btn_ok.setOnClickListener {
            addFragment(EmployeeSetupFragment(), true, "Employee Setup")
            dialog.dismiss()
        }
        dialogView.btn_cancel.setOnClickListener {
            dialog.dismiss()
        }
    }

    fun addFragment(fragment: Fragment, addToBackStack: Boolean, tag: String) {
        val manager = activity?.supportFragmentManager
        val ft = manager!!.beginTransaction()

        if (addToBackStack) {
            ft.addToBackStack(tag)
        }
        arguments?.putString("browseCat",categoriesList[pos].catName)
        ft.replace(R.id.fragment_container, fragment, tag)
        ft.commitAllowingStateLoss()
    }
}

