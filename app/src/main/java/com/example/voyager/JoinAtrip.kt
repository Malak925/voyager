package com.example.voyager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_join_atrip.*
import kotlinx.android.synthetic.main.activity_personal_profile.*
import kotlinx.android.synthetic.main.activity_start_trip.*

class JoinAtrip : AppCompatActivity() {

    var db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_atrip)

        jnTrip2.setOnClickListener(View.OnClickListener {
            val code= editTextNumber.text.toString()

            db.collection("Code")
                .whereEqualTo("code", code)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (document in task.result!!) {
                            val tripID= document.get("tripID")
                            Log.d("TAG", "tripID: $tripID" )
                            getTripDetails(tripID?.toString())
                        }
                    } else {
                        Log.w("TAG", "Error getting documents.", task.exception)
                    }
                }

        })
    }

    fun getTripDetails(tripID: String?){
        db.collection("Trip")
            .document(tripID?:"")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task?.result?.data?.let{
                        Log.d("TripResult",it.toString())
                        val intent= Intent(this,trip_info::class.java)
                        intent.putExtra("cap",it["trip cap"].toString())
                        intent.putExtra("location",it["trip location"].toString())
                        intent.putExtra("name",it["trip name"].toString())
                        intent.putExtra("date",it["trip date"].toString())
                        intent.putExtra("code",editTextNumber.text.toString())
                        startActivity(intent)
                    }
                } else {
                    Log.w("TAG", "Error getting documents.", task.exception)
                }
            }
    }
}