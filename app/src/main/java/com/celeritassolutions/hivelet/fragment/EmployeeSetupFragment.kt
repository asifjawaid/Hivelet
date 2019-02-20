package com.celeritassolutions.hivelet.fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.celeritassolutions.hivelet.R
import com.celeritassolutions.hivelet.adapter.EmployeeSetupAdapter
import com.celeritassolutions.hivelet.database.DatabaseHelper
import com.celeritassolutions.hivelet.utils.RecyclerItemClickListener
import com.celeritassolutions.hivelet.utils.SP
import kotlinx.android.synthetic.main.confirmation_dialog.view.*
import androidx.appcompat.widget.AppCompatEditText
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import com.celeritassolutions.hivelet.model.EmployeeModel
import com.celeritassolutions.hivelet.utils.Utils

class EmployeeSetupFragment : Fragment() {

    var loadEmployees: MutableList<String> = ArrayList()
    private var employeeList: List<EmployeeModel> = ArrayList()
    private var employeeRecycler: RecyclerView? = null
    private var btnAddEmployee: AppCompatButton? = null
    private lateinit var btnHome: AppCompatImageView
    private lateinit var btnBack: AppCompatImageView
    private var listView: ListView? = null
    var listItems: MutableList<String>? = null
    var listAdapter: ArrayAdapter<String>? = null
    private lateinit var getSPValues: SP
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var editTextSearch: AppCompatEditText
    private var countMTD: Int = 0
    private var countYTD: Int = 0
    private var currentYear: String = ""
    private lateinit var utils:Utils

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootview = inflater.inflate(R.layout.activity_employees_setup, container, false)
        employeeRecycler = rootview.findViewById(R.id.employee_recycler)
        btnAddEmployee = rootview.findViewById(R.id.btn_add_employee)
        btnHome = rootview.findViewById(R.id.home_toolbar)
        btnBack = rootview.findViewById(com.celeritassolutions.hivelet.R.id.back_toolbar)
        listView = rootview.findViewById(com.celeritassolutions.hivelet.R.id.select_employess_recycler)
        editTextSearch = rootview.findViewById(R.id.employee_search)
        utils = Utils()

        getSPValues = SP(context!!.applicationContext)
        listItems = ArrayList()
        dbHelper = DatabaseHelper(context!!.applicationContext)
        employeeList = dbHelper.select_employees()
        currentYear = utils.getYear()
        employeeRecycler?.setHasFixedSize(true)
        employeeRecycler?.layoutManager = LinearLayoutManager(this.context)
        employeeRecycler?.adapter = EmployeeSetupAdapter(employeeList, context!!.applicationContext)
        employeeRecycler?.addOnItemTouchListener(
            RecyclerItemClickListener(
                context!!,
                employeeRecycler!!,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        if (employeeList[position].isSelected == false){
                            Log.d("#name",employeeList[position].name)
                            listItems?.add(employeeList[position].name)
                            listAdapter = ArrayAdapter(
                                context!!.applicationContext, R.layout.selected_employee_item, R.id.txt_name, listItems!!
                            )
                            listView?.adapter = listAdapter
                            listAdapter?.notifyDataSetChanged()
                            employeeList[position].isSelected = true
                        }

                    }

                    override fun onLongItemClick(view: View?, position: Int) {
                    }
                })
        )

        editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun afterTextChanged(editable: Editable) {

            }
        })

        listView?.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            listView?.requestLayout()
            listItems?.removeAt(position)
            listAdapter?.notifyDataSetChanged()
            employeeList[position].isSelected = false
        }

        btnAddEmployee?.setOnClickListener {
            showDialog()
        }
        btnHome.setOnClickListener {
           addFragment(DashboardFragment(), false, "Dashboard")
        }

        btnBack.setOnClickListener {
            fragmentManager?.popBackStackImmediate()
        }

        return rootview
    }

    private fun showDialog() {
        // Inflates the dialog with custom view
        val dialogView = LayoutInflater.from(activity!!.applicationContext).inflate(R.layout.confirmation_dialog, null)
        val builder = AlertDialog.Builder(context!!)
            .setView(dialogView)

        val dialog = builder.show()
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        var responseID = getSPValues.getdata("surveyID")
        var deptTitle = getSPValues.getdata("deptTitle")
        var currentDate = getSPValues.getdata("currentDate")
        var trainerName = getSPValues.getdata("trainerName")
        var surveyStatus = getSPValues.getdata("surveyStatus")
        var surveyTotal = getSPValues.getdata("totalScore")
        dialogView.txt_dialog.text = "${listItems?.size} employee(s) added to assessment $responseID for department $deptTitle. "
        dialogView.btn_ok.setOnClickListener {
            /*countMTD = dbHelper.countSubmissionScore(getSPValues.getdata("deptTitle"))
            countYTD = dbHelper.countYTDSubmissionScore(getSPValues.getdata("deptTitle"))
            */
            getSPValues.savedata("saveResponseID",responseID)
            getSPValues.savedata("saveDeptTitle",deptTitle)
            getSPValues.savedata("saveCurrentDate",currentDate)
            getSPValues.savedata("saveTrainerName",trainerName)
            getSPValues.savedata("saveSurveyTotal",surveyTotal)
            getSPValues.savedata("saveSurveyStatus",surveyStatus)
            getSPValues.savedata("saveCountMTD",countMTD.toString())
            getSPValues.savedata("saveCountYTD",countYTD.toString())
            getSPValues.savedata("employeeSelected","true")

            Log.d("#COUNTMTD",countMTD.toString())
            fragmentManager?.popBackStackImmediate()
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
}
