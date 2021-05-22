package com.example.voyager

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_create_trip.*
import java.util.*
import kotlin.collections.HashMap


open class CreateTrip : AppCompatActivity() {

    var db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_trip)
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)




        Pkdt.setOnClickListener {
            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { View, mYear, mMonth, mDay ->

                    Tripdt.setText("" + mDay + "/" + mMonth + "/" + mYear)
                },
                year,
                month,
                day)

            dpd.show()


        }

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
                    i.putExtra(CodeGen.KEY_TRIP_ID, documentReference.id)
                    startActivity(i)
                }
                .addOnFailureListener { e -> Log.w("TAG", "Error adding document", e) }


        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId==R.id.perpro)

        {

            startActivity(Intent(this,personal_profile::class.java))
        }
        else if (item.itemId==R.id.tripro)
        {

            startActivity(Intent(this,trip_info::class.java))
        }
        else if (item.itemId==R.id.UpImg){
            startActivity(Intent(this,imageUpload::class.java))


        }
        else if (item.itemId==R.id.Bill){
            startActivity(Intent(this,Bill::class.java))


        }
        else if (item.itemId==R.id.parLst){
            startActivity(Intent(this,participants::class.java))


        }

        return super.onOptionsItemSelected(item)
    }

}