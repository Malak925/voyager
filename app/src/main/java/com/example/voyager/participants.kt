package com.example.voyager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_join_atrip.*
import kotlinx.android.synthetic.main.activity_personal_profile.*
import kotlinx.android.synthetic.main.activity_trip_info.*

class participants : AppCompatActivity() {
    var db = FirebaseFirestore.getInstance()
    val participantsEmails= ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_participants)
        loadTripId()
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
           /* val senderEmail = "Voyager.M.O.925@gmail.com\n"
            var RecieverEmail = "${ FirebaseAuth.getInstance().currentUser?.email}"
            var intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_EMAIL,RecieverEmail)
            intent.putExtra(Intent.EXTRA_SUBJECT,"Hope you had the most FUN")
            intent.setType("message/rfc822")
            startActivity(Intent.createChooser(intent,"Choose an email Client"))*/
            startActivity(Intent(this, EndTrip::class.java))



        }


        return super.onOptionsItemSelected(item)
    }

    fun loadTripId(){
        db.collection("tripUsers")
            .whereEqualTo("userId",FirebaseAuth.getInstance().currentUser?.email)
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
                        documents.forEach{ documentSnapshot ->
                            participantsEmails.add(documentSnapshot.get("userId").toString())
                        }
                        setListView()
                    }
                } else {
                    Log.w("TAG", "Error getting documents.", task.exception)
                }
            }
    }

    private fun setListView() {
        val mListview = findViewById<ListView>(R.id.parLst)
        var adapter = ArrayAdapter<String>(this , android.R.layout.simple_list_item_1,participantsEmails )
        mListview.adapter = adapter

        mListview.setOnItemClickListener() {
                parent , view , position , id ->

            val intent= Intent(this, personal_profile::class.java)
            intent.putExtra("email",participantsEmails[position])
            startActivity(intent)
        }
    }
}

