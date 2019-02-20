package com.celeritassolutions.hivelet.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.celeritassolutions.hivelet.R
import com.celeritassolutions.hivelet.database.DatabaseHelper
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_continue.setOnClickListener {
            val intent = Intent(this, HomePage::class.java)
            startActivity(intent)
        }

        try{
            val adb = DatabaseHelper(this)

            adb.openDatabase()

            Log.d("#DB","writing>>")
        }catch (e: Exception){
            e.printStackTrace()
        }
    }
}
