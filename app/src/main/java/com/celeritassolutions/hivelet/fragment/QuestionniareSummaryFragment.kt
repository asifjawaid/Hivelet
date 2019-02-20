package com.celeritassolutions.hivelet.fragment

import ApiClient
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
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.celeritassolutions.hivelet.R
import com.celeritassolutions.hivelet.adapter.QuestionnaireSummaryAdapter
import com.celeritassolutions.hivelet.api.ApiInterface
import com.celeritassolutions.hivelet.database.DatabaseHelper
import com.celeritassolutions.hivelet.model.CatReviewModel
import com.celeritassolutions.hivelet.model.CategoriesModel
import com.celeritassolutions.hivelet.model.QuestionnireModel
import com.celeritassolutions.hivelet.utils.RecyclerItemClickListener
import com.celeritassolutions.hivelet.utils.SP
import com.celeritassolutions.hivelet.utils.Utils
import kotlinx.android.synthetic.main.activity_comments.view.*
import kotlinx.android.synthetic.main.employee_warning_dialog.view.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat

class QuestionniareSummaryFragment : Fragment() {
    private var categoriesList: List<CategoriesModel> = ArrayList()
    private var selectResponsesToPost: List<QuestionnireModel> = ArrayList()
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
    private var isEmployeeAdded = ""
    var mAPIService: ApiInterface? = null
    private var pos: Int = 0
    private var totalScore: Double = 0.0
    private var deptID = ""


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
        isEmployeeAdded = getSharedPrefs.getdata("employeeSelected")
        dbHelper = DatabaseHelper(this.context!!)

        initCalculations()
        if (isEmployeeAdded == "true") {
            for (i in categoriesList.indices) {
                dbHelper!!.insertCatScore(responseID, categoriesList[i].catName, "0")
                Log.d("CATSCORE", categoriesList[i].catName)
            }
        }

        if (arguments != null) {
            extraTitle = arguments?.getString("title")!!
            extraID = arguments?.getString("id")!!.toString()
            extraTrianer = arguments?.getString("trainer")!!.toString()
            pExtraID = arguments?.getString("pExtraID")!!.toString()
            pExtraTitle = arguments?.getString("pExtraTitle")!!.toString()
            titleToolbar?.text = extraTitle
        }

        deptID = getSharedPrefs.getdata("deptID")

        positiveResponses = dbHelper?.countPositiveResponse(responseID)!!
        negativeResponses = dbHelper?.countNegativeResponse(responseID)!!
        responsesSum = positiveResponses + negativeResponses
        if (!responsesSum.equals(0)) {
            TotalScore = ((positiveResponses * 100) / responsesSum).toDouble()
            val s = "$TotalScore"
            val parts = s.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray() // escape .
            val part1 = parts[0]
            val part2 = parts[1]
            txtScore.text = "$part1%"
            summaryScore = part1.toInt()
            scoreProgress.scaleY = 3f
            scoreProgress.progress = part1.toInt()
            getSharedPrefs.savedata("totalScore", "$TotalScore")
        }



        initializeRatingBar()

        arrivalSummaryRecycler?.layoutManager = LinearLayoutManager(this.context)
        arrivalSummaryRecycler?.adapter = QuestionnaireSummaryAdapter(categoriesList, this.context!!)
        arrivalSummaryRecycler?.addOnItemTouchListener(
            RecyclerItemClickListener(
                context!!,
                arrivalSummaryRecycler!!,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        pos = position
                        getSharedPrefs.savedata("catTitle", categoriesList[position].catName)
                        addFragment(CategoryDetailFragment(),true,"Category Details")
                    }

                    override fun onLongItemClick(view: View?, position: Int) {

                    }

                })
        )


        btnSubmit.setOnClickListener {
            if (isEmployeeAdded == "true") {
                selectResponsesToPost = dbHelper!!.selectResponsesToPost(responseID)
                dbHelper!!.updateCatResponse(responseID, categoriesList[0].catName, totalAmbianceScore.toString())
                dbHelper!!.updateCatResponse(responseID, categoriesList[1].catName, totalCleanScore.toString())
                dbHelper!!.updateCatResponse(responseID, categoriesList[2].catName, totalCourtesyScore.toString())
                dbHelper!!.updateCatResponse(responseID, categoriesList[3].catName, totalEffeciencyScore.toString())
                dbHelper!!.updateCatResponse(responseID, categoriesList[4].catName, totalElementsScore.toString())
                dbHelper!!.updateCatResponse(responseID, categoriesList[5].catName, totalFoodScore.toString())
                dbHelper!!.updateCatResponse(responseID, categoriesList[6].catName, totalGraciousScore.toString())
                dbHelper!!.updateCatResponse(responseID, categoriesList[7].catName, totalGuestScore.toString())
                dbHelper!!.updateCatResponse(responseID, categoriesList[8].catName, totalClassificationScore.toString())
                dbHelper!!.updateCatResponse(responseID, categoriesList[9].catName, totalServiceScore.toString())
                dbHelper!!.updateCatResponse(responseID, categoriesList[10].catName, totalStaffScore.toString())
                dbHelper!!.updateCatResponse(responseID, categoriesList[11].catName, totalTechnicalScore.toString())


                Log.d("#selectResponsesToPost","${selectResponsesToPost.size}")
                val saveResponseID = getSharedPrefs.getdata("saveResponseID")
                val saveSurveyStatus = "Submitted"
                val saveDeptTitle = getSharedPrefs.getdata("saveDeptTitle")
                val saveCurrentDate = getSharedPrefs.getdata("saveCurrentDate")
                val saveTrainerName = getSharedPrefs.getdata("saveTrainerName")
                val saveSurveyTotal = getSharedPrefs.getdata("saveSurveyTotal")

                dbHelper!!.saveResponses(
                    saveResponseID,
                    saveDeptTitle,
                    saveCurrentDate,
                    saveTrainerName,
                    saveSurveyTotal,
                    saveSurveyStatus
                )
                val savecountMTD = dbHelper!!.countSubmissionScore(getSharedPrefs.getdata("deptTitle"))
                val savecountYTD = dbHelper!!.countYTDSubmissionScore(getSharedPrefs.getdata("deptTitle"))
                dbHelper!!.updateScores(saveDeptTitle, savecountMTD.toString(), savecountYTD.toString())

                for(i in selectResponsesToPost.indices){
                    postResponseData(selectResponsesToPost[i].q_id,selectResponsesToPost[i].classID,selectResponsesToPost[i].queResponse,selectResponsesToPost[i].assessmentName
                    ,selectResponsesToPost[i].question,selectResponsesToPost[i].isCommentAdded,deptID,selectResponsesToPost[i].isPhotoAdded)
                    Toast.makeText(context,"Post Successfull",Toast.LENGTH_SHORT).show()

                }
                postAssessmentData()
                postGeneralDemoRC()
                addFragment(DashboardFragment(), false, "Dashboard")
            } else {
                showDialog()
            }
        }

        btnSave.setOnClickListener {
            if (isEmployeeAdded == "true") {
                dbHelper!!.updateCatResponse(responseID, categoriesList[0].catName, totalAmbianceScore.toString())
                dbHelper!!.updateCatResponse(responseID, categoriesList[1].catName, totalCleanScore.toString())
                dbHelper!!.updateCatResponse(responseID, categoriesList[2].catName, totalCourtesyScore.toString())
                dbHelper!!.updateCatResponse(responseID, categoriesList[3].catName, totalEffeciencyScore.toString())
                dbHelper!!.updateCatResponse(responseID, categoriesList[4].catName, totalElementsScore.toString())
                dbHelper!!.updateCatResponse(responseID, categoriesList[5].catName, totalFoodScore.toString())
                dbHelper!!.updateCatResponse(responseID, categoriesList[6].catName, totalGraciousScore.toString())
                dbHelper!!.updateCatResponse(responseID, categoriesList[7].catName, totalGuestScore.toString())
                dbHelper!!.updateCatResponse(responseID, categoriesList[8].catName, totalClassificationScore.toString())
                dbHelper!!.updateCatResponse(responseID, categoriesList[9].catName, totalServiceScore.toString())
                dbHelper!!.updateCatResponse(responseID, categoriesList[10].catName, totalStaffScore.toString())
                dbHelper!!.updateCatResponse(responseID, categoriesList[11].catName, totalTechnicalScore.toString())


                val saveResponseID = getSharedPrefs.getdata("saveResponseID")
                val saveSurveyStatus = "Saved"
                val saveDeptTitle = getSharedPrefs.getdata("saveDeptTitle")
                val saveCurrentDate = getSharedPrefs.getdata("saveCurrentDate")
                //val saveCountYTD = getSharedPrefs.getdata("saveCountYTD")
                val saveTrainerName = getSharedPrefs.getdata("saveTrainerName")
                //val saveCountMTD = getSharedPrefs.getdata("saveCountMTD")
                val saveSurveyTotal = getSharedPrefs.getdata("saveSurveyTotal")
                dbHelper!!.saveResponses(saveResponseID, saveDeptTitle, saveCurrentDate, saveTrainerName, saveSurveyTotal, saveSurveyStatus)
                var savecountMTD = dbHelper!!.countSubmissionScore(getSharedPrefs.getdata("deptTitle"))
                var savecountYTD = dbHelper!!.countYTDSubmissionScore(getSharedPrefs.getdata("deptTitle"))
                dbHelper!!.updateScores(saveDeptTitle, savecountMTD.toString(), savecountYTD.toString())
                addFragment(DashboardFragment(), false, "Dashboard")
            } else {
                showDialog()
            }


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

        //COURTESY
        categoriesList = dbHelper!!.selectCategories()
        if (dbHelper!!.countResponsesByCategory(
                "${getSharedPrefs.getdata("surveyID")}",
                "1",
                "${getSharedPrefs.getdata("assessName")}",
                "${getSharedPrefs.getdata("deptTitle")}"
            ) != 0
        ) {
            val courtesyTotal: Double = dbHelper!!.countResponsesByCategory(
                "${getSharedPrefs.getdata("surveyID")}",
                "1",
                "${getSharedPrefs.getdata("assessName")}",
                "${getSharedPrefs.getdata("deptTitle")}"
            )
                .toDouble()
            val courtesyYes: Double = dbHelper!!.countYesResponsesByCategory(
                "${getSharedPrefs.getdata("surveyID")}",
                "1",
                "${getSharedPrefs.getdata("assessName")}",
                "${getSharedPrefs.getdata("deptTitle")}"
            )
                .toDouble()

            val catscore: Double = courtesyYes / courtesyTotal
            totalCourtesyScore = catscore * 100
            categoriesList[2].catScore = "${DecimalFormat("##").format(totalCourtesyScore)}%"
        } else {
            categoriesList[2].catScore = "0%"
        }

        //AMBIANCE
        if (dbHelper!!.countResponsesByCategory(
                "${getSharedPrefs.getdata("surveyID")}",
                "5",
                "${getSharedPrefs.getdata("assessName")}",
                "${getSharedPrefs.getdata("deptTitle")}"
            ) != 0
        ) {
            val courtesyTotal: Double = dbHelper!!.countResponsesByCategory(
                "${getSharedPrefs.getdata("surveyID")}",
                "5",
                "${getSharedPrefs.getdata("assessName")}",
                "${getSharedPrefs.getdata("deptTitle")}"
            )
                .toDouble()
            val courtesyYes: Double = dbHelper!!.countYesResponsesByCategory(
                "${getSharedPrefs.getdata("surveyID")}",
                "5",
                "${getSharedPrefs.getdata("assessName")}",
                "${getSharedPrefs.getdata("deptTitle")}"
            )
                .toDouble()

            val catscore: Double = courtesyYes / courtesyTotal
            totalAmbianceScore = catscore * 100
            categoriesList[0].catScore = "${DecimalFormat("##").format(totalAmbianceScore)}%"
        } else {
            categoriesList[0].catScore = "0%"
        }

        //CLEANLINESS
        if (dbHelper!!.countResponsesByCategory(
                "${getSharedPrefs.getdata("surveyID")}",
                "6",
                "${getSharedPrefs.getdata("assessName")}",
                "${getSharedPrefs.getdata("deptTitle")}"
            ) != 0
        ) {
            val courtesyTotal: Double = dbHelper!!.countResponsesByCategory(
                "${getSharedPrefs.getdata("surveyID")}",
                "6",
                "${getSharedPrefs.getdata("assessName")}",
                "${getSharedPrefs.getdata("deptTitle")}"
            )
                .toDouble()
            val courtesyYes: Double = dbHelper!!.countYesResponsesByCategory(
                "${getSharedPrefs.getdata("surveyID")}",
                "6",
                "${getSharedPrefs.getdata("assessName")}",
                "${getSharedPrefs.getdata("deptTitle")}"
            )
                .toDouble()

            val catscore: Double = courtesyYes / courtesyTotal
            totalCleanScore = catscore * 100
            categoriesList[1].catScore = "${DecimalFormat("##").format(totalCleanScore)}%"
        } else {
            categoriesList[1].catScore = "0%"
        }

        //EFFECIENCY
        if (dbHelper!!.countResponsesByCategory(
                "${getSharedPrefs.getdata("surveyID")}",
                "8",
                "${getSharedPrefs.getdata("assessName")}",
                "${getSharedPrefs.getdata("deptTitle")}"
            ) != 0
        ) {
            val courtesyTotal: Double = dbHelper!!.countResponsesByCategory(
                "${getSharedPrefs.getdata("surveyID")}",
                "8",
                "${getSharedPrefs.getdata("assessName")}",
                "${getSharedPrefs.getdata("deptTitle")}"
            )
                .toDouble()
            val courtesyYes: Double = dbHelper!!.countYesResponsesByCategory(
                "${getSharedPrefs.getdata("surveyID")}",
                "8",
                "${getSharedPrefs.getdata("assessName")}",
                "${getSharedPrefs.getdata("deptTitle")}"
            )
                .toDouble()

            val catscore: Double = courtesyYes / courtesyTotal
            totalEffeciencyScore = catscore * 100
            categoriesList[3].catScore = "${DecimalFormat("##").format(totalEffeciencyScore)}%"
        } else {
            categoriesList[3].catScore = "0%"
        }


        //4ELEMENTS
        if (dbHelper!!.countResponsesByCategory(
                "${getSharedPrefs.getdata("surveyID")}",
                "7",
                "${getSharedPrefs.getdata("assessName")}",
                "${getSharedPrefs.getdata("deptTitle")}"
            ) != 0
        ) {
            val courtesyTotal: Double = dbHelper!!.countResponsesByCategory(
                "${getSharedPrefs.getdata("surveyID")}",
                "7",
                "${getSharedPrefs.getdata("assessName")}",
                "${getSharedPrefs.getdata("deptTitle")}"
            )
                .toDouble()
            val courtesyYes: Double = dbHelper!!.countYesResponsesByCategory(
                "${getSharedPrefs.getdata("surveyID")}",
                "7",
                "${getSharedPrefs.getdata("assessName")}",
                "${getSharedPrefs.getdata("deptTitle")}"
            )
                .toDouble()

            val catscore: Double = courtesyYes / courtesyTotal
            totalElementsScore = catscore * 100
            categoriesList[4].catScore = "${DecimalFormat("##").format(totalElementsScore)}%"
        } else {
            categoriesList[4].catScore = "0%"
        }

        //5FOOD
        if (dbHelper!!.countResponsesByCategory(
                "${getSharedPrefs.getdata("surveyID")}",
                "10",
                "${getSharedPrefs.getdata("assessName")}",
                "${getSharedPrefs.getdata("deptTitle")}"
            ) != 0
        ) {
            val courtesyTotal: Double = dbHelper!!.countResponsesByCategory(
                "${getSharedPrefs.getdata("surveyID")}",
                "10",
                "${getSharedPrefs.getdata("assessName")}",
                "${getSharedPrefs.getdata("deptTitle")}"
            )
                .toDouble()
            val courtesyYes: Double = dbHelper!!.countYesResponsesByCategory(
                "${getSharedPrefs.getdata("surveyID")}",
                "10",
                "${getSharedPrefs.getdata("assessName")}",
                "${getSharedPrefs.getdata("deptTitle")}"
            )
                .toDouble()

            val catscore: Double = courtesyYes / courtesyTotal
            totalFoodScore = catscore * 100
            categoriesList[5].catScore = "${DecimalFormat("##").format(totalFoodScore)}%"
        } else {
            categoriesList[5].catScore = "0%"
        }

        //6GRACIOUS
        if (dbHelper!!.countResponsesByCategory(
                "${getSharedPrefs.getdata("surveyID")}",
                "2",
                "${getSharedPrefs.getdata("assessName")}",
                "${getSharedPrefs.getdata("deptTitle")}"
            ) != 0
        ) {
            val courtesyTotal: Double = dbHelper!!.countResponsesByCategory(
                "${getSharedPrefs.getdata("surveyID")}",
                "2",
                "${getSharedPrefs.getdata("assessName")}",
                "${getSharedPrefs.getdata("deptTitle")}"
            )
                .toDouble()
            val courtesyYes: Double = dbHelper!!.countYesResponsesByCategory(
                "${getSharedPrefs.getdata("surveyID")}",
                "2",
                "${getSharedPrefs.getdata("assessName")}",
                "${getSharedPrefs.getdata("deptTitle")}"
            )
                .toDouble()

            val catscore: Double = courtesyYes / courtesyTotal
            totalGraciousScore = catscore * 100
            categoriesList[6].catScore = "${DecimalFormat("##").format(totalGraciousScore)}%"
        } else {
            categoriesList[6].catScore = "0%"
        }

        //7GUEST
        if (dbHelper!!.countResponsesByCategory(
                "${getSharedPrefs.getdata("surveyID")}",
                "3",
                "${getSharedPrefs.getdata("assessName")}",
                "${getSharedPrefs.getdata("deptTitle")}"
            ) != 0
        ) {
            val courtesyTotal: Double = dbHelper!!.countResponsesByCategory(
                "${getSharedPrefs.getdata("surveyID")}",
                "3",
                "${getSharedPrefs.getdata("assessName")}",
                "${getSharedPrefs.getdata("deptTitle")}"
            )
                .toDouble()
            val courtesyYes: Double = dbHelper!!.countYesResponsesByCategory(
                "${getSharedPrefs.getdata("surveyID")}",
                "3",
                "${getSharedPrefs.getdata("assessName")}",
                "${getSharedPrefs.getdata("deptTitle")}"
            )
                .toDouble()

            val catscore: Double = courtesyYes / courtesyTotal
            totalGuestScore = catscore * 100
            categoriesList[7].catScore = "${DecimalFormat("##").format(totalGuestScore)}%"
        } else {
            categoriesList[7].catScore = "0%"
        }

        //8CLASSIFICATION
        if (dbHelper!!.countResponsesByCategory(
                "${getSharedPrefs.getdata("surveyID")}",
                "11",
                "${getSharedPrefs.getdata("assessName")}",
                "${getSharedPrefs.getdata("deptTitle")}"
            ) != 0
        ) {
            val courtesyTotal: Double = dbHelper!!.countResponsesByCategory(
                "${getSharedPrefs.getdata("surveyID")}",
                "11",
                "${getSharedPrefs.getdata("assessName")}",
                "${getSharedPrefs.getdata("deptTitle")}"
            )
                .toDouble()
            val courtesyYes: Double = dbHelper!!.countYesResponsesByCategory(
                "${getSharedPrefs.getdata("surveyID")}",
                "11",
                "${getSharedPrefs.getdata("assessName")}",
                "${getSharedPrefs.getdata("deptTitle")}"
            )
                .toDouble()

            val catscore: Double = courtesyYes / courtesyTotal
            totalClassificationScore = catscore * 100
            categoriesList[8].catScore = "${DecimalFormat("##").format(totalClassificationScore)}%"
        } else {
            categoriesList[8].catScore = "0%"
        }

        //9SERVICE
        if (dbHelper!!.countResponsesByCategory(
                "${getSharedPrefs.getdata("surveyID")}",
                "12",
                "${getSharedPrefs.getdata("assessName")}",
                "${getSharedPrefs.getdata("deptTitle")}"
            ) != 0
        ) {
            val courtesyTotal: Double = dbHelper!!.countResponsesByCategory(
                "${getSharedPrefs.getdata("surveyID")}",
                "12",
                "${getSharedPrefs.getdata("assessName")}",
                "${getSharedPrefs.getdata("deptTitle")}"
            )
                .toDouble()
            val courtesyYes: Double = dbHelper!!.countYesResponsesByCategory(
                "${getSharedPrefs.getdata("surveyID")}",
                "12",
                "${getSharedPrefs.getdata("assessName")}",
                "${getSharedPrefs.getdata("deptTitle")}"
            )
                .toDouble()

            val catscore: Double = courtesyYes / courtesyTotal
            totalServiceScore = catscore * 100
            categoriesList[9].catScore = "${DecimalFormat("##").format(totalServiceScore)}%"
        } else {
            categoriesList[9].catScore = "0%"
        }

        //10STAFF
        if (dbHelper!!.countResponsesByCategory(
                "${getSharedPrefs.getdata("surveyID")}",
                "9",
                "${getSharedPrefs.getdata("assessName")}",
                "${getSharedPrefs.getdata("deptTitle")}"
            ) != 0
        ) {
            val courtesyTotal: Double = dbHelper!!.countResponsesByCategory(
                "${getSharedPrefs.getdata("surveyID")}",
                "9",
                "${getSharedPrefs.getdata("assessName")}",
                "${getSharedPrefs.getdata("deptTitle")}"
            )
                .toDouble()
            val courtesyYes: Double = dbHelper!!.countYesResponsesByCategory(
                "${getSharedPrefs.getdata("surveyID")}",
                "9",
                "${getSharedPrefs.getdata("assessName")}",
                "${getSharedPrefs.getdata("deptTitle")}"
            )
                .toDouble()

            val catscore: Double = courtesyYes / courtesyTotal
            totalStaffScore = catscore * 100
            categoriesList[10].catScore = "${DecimalFormat("##").format(totalStaffScore)}%"
        } else {
            categoriesList[10].catScore = "0%"
        }

        //11TECHNICAL
        if (dbHelper!!.countResponsesByCategory(
                "${getSharedPrefs.getdata("surveyID")}",
                "4",
                "${getSharedPrefs.getdata("assessName")}",
                "${getSharedPrefs.getdata("deptTitle")}"
            ) != 0
        ) {
            val courtesyTotal: Double = dbHelper!!.countResponsesByCategory(
                "${getSharedPrefs.getdata("surveyID")}",
                "4",
                "${getSharedPrefs.getdata("assessName")}",
                "${getSharedPrefs.getdata("deptTitle")}"
            )
                .toDouble()
            val courtesyYes: Double = dbHelper!!.countYesResponsesByCategory(
                "${getSharedPrefs.getdata("surveyID")}",
                "4",
                "${getSharedPrefs.getdata("assessName")}",
                "${getSharedPrefs.getdata("deptTitle")}"
            )
                .toDouble()

            val catscore: Double = courtesyYes / courtesyTotal
            totalTechnicalScore = catscore * 100
            categoriesList[11].catScore = "${DecimalFormat("##").format(totalTechnicalScore)}%"
        } else {
            categoriesList[11].catScore = "0%"
        }
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
        dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        //dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialogView.btn_close.setOnClickListener {
            dialog.dismiss()
        }

        dialogView.btn_save.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun showDialog() {
        // Inflates the dialog with custom view
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
        ft.replace(R.id.fragment_container, fragment, tag)
        ft.commitAllowingStateLoss()
    }
    private fun postResponseData(responseID:String,questionID: String, response: String, assesName: String, responseQuestion: String, responseComment: String, deptID: String, imageDate: String) {
        var apiService = ApiClient.client?.create(ApiInterface::class.java)
        val listcall = apiService?.responseDemoRC(
            responseID, questionID, response, assesName, "11-Feb-2019 12:29 PM", responseQuestion, responseComment, deptID, "1", imageDate,
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
    }

    private fun postAssessmentData() {
        var apiService = ApiClient.client?.create(ApiInterface::class.java)
        val listcall = apiService?.assessmentListRDemoRC(
            "${getSharedPrefs.getdata("deptID")}", "$responseID", "${getSharedPrefs.getdata("postCurrentDate")}",
            "${getSharedPrefs.getdata("postCurrentDate")}", "${getSharedPrefs.getdata("trainerName")}",
            "${getSharedPrefs.getdata("totalScore")}", "Submitted", " ",
            "${getSharedPrefs.getdata("assesID")}", "Hivelet"
        )
        listcall?.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Log.d("#PostAssessmentData", "${response.code()}")

                } else {
                    Log.d("#FailedAssessmentData", "${response.code()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("TAG", "" + t.printStackTrace())
            }
        })
    }

    private fun postGeneralDemoRC() {
        var apiService = ApiClient.client?.create(ApiInterface::class.java)
        val listcall = apiService?.generalDemoRC("$responseID", "${getSharedPrefs.getdata("deptID")}", "${getSharedPrefs.getdata("assesID")}", "", "", "Hivelet")
        listcall?.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Log.d("#PostAssessmentData", "${response.code()}")

                } else {
                    Log.d("#FailedAssessmentData", "${response.code()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("TAG", "" + t.printStackTrace())
            }

        })
    }
    /*private void emailLoginRequest() {
        LoginService loginService = ApiFactory.createService(LoginService.class);
        Call<JsonObject> call = loginService.login(edtEmail.getText().toString(),edtPassword.getText().toString(),mDeviceType,mDeviceToken);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                hideProgressDialog();
                if (response.isSuccessful()) {
                    LOGD(TAG, "onResponse 0: " + response.body().toString());
                    LoginResponse loginResponse = new Gson().fromJson(response.body().toString(), LoginResponse.class);

                    System.out.println("+++ get message >> " + loginResponse.getMessage());
                    int status = loginResponse.getStatus();

                }else {
                    LOGD(TAG, "response fail 0: " + response.body());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                hideProgressDialog();
                LOGD(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
*/

    /* fun postResponseData(){
         mAPIService!!.responseDemoRC("SampleTest2@gamil.com", "123456","123","NA","NA","NA",
             "NA","NA","NA","NA","NA").enqeue(object : Callback<CategoriesModel> {
             override fun onFailure(call: Call<CategoriesModel>, t: Throwable) {
                 *//*  Log.i("", "post registration to API" + response.body()!!.toString())
                       Log.i("", "post status to API" + response.body()!!.status)
                       Log.i("", "post msg to API" + response.body()!!.messages)*//*
            }

            override fun onResponse(call: Call<CategoriesModel>, response: Response<CategoriesModel>) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }


        })
    }*/
    /*private fun postResponseData() {

            var call1:Call<*>  = apiInterface.createUser(login);
            call1.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    LoginResponse loginResponse = response.body();

                    Log.e("keshav", "loginResponse 1 --> " + loginResponse);
                    if (loginResponse != null) {
                        Log.e("keshav", "getUserId          -->  " + loginResponse.getUserId());
                        Log.e("keshav", "getFirstName       -->  " + loginResponse.getFirstName());
                        Log.e("keshav", "getLastName        -->  " + loginResponse.getLastName());
                        Log.e("keshav", "getProfilePicture  -->  " + loginResponse.getProfilePicture());

                        String responseCode = loginResponse.getResponseCode();
                        Log.e("keshav", "getResponseCode  -->  " + loginResponse.getResponseCode());
                        Log.e("keshav", "getResponseMessage  -->  " + loginResponse.getResponseMessage());
                        if (responseCode != null && responseCode.equals("404")) {
                            Toast.makeText(MainActivity.this, "Invalid Login Details \n Please try again", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Welcome " + loginResponse.getFirstName(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "onFailure called ", Toast.LENGTH_SHORT).show();
                    call.cancel();
                }
            });
        }*/
}

