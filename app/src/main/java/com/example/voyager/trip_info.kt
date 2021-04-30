package com.example.voyager

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_trip_info.*


class trip_info : AppCompatActivity() {
    var db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_info)
        db.collection("Trip")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        Log.d("TAG", document.id + " => " + document.data)
                        tripNm2.text = document.data.getValue("trip name").toString()
                        Tripcap2.text=document.data.getValue("trip cap").toString()
                        Tripdt2.text=document.data.getValue("trip date").toString()
                        Triploc2.text=document.data.getValue("trip location").toString()
                    }
                } else {
                    Log.w("TAG", "Error getting documents.", task.exception)
                }
            }

        db.collection("Code")
            .get()
            .addOnCompleteListener{ task ->
            if (task.isSuccessful){
                for(document in task.result!!)
                {
                    cd.text = document.data.getValue("generated").toString()
                }


            }





            }


    }

}