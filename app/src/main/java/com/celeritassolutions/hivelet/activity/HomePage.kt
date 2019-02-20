package com.celeritassolutions.hivelet.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.celeritassolutions.hivelet.R
import androidx.fragment.app.Fragment
import com.celeritassolutions.hivelet.fragment.DashboardFragment

class HomePage: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_fragment)
        addFragment(DashboardFragment(), false, "DASHBOARD")
    }

    fun addFragment(fragment: Fragment, addToBackStack: Boolean, tag: String) {
        val manager = supportFragmentManager
        val ft = manager.beginTransaction()

        if (addToBackStack) {
            ft.addToBackStack(tag)
        }
        ft.replace(R.id.fragment_container, fragment, tag)
        ft.commitAllowingStateLoss()
    }
}