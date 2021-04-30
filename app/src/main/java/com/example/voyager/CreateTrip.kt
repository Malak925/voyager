package com.example.voyager

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_create_trip.*


class CreateTrip : AppCompatActivity() {
    var db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_trip)
        var i = Intent(this, CodeGen::class.java)
        nxt1.setOnClickListener {
// Create a new user with a first and last name
            // Create a new user with a first and last name
            val trip: MutableMap<String, Any> = HashMap()
            trip["trip name"] = tripNm.text.toString()
            trip["trip location"] = Triploc.text.toString()
            trip["trip date"] = Tripdt.text.toString()
            trip["trip cap"] = Tripcap.text.toString()

// Add a new document with a generated ID

// Add a new document with a generated ID
            db.collection("Trip")
                .add(trip)
                .addOnSuccessListener { documentReference ->
                    Log.d(
                        "TAG", "DocumentSnapshot added with ID: " + documentReference.id
                    )
                    i.putExtra(CodeGen.KEY_TRIP_ID,documentReference.id)
                    startActivity(i)
                }
                .addOnFailureListener { e -> Log.w("TAG", "Error adding document", e) }


        }


    }
}