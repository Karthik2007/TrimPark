package com.karthik.trimpark.home.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.karthik.trimpark.R
import com.karthik.trimpark.parkingentry.view.ParkingEntryActivity
import com.karthik.trimpark.parkingexit.view.ParkingExitActivity
import kotlinx.android.synthetic.main.activity_main.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupActionListeners()
    }

    private fun setupActionListeners() {

        entry_btn.setOnClickListener {
            startActivity(Intent(applicationContext,ParkingEntryActivity::class.java))
        }

        exit_btn.setOnClickListener {
            startActivity(Intent(applicationContext, ParkingExitActivity::class.java))
        }

        collection_btn.setOnClickListener(WIPListener)
        parking_lot_btn.setOnClickListener(WIPListener)
    }

    var WIPListener = View.OnClickListener {
        Toast.makeText(this,"Feature under construction", Toast.LENGTH_LONG).show()
    }
}
