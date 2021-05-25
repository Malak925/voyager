package com.example.voyager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_create_trip.*
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
                            registerUser(tripID?.toString()?:"")
                            getTripDetails(tripID?.toString())

                        }
                    } else {
                        Log.w("TAG", "Error getting documents.", task.exception)
                    }
                }

        })
    }

    fun registerUser(tripId:String){
        val userEmail= FirebaseAuth.getInstance().currentUser?.email
        val tripUser: MutableMap<String, Any> = HashMap()
        tripUser["tripId"] = tripId
        tripUser["userId"] = userEmail?:""

        db.collection("tripUsers")
            .add(tripUser)
            .addOnSuccessListener { documentReference ->
                Log.d(
                    "TAG", "DocumentSnapshot added with ID: " + documentReference.id
                )
            }
            .addOnFailureListener { e -> Log.w("TAG", "Error adding document", e) }
    }

    fun getTripDetails(tripID: String?){
        db.collection("Trip")
            .document(tripID?:"")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task?.result?.data?.let{
                        Log.d("TripResult",it.toString())
                        val intent3 = Intent (this , participants::class.java)

                        val intent= Intent(this,trip_info::class.java)
                        intent.putExtra("cap",it["trip cap"].toString())
                        intent.putExtra("location",it["trip location"].toString())
                        intent.putExtra("name",it["trip name"].toString())
                        intent.putExtra("date",it["trip date"].toString())
                        intent.putExtra("code",editTextNumber.text.toString())
                        intent3.putExtra("cap",it["trip cap"].toString())


                        startActivity(intent)
                    }
                } else {
                    Log.w("TAG", "Error getting documents.", task.exception)
                }
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
        else if (item.itemId == R.id.bills) {
            startActivity(Intent(this, BillMenu::class.java))


        }
        else if (item.itemId == R.id.end) {

            startActivity(Intent(this, EndTrip::class.java))



        }

        return super.onOptionsItemSelected(item)
    }


}