package com.celeritassolutions.hivelet.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.celeritassolutions.hivelet.R
import com.celeritassolutions.hivelet.utils.RecyclerItemClickListener
import com.celeritassolutions.hivelet.adapter.DashboardAdapter
import com.celeritassolutions.hivelet.database.DatabaseHelper
import com.celeritassolutions.hivelet.model.DepartmentsModel
import com.celeritassolutions.hivelet.utils.SP
import com.celeritassolutions.hivelet.utils.Utils
import java.text.DecimalFormat

class DashboardFragment : Fragment() {
    private var departmentList: List<DepartmentsModel> = ArrayList()
    private var dashboardRecycler: RecyclerView? = null
    private var dbHelper: DatabaseHelper? = null
    private var pos: Int = 0
    private lateinit var sp: SP
    private lateinit var txtMTDScore: AppCompatTextView
    private lateinit var txtYTDScore: AppCompatTextView
    private lateinit var totalMTD: AppCompatTextView
    private lateinit var totalYTD: AppCompatTextView
    private lateinit var totalAvgMTD: AppCompatTextView
    private lateinit var totalAvgYTD: AppCompatTextView
    private var countHouseKeeping: Int = 0
    private var countReservation: Int = 0
    private var countFront: Int = 0
    private var countLoss: Int = 0
    private var countSpa: Int = 0
    private var countHotel: Int = 0
    private var countRoom: Int = 0
    private var countPublic: Int = 0

    private var YTDHouseKeeping: Int = 0
    private var YTDReservation: Int = 0
    private var YTDFront: Int = 0
    private var YTDLoss: Int = 0
    private var YTDSpa: Int = 0
    private var YTDHotel: Int = 0
    private var YTDRoom: Int = 0
    private var YTDPublic: Int = 0

    private var sumHouseKeeping: Int = 0
    private var sumReservation: Int = 0
    private var sumFront: Int = 0
    private var sumLoss: Int = 0
    private var sumSpa: Int = 0
    private var sumHotel: Int = 0
    private var sumRoom: Int = 0
    private var sumPublic: Int = 0

    private var sumYTDHouseKeeping: Int = 0
    private var sumYTDReservation: Int = 0
    private var sumYTDFront: Int = 0
    private var sumYTDLoss: Int = 0
    private var sumYTDSpa: Int = 0
    private var sumYTDHotel: Int = 0
    private var sumYTDRoom: Int = 0
    private var sumYTDPublic: Int = 0

    private var dashboardHouseKeeping: Double = 0.0
    private var dashboardReservation: Double = 0.0
    private var dashboardFront: Double = 0.0
    private var dashboardLoss: Double = 0.0
    private var dashboardSpa: Double = 0.0
    private var dashboardHotel: Double = 0.0
    private var dashboardRoom: Double = 0.0
    private var dashboardPublic: Double = 0.0


    //AVG count vars
    private var avgMTDCount: Double = 0.0
    private var totalAvgMTDCount: Double = 0.0
    private var avgYTDCount: Double = 0.0
    private var totalAvgYTDCount: Double = 0.0

    private var assesMTDScore: Double = 0.0
    private var assesYTDScore: Double = 0.0

    private lateinit var utils: Utils

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootview = inflater.inflate(R.layout.activity_dashboard, container, false)
        dashboardRecycler = rootview.findViewById(R.id.dashboard_recycler)
        txtMTDScore = rootview.findViewById(R.id.avg_mtd)
        txtYTDScore = rootview.findViewById(R.id.avg_ytd)
        totalMTD = rootview.findViewById(R.id.lbl_mtd)
        totalYTD = rootview.findViewById(R.id.lbl_ytd)
        totalAvgMTD = rootview.findViewById(R.id.txt_avg_mtd)
        totalAvgYTD = rootview.findViewById(R.id.txt_avg_ytd)

        dbHelper = DatabaseHelper(this.context!!)
        countHouseKeeping = dbHelper!!.countSubmissionScore("Housekeeping")
        countReservation = dbHelper!!.countSubmissionScore("Reservations")
        countFront = dbHelper!!.countSubmissionScore("Front Desk")
        countLoss = dbHelper!!.countSubmissionScore("Loss Prevention")
        countSpa = dbHelper!!.countSubmissionScore("Spa")
        countHotel = dbHelper!!.countSubmissionScore("Hotel Bar")
        countRoom = dbHelper!!.countSubmissionScore("Room Service")
        countPublic = dbHelper!!.countSubmissionScore("Public Space")
        Log.d("#Room","$countRoom")
        YTDHouseKeeping = dbHelper!!.countYTDSubmissionScore("Housekeeping")
        YTDReservation = dbHelper!!.countYTDSubmissionScore("Reservations")
        YTDFront = dbHelper!!.countYTDSubmissionScore("Front Desk")
        YTDLoss = dbHelper!!.countYTDSubmissionScore("Loss Prevention")
        YTDSpa = dbHelper!!.countYTDSubmissionScore("Spa")
        YTDHotel = dbHelper!!.countYTDSubmissionScore("Hotel Bar")
        YTDRoom = dbHelper!!.countYTDSubmissionScore("Room Service")
        YTDPublic = dbHelper!!.countYTDSubmissionScore("Public Space")

        sumHouseKeeping = dbHelper!!.sumSubmissionScore("Housekeeping")
        sumReservation = dbHelper!!.sumSubmissionScore("Reservations")
        sumFront = dbHelper!!.sumSubmissionScore("Front Desk")
        sumLoss = dbHelper!!.sumSubmissionScore("Loss Prevention")
        sumSpa = dbHelper!!.sumSubmissionScore("Spa")
        sumHotel = dbHelper!!.sumSubmissionScore("Hotel Bar")
        sumRoom = dbHelper!!.sumSubmissionScore("Room Service")
        sumPublic = dbHelper!!.sumSubmissionScore("Public Space")

        sumYTDHouseKeeping = dbHelper!!.sumSubmissionYTDScore("Housekeeping")

        sumYTDReservation = dbHelper!!.sumSubmissionYTDScore("Reservations")
        sumYTDFront = dbHelper!!.sumSubmissionYTDScore("Front Desk")
        sumYTDLoss = dbHelper!!.sumSubmissionYTDScore("Loss Prevention")
        sumYTDSpa = dbHelper!!.sumSubmissionYTDScore("Spa")
        sumYTDHotel = dbHelper!!.sumSubmissionYTDScore("Hotel Bar")
        sumYTDRoom = dbHelper!!.sumSubmissionYTDScore("Room Service")
        sumYTDPublic = dbHelper!!.sumSubmissionYTDScore("Public Space")

        assesYTDScore = (sumYTDHouseKeeping+sumYTDReservation+sumYTDFront+sumYTDLoss+sumYTDSpa+sumYTDHotel+sumYTDRoom+sumYTDPublic).toDouble()

        departmentList = dbHelper!!.selectDepartments()


        if (countHouseKeeping != 0){
            dashboardHouseKeeping = (sumHouseKeeping/countHouseKeeping).toDouble()
            departmentList[0].deptScore = dashboardHouseKeeping.toString()
            avgMTDCount += 1
            avgYTDCount += 1
            assesMTDScore += sumHouseKeeping
            Log.d("#SUMHOUSEKEEPING","$sumHouseKeeping")
        }else{
            departmentList[0].deptScore = "0.0"
        }
        if (countReservation != 0){
            dashboardReservation = (sumReservation/(countReservation)).toDouble()
            departmentList[1].deptScore = dashboardReservation.toString()
            avgMTDCount += 1
            avgYTDCount += 1
            assesMTDScore += sumReservation
        }else{
            departmentList[1].deptScore = "0.0"
        }
        if (countFront != 0){
            dashboardFront = (sumFront/(countFront)).toDouble()
            departmentList[2].deptScore = dashboardFront.toString()
            avgMTDCount += 1
            avgYTDCount += 1
            assesMTDScore += sumFront
        }else{
            departmentList[2].deptScore = "0.0"
        }
        if (countLoss!=0){
            dashboardLoss = (sumLoss/(countLoss)).toDouble()
            departmentList[3].deptScore = dashboardLoss.toString()
            avgMTDCount += 1
            avgYTDCount += 1
            assesMTDScore += sumLoss
        }else{
            departmentList[3].deptScore = "0.0"
        }
        if (countSpa!=0){
            dashboardSpa = (sumSpa/(countSpa)).toDouble()
            departmentList[4].deptScore = dashboardSpa.toString()
            avgMTDCount += 1
            avgYTDCount += 1
            assesMTDScore += sumSpa
        }else{
            departmentList[4].deptScore = "0.0"
        }
        if (countHotel!=0){
            dashboardHotel = (sumHotel/(countHotel)).toDouble()
            departmentList[5].deptScore = dashboardHotel.toString()
            avgMTDCount += 1
            avgYTDCount += 1
            assesMTDScore += sumHotel
        }else{
            departmentList[5].deptScore = "0.0"
        }
        if (countRoom!=0){
            dashboardRoom = (sumRoom/(countRoom)).toDouble()
            departmentList[6].deptScore = dashboardRoom.toString()
            avgMTDCount += 1
            avgYTDCount += 1
            assesMTDScore += sumRoom
        }else{
            departmentList[6].deptScore = "0.0"
        }
        if (countPublic!=0){
            dashboardPublic = (sumPublic/(countPublic)).toDouble()
            departmentList[7].deptScore = dashboardPublic.toString()
            avgMTDCount += 1
            avgYTDCount += 1
            assesMTDScore += sumPublic
        }else{
            departmentList[7].deptScore = "0.0"
        }

        totalMTD.text = (countHouseKeeping+countReservation+countFront+countLoss+countSpa+countHotel+countRoom+countPublic).toString()
        totalYTD.text = (YTDHouseKeeping+YTDReservation+YTDFront+YTDLoss+YTDSpa+YTDHotel+YTDRoom+YTDPublic).toString()
        totalAvgMTDCount = (countHouseKeeping+countReservation+countFront+countLoss+countSpa+countHotel+countRoom+countPublic).toDouble()
        totalAvgYTDCount = (YTDHouseKeeping+YTDReservation+YTDFront+YTDLoss+YTDSpa+YTDHotel+YTDRoom+YTDPublic).toDouble()
        val finalAvgMTDCount: Double
        val finalAvgYTDCount: Double
        if(avgMTDCount != 0.0 && avgYTDCount != 0.0){
            finalAvgMTDCount = (totalAvgMTDCount/avgMTDCount)
            finalAvgYTDCount = (totalAvgYTDCount/avgYTDCount)

            totalAvgMTD.text = DecimalFormat("##.#").format(finalAvgMTDCount)
            totalAvgYTD.text = DecimalFormat("##.#").format(finalAvgYTDCount)
        }else{
            totalAvgMTD.text = "0.0"
            totalAvgYTD.text = "0.0"
        }

        val txtAssessMTD = assesMTDScore/totalAvgMTDCount
        val txtAssessYTD = assesYTDScore/totalAvgYTDCount
        if (txtAssessMTD != 0.0){
            txtMTDScore.text = DecimalFormat("##.#").format(txtAssessMTD)
        }else{
            txtMTDScore.text = "0.0"
        }

        if (txtAssessMTD != 0.0){
            txtYTDScore.text = DecimalFormat("##.#").format(txtAssessYTD)
        }else{
            txtYTDScore.text = "0.0"
        }



        utils = Utils()
        sp = SP(context!!.applicationContext)
        dashboardRecycler?.layoutManager = LinearLayoutManager(this.context)
        dashboardRecycler?.adapter = DashboardAdapter(departmentList, this.context!!)
        dashboardRecycler?.addOnItemTouchListener(
            RecyclerItemClickListener(
                context!!,
                dashboardRecycler!!,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        pos = position
                        when(position){
                            0 -> {
                                sp.savedata("deptID","1")
                                addFragment(HouseKeepingFragment(),true, departmentList[position].deptName)
                            }
                            1->{
                                sp.savedata("deptID","2")
                                addFragment(ReservationFragment(),true, departmentList[position].deptName)
                            }
                            2->{
                                sp.savedata("deptID","3")
                                addFragment(FrontDeskFragment(),true, departmentList[position].deptName)
                            }
                            3->{
                                sp.savedata("deptID","4")
                                addFragment(LossPreventionFragment(), true, departmentList[position].deptName)
                            }
                            4->{
                                sp.savedata("deptID","5")
                                addFragment(SpaFragment(), true, departmentList[position].deptName)
                            }
                            5->{
                                sp.savedata("deptID","6")
                                addFragment(HotelBarFragment(), true, departmentList[position].deptName)
                            }
                            6->{
                                sp.savedata("deptID","7")
                                addFragment(RoomServiceFragment(), true, departmentList[position].deptName)
                            }
                            7->{
                                sp.savedata("deptID","8")
                                addFragment(PublicSpaceFragment(), true, departmentList[position].deptName)
                            }
                        }
                    }

                    override fun onLongItemClick(view: View?, position: Int) {

                    }

                })
        )


        return rootview
    }

    private fun initilizeCounts() {

    }

    fun addFragment(fragment: Fragment, addToBackStack: Boolean, tag: String) {
        val manager = activity?.supportFragmentManager
        val ft = manager!!.beginTransaction()

        if (addToBackStack) {
            ft.addToBackStack(tag)
        }
        val ldf = fragment
        val args = Bundle()
        args.putString("title", departmentList[pos].deptName)
        args.putString("id", departmentList[pos].deptID)
        sp.savedata("deptTitle",departmentList[pos].deptName)
        ldf.setArguments(args)
        ft.replace(com.celeritassolutions.hivelet.R.id.fragment_container, fragment, tag)
        ft.commitAllowingStateLoss()
    }
}