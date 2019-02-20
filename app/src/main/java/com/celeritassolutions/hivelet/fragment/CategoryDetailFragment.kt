package com.celeritassolutions.hivelet.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.celeritassolutions.hivelet.R
import com.celeritassolutions.hivelet.adapter.positiveCatAdapter
import com.celeritassolutions.hivelet.database.DatabaseHelper
import com.celeritassolutions.hivelet.model.BrowseQuestionnaireModel
import com.celeritassolutions.hivelet.utils.SP

class CategoryDetailFragment : Fragment() {

    private var catName: String = ""
    private lateinit var dbHelper :DatabaseHelper
    private lateinit var getSharedPreferences: SP
    private var responseID: String = ""
    private var positiveCatList: List<BrowseQuestionnaireModel> = ArrayList()
    private var negativeCatList: List<BrowseQuestionnaireModel> = ArrayList()
    private var positiveCatResponseRecycler: RecyclerView? = null
    private var negativeCatResponseRecycler: RecyclerView? = null
    private lateinit var btnBack: AppCompatImageView
    private lateinit var btnHome: AppCompatImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        val rootView = inflater.inflate(R.layout.fragment_category_detail, container, false)

        positiveCatResponseRecycler = rootView.findViewById(R.id.rv_positive)
        negativeCatResponseRecycler = rootView.findViewById(R.id.rv_negative)
        btnBack = rootView.findViewById(R.id.back_toolbar)
        btnHome = rootView.findViewById(R.id.home_toolbar)


        dbHelper = DatabaseHelper(context!!.applicationContext)
        getSharedPreferences = SP(context!!.applicationContext)

        responseID = getSharedPreferences.getdata("surveyID")
        catName = getSharedPreferences.getdata("catTitle")
        positiveCatList = dbHelper.setCatPositiveResponses(responseID,catName)
        negativeCatList = dbHelper.setCatNegativeResponses(responseID,catName)

        Log.d("#catName",catName)
        Log.d("#responseID",responseID)
        for (i in positiveCatList.indices) {
            Log.d("#positiveCatList", positiveCatList[i].class_name)
        }
        positiveCatResponseRecycler?.layoutManager = LinearLayoutManager(this.context)
        positiveCatResponseRecycler?.adapter = positiveCatAdapter(positiveCatList, this.context!!)

        negativeCatResponseRecycler?.layoutManager = LinearLayoutManager(this.context)
        negativeCatResponseRecycler?.adapter = positiveCatAdapter(negativeCatList, this.context!!)

        btnBack.setOnClickListener {
            fragmentManager?.popBackStackImmediate()
        }

        btnHome.setOnClickListener {
            addFragment(DashboardFragment(), false, "Dashboard")
        }
        return rootView
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
