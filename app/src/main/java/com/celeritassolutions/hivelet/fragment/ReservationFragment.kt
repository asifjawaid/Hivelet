package com.celeritassolutions.hivelet.fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.celeritassolutions.hivelet.R
import com.celeritassolutions.hivelet.adapter.AssessmentAdapter
import com.celeritassolutions.hivelet.database.DatabaseHelper
import com.celeritassolutions.hivelet.model.AssessmentsModel
import com.celeritassolutions.hivelet.model.ResponsesModel
import com.celeritassolutions.hivelet.utils.RecyclerItemClickListener
import com.celeritassolutions.hivelet.utils.SP
import kotlinx.android.synthetic.main.guest_services_fragment.*
import kotlinx.android.synthetic.main.warning_dialod.view.*

class ReservationFragment : Fragment(), AdapterView.OnItemSelectedListener {


    private var assessmentList: List<AssessmentsModel> = ArrayList()
    private var assessmentName: ArrayList<String> = ArrayList()
    private var pastAssessmentList: List<ResponsesModel> = ArrayList()
    private var pos: Int = 0
    private var list:ArrayList<String> = ArrayList()

    private var dbHelper: DatabaseHelper? = null
    private val assessments: ArrayList<String>
    private var guestRecyclerView: RecyclerView? = null
    private var spinnerManager: Spinner? = null
    private var spinnerAssessment: Spinner? = null
    private var btnLaunch: AppCompatButton? = null
    private var titleToolbar: AppCompatTextView? = null
    private lateinit var txtManager: AppCompatTextView
    private lateinit var txtAssessments: AppCompatTextView
    private lateinit var txtSpinnerManager: String
    private lateinit var txtSpinnerAssessment: String
    private lateinit var btnBack:AppCompatImageView
    private var extraTitle: String = ""
    private var extraID: String = ""
    private lateinit var getSP: SP

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootview = inflater.inflate(R.layout.guest_services_fragment, container, false)
        guestRecyclerView = rootview.findViewById(R.id.guest_services_recyclerview)
        spinnerManager = rootview.findViewById(R.id.spnr_manager)
        spinnerManager?.onItemSelectedListener = this
        spinnerAssessment = rootview.findViewById(R.id.spnr_assessment)
        btnLaunch = rootview.findViewById(R.id.btn_launch)
        titleToolbar = rootview.findViewById(R.id.title_toolbar)
        txtAssessments = rootview.findViewById(R.id.txt_assessments)
        txtManager = rootview.findViewById(R.id.txt_manager)
        btnBack = rootview.findViewById(R.id.btn_back)
        dbHelper = DatabaseHelper(this.context!!)
        getSP = SP(context?.applicationContext!!)
        pastAssessmentList = dbHelper?.selectPastAssessments(getSP.getdata("deptTitle"))!!
        guestRecyclerView?.layoutManager = LinearLayoutManager(this.context)
        if (pastAssessmentList.size > 0){
            guestRecyclerView?.adapter = AssessmentAdapter(pastAssessmentList, this.context!!)
            guestRecyclerView?.addOnItemTouchListener(
                RecyclerItemClickListener(
                    context!!,
                    guestRecyclerView!!,
                    object : RecyclerItemClickListener.OnItemClickListener {
                        override fun onItemClick(view: View, position: Int) {
                            pos = position
                            getSP.savedata("pastAssessmentResponse", pastAssessmentList[position].respose_id)
                            addFragment(BrowserQuestionnaire(), true, pastAssessmentList[position].respose_id)
                        }
                        override fun onLongItemClick(view: View?, position: Int) {

                        }

                    })
            )
        }

        if (arguments != null){
            extraTitle = arguments?.getString("title")!!
            extraID = arguments?.getString("id")!!
        }
        loadData()
        assessmentList = dbHelper!!.selectAssessments(extraID)
        for (i in assessmentList.indices) {
            assessmentName.add(assessmentList[i].assessmentName)
        }
        assessmentName.add(assessmentList.lastIndex+1," ")

        val listsize: Int = list.size - 1
        val listAssessmentSize: Int = assessmentName.size -1

        titleToolbar?.text = extraTitle
        btn_back?.setOnClickListener {
            fragmentManager?.popBackStackImmediate()
        }
        /*Spinner Loading Data from SQLITE*/
        val adapter = object : ArrayAdapter<String>(context!!.applicationContext, android.R.layout.simple_list_item_1, assessmentName){
            override fun getCount(): Int {
                return listAssessmentSize
            }
        }

        spinnerAssessment?.adapter = adapter
        spinnerAssessment?.onItemSelectedListener = this
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerAssessment?.setSelection(listAssessmentSize)

        var adapterManager = object:ArrayAdapter<String>(context!!.applicationContext, android.R.layout.simple_list_item_1, list){
            override fun getCount(): Int {
                return listsize
            }
        }
        spinnerManager?.adapter = adapterManager
        spinnerManager?.onItemSelectedListener = this
        adapterManager.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerManager?.setSelection(listsize);

        btnLaunch?.setOnClickListener {
            if (spinnerManager?.selectedItem != " " && spinnerAssessment?.selectedItem != " ") {
                addFragment(QuestionniareFragment(), true, txtSpinnerAssessment)
                assessmentName.clear()
                list.clear()
            } else {
                Log.d("TAG", "BBC")
                showDialog()
            }
        }

        btnBack.setOnClickListener {
            fragmentManager?.popBackStackImmediate()
        }

        return rootview
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        txtSpinnerManager = spinnerManager?.selectedItem.toString()
        txtSpinnerAssessment = spinnerAssessment?.selectedItem.toString()
        txtManager.isVisible = txtSpinnerManager == " "
        txtAssessments.isVisible = txtSpinnerAssessment == " "
        pos = spinnerAssessment?.selectedItemPosition!!

    }

    private fun showDialog() {
        // Inflates the dialog with custom view
        val dialogView = LayoutInflater.from(activity!!.applicationContext).inflate(R.layout.warning_dialod, null)
        val builder = AlertDialog.Builder(context!!)
            .setView(dialogView)

        val dialog = builder.show()
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialogView.btn_ok.setOnClickListener {
            dialog.dismiss()
        }
    }

    fun loadData() {
        assessments.add("100517001531")
        assessments.add("456798601531")
        assessments.add("100545564831")
        assessments.add("895632547555")
        assessments.add("022154232857")
        assessments.add("550214879666")
        list.add("Diane ArmStrong")
        list.add("Firat Okcu")
        list.add("George Grindjam")
        list.add("Jasmin Glueck")
        list.add("Megan Bringley")
        list.add("Michael Bunder")
        list.add("Nicole Valencia")
        list.add(" ")
    }

    init {
        this.assessments = ArrayList()
    }

    fun addFragment(fragment: Fragment, addToBackStack: Boolean, tag: String) {
        val manager = activity?.supportFragmentManager
        val ft = manager!!.beginTransaction()

        if (addToBackStack) {
            ft.addToBackStack(tag)
        }
        val ldf = fragment
        val args = Bundle()
        args.putString("title", tag)
        args.putString("id", assessmentList[pos].assessmentID)
        args.putString("trainer",spinnerManager?.selectedItem.toString())
        args.putString("extraTitle", extraTitle)
        args.putString("extraID", extraID)
        getSP.savedata("assessName",assessmentList[pos].assessmentName)
        getSP.savedata("assesID",assessmentList[pos].assessmentID)
        ldf.setArguments(args)
        ft.replace(com.celeritassolutions.hivelet.R.id.fragment_container, fragment, tag)
        ft.commitAllowingStateLoss()
    }
}