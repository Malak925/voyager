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

        if(auth.currentUser!=null){
            startActivity(Intent(this, StartTrip::class.java))
            finish()
        }


        var y = Intent(this , signin::class.java)
        sg1.setOnClickListener {
            startActivity(y)
        }
        lg1.setOnClickListener {
            /*em1.setText("Malakdawod925@gmail.com")
            ps1.setText("Malak525")*/
            if (em1.text.trim().toString().isNotEmpty()&&ps1.text.trim().toString().isNotEmpty()){

                creatUser(em1.text.trim().toString().toLowerCase(), ps1.text.trim().toString())

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
                            task?.result?.user?.let { user ->
                                auth.updateCurrentUser(
                                    user
                                )
                            }
                        }
                        startActivity(i)
                        finish()
                    } else {
                        Log.e("task messege", "failed")
                    }

                }}
}


