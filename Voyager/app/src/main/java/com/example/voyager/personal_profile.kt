package com.example.voyager

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import kotlinx.android.synthetic.main.activity_personal_profile.*


class personal_profile : AppCompatActivity() {


    var db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_profile)
        db.collection("users")
            .whereEqualTo("Email",FirebaseAuth.getInstance().currentUser?.email)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (document in task.result!!) {
                            Log.d("TAG", document.id + " => " + document.data)
                   uname2.text = document.data.getValue("Name").toString()
                            phno2.text= document.data.getValue("phone number").toString()
                            em3.text=document.data.getValue("Email").toString()
                        }
                    } else {
                        Log.w("TAG", "Error getting documents.", task.exception)
                    }
                }


    }
}
