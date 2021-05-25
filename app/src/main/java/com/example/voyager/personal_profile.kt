package com.example.voyager

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import kotlinx.android.synthetic.main.activity_personal_profile.*


class personal_profile : AppCompatActivity() {


    var db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_profile)
        var userEmail= intent.getStringExtra("email")
        if(userEmail==null){
            userEmail= FirebaseAuth.getInstance().currentUser?.email
        }

        db.collection("users")
            .whereEqualTo("Email",userEmail)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (document in task.result!!) {
                            Log.d("TAG", document.id + " => " + document.data)
                            uname2.text = document.data.getValue("Name").toString()
                            phno2.text= document.data.getValue("phone number").toString()
                            em3.text=document.data.getValue("Email").toString()

                           Glide.with(this)
                                .load(document.data.getValue("imageUrl").toString())
                                .into(pic2)

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
