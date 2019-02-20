package com.celeritassolutions.hivelet.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.celeritassolutions.hivelet.R
import com.celeritassolutions.hivelet.adapter.BrowseQuestionnaireAdapter
import com.celeritassolutions.hivelet.database.DatabaseHelper
import com.celeritassolutions.hivelet.model.BrowseQuestionnaireModel
import com.celeritassolutions.hivelet.utils.SP
import com.celeritassolutions.hivelet.utils.Utils
import java.util.*


class BrowserQuestionnaire : Fragment(){


    private var browseQuestionnaire: List<BrowseQuestionnaireModel> = ArrayList()
    private var dbHelper: DatabaseHelper? = null
    private var arrivalDashboard: RecyclerView? = null
    private lateinit var btnProceed: AppCompatButton
    private lateinit var btnHome: AppCompatImageView
    private lateinit var btnAddEmployee: AppCompatImageView
    private lateinit var titleToolbar: AppCompatTextView
    private lateinit var txtDate: AppCompatTextView
    private lateinit var txtSurveyID: AppCompatTextView
    private var utils = Utils()
    private lateinit var saveSharedPrefs: SP
    private var surveyID: String = ""
    private var currentDate: String = utils.getCurrentDate()
    private lateinit var pos :String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootview = inflater.inflate(R.layout.activity_arrival_service, container, false)
        arrivalDashboard = rootview.findViewById(R.id.arrival_dashboard)
        btnProceed = rootview.findViewById(R.id.btn_proceed)
        btnHome = rootview.findViewById(R.id.home_toolbar)
        btnAddEmployee = rootview.findViewById(R.id.btn_email)
        titleToolbar = rootview.findViewById(R.id.title_toolbar)
        txtDate = rootview.findViewById(R.id.txt_date)
        txtSurveyID = rootview.findViewById(R.id.survey_id)

        dbHelper = DatabaseHelper(this.context!!)

        saveSharedPrefs = SP(this.context!!)
        pos = saveSharedPrefs.getdata("pos")
        surveyID = saveSharedPrefs.getdata("pastAssessmentResponse")
        //titleToolbar.text = extraTitle
        txtDate.text = currentDate
        txtSurveyID.text = surveyID

        browseQuestionnaire = dbHelper!!.browseQuestionnaire(surveyID)
        Log.d("BrowseQue",surveyID)
        arrivalDashboard?.layoutManager = LinearLayoutManager(this.context)
        arrivalDashboard?.adapter = BrowseQuestionnaireAdapter(browseQuestionnaire, this.context!!)

        btnProceed.setOnClickListener {
            addFragment(BrowseQuestionniareSummaryFragment(), true, "")
        }

        btnHome.setOnClickListener {
            addFragment(DashboardFragment(), false, "Dashboard")
        }

        btnAddEmployee.setOnClickListener {
            addFragment(EmployeeSetupFragment(), true, "Add Employee Setup")
        }

        for (survey_index in browseQuestionnaire.indices){

            //survey_index+1+1 because our questionnaire index starts from 1
            dbHelper!!.browseQuestionnaire(surveyID)
        }

        return rootview
    }

    fun addFragment(fragment: Fragment, addToBackStack: Boolean, tag: String) {
        val manager = activity?.supportFragmentManager
        val ft = manager!!.beginTransaction()

        if (addToBackStack) {
            ft.addToBackStack(tag)
        }
        val ldf = fragment
        val args = Bundle()
        ldf.arguments = args
        ft.replace(com.celeritassolutions.hivelet.R.id.fragment_container, fragment, tag)
        ft.commitAllowingStateLoss()
    }
}
