package com.example.voyager

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_trip_info.*


class trip_info : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_info)
        val bundle= intent.extras
        bundle?.let {
            tripNm2.text = it.getString("name")
            Tripcap2.text= it.getString("cap")
            Tripdt2.text=it.getString("date")
            Triploc2.text=it.getString("location")
            cd.text= it.getString("code")
        }
    }
}