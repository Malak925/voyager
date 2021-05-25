package com.example.voyager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_bill.*
import kotlin.math.cos

class Bill : AppCompatActivity() {
    var db = FirebaseFirestore.getInstance()
    var cost= 0f
    var serviceName= ""
    var saveBillsCount= 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bill)
        saveButton.setOnClickListener {
            cost= costText.text.toString().toFloat()
            serviceName= billName.text.toString()

            loadTripId()
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
        else if (item.itemId == R.id.end) {

            startActivity(Intent(this, EndTrip::class.java))



        }

        return super.onOptionsItemSelected(item)
    }


    fun loadTripId(){
        db.collection("tripUsers")
            .whereEqualTo("userId", FirebaseAuth.getInstance().currentUser?.email)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful && task.result?.documents?.isEmpty()==false) {

                    task?.result?.documents?.get(0).let{
                        Log.d("TripResult",it?.get("tripId").toString())
                        val tripId= it?.get("tripId").toString()
                        loadParticipants(tripId)
                    }
                } else {
                    Log.w("TAG", "Error getting documents.", task.exception)
                }
            }
    }
    private fun loadParticipants(tripId: String?) {
        db.collection("tripUsers")
            .whereEqualTo("tripId",tripId)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task?.result?.documents?.let{ documents ->
                        val costPerUser= cost/documents.size

                        documents.forEach{ documentSnapshot ->
                            val billMap: MutableMap<String, Any> = HashMap()
                            billMap["cost"] = costPerUser
                            billMap["serviceName"] = serviceName?:""
                            billMap["tripId"] = tripId?:""
                            billMap["userId"] = documentSnapshot.get("userId")?:""
                            saveBill(billMap,documents.size)
                        }

                    }
                } else {
                    Log.w("TAG", "Error getting documents.", task.exception)
                }
            }
    }

    private fun saveBill(billMap: MutableMap<String, Any>, size:Int) {
        db.collection("tripBills").add(billMap)
            .addOnSuccessListener { documentReference ->
            Log.d(
                "TAG", "DocumentSnapshot added with ID: " + documentReference.id)
                saveBillsCount++

                if(saveBillsCount>=size){
                    // todo finished
                }
        }
            .addOnFailureListener { e -> Log.w("TAG", "Error adding document", e) }
    }
}