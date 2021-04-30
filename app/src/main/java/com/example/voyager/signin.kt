package com.example.voyager

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_personal_profile.*
import kotlinx.android.synthetic.main.activity_signin.*


class signin : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    var db = FirebaseFirestore.getInstance()

    val user: MutableMap<String, Any> = HashMap()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        auth = FirebaseAuth.getInstance()
        var u = Intent(MediaStore.ACTION_IMAGE_CAPTURE)


        storageReference = FirebaseStorage.getInstance().reference




        pic.setOnClickListener {
startActivityForResult(u,2021)

}

        sg2.setOnClickListener {


            if (em2.text.trim ().toString().isNotEmpty() && ps1.text.trim ().toString().isNotEmpty()) {

                creatUser(em2.text.trim().toString(), ps1.text.trim().toString())

            }
            else {

                Toast.makeText(this, "Please Enter values", Toast.LENGTH_LONG).show()
                        }


            }

        }
    fun creatUser(email: String, password: String){

               var i = Intent(this, MainActivity::class.java)


// Add a new document with a generated ID
        user["Name"] = uname.text.toString()
        user["phone number"] = phno.text.toString()
        user["Email"] = em2.text.toString()

// Add a new document with a generated ID


        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.e("task message", "sucessful")
                        db.collection("users")
                                .add(user)
                                .addOnSuccessListener { documentReference -> Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.id) }
                                .addOnFailureListener { e -> Log.w("TAG", "Error adding document", e) }
                        startActivity(i)

                    } else {
                        Log.e("task messege", "failed")


                    }

                }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
      if (requestCode == 2021 )
      {
          if (resultCode== RESULT_OK)
          {
              var ph = data?.extras?.get("data") as Bitmap
              pic.setImageBitmap(ph)


          }



      }



        super.onActivityResult(requestCode, resultCode, data)
    }


}