package com.example.voyager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_end_trip.*

class EndTrip : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_end_trip)
        endtrip.setOnClickListener {

            val senderEmail = "Voyager.M.O.925@gmail.com\n"
            var RecieverEmail = "${ FirebaseAuth.getInstance().currentUser?.email}"
            var intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_EMAIL,RecieverEmail)
            intent.putExtra(Intent.EXTRA_SUBJECT,"Hope you had the most FUN")
            intent.putExtra(Intent.EXTRA_TEXT,"")
            intent.setType("message/rfc822")
            startActivity(Intent.createChooser(intent,"Choose an email Client"))





        }
    }
}