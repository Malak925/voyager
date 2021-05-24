package com.example.voyager


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthProvider
import kotlinx.android.synthetic.main.activity_main.*

class  MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        var y = Intent(this , signin::class.java)
        sg1.setOnClickListener {
            startActivity(y)
        }
        lg1.setOnClickListener {
            em1.setText("test4@test.com")
            ps1.setText("123456789")
            if (em1.text.trim().toString().isNotEmpty()&&ps1.text.trim().toString().isNotEmpty()){

                creatUser(em1.text.trim().toString(), ps1.text.trim().toString())

            }else {

                Toast.makeText(this, "Input Required", Toast.LENGTH_LONG).show()
            }

            Log.e("Action", "Clicked")

        }
    }
    fun creatUser(email: String, password: String){

        var i = Intent(this, StartTrip::class.java)

        auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.e("task message", "sucessful")
                        task?.result?.let {
                            task?.result?.user?.let { it1 ->
                                FirebaseAuth.getInstance().updateCurrentUser(
                                    it1
                                )
                            }
                        }
                        startActivity(i)

                    } else {
                        Log.e("task messege", "failed")
                    }

                }}
}


