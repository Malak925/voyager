package com.example.voyager

import android.app.Activity
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.Intent.ACTION_PICK
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.ACTION_IMAGE_CAPTURE
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_personal_profile.*
import kotlinx.android.synthetic.main.activity_signin.*
import kotlinx.android.synthetic.main.activity_trip_info.*


class signin : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    var db = FirebaseFirestore.getInstance()

    val user: MutableMap<String, Any> = HashMap()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        auth = FirebaseAuth.getInstance()


        storageReference = FirebaseStorage.getInstance().reference




        pic.setOnClickListener {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)==
                    PackageManager.PERMISSION_DENIED){
                    val permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permissions,PERMISSION_CODE)

                }
                else{
                    pickImageFromGallery()



                }


            }
            else{
                pickImageFromGallery()



            }}

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




    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        when(requestCode){
           PERMISSION_CODE -> {
                if(grantResults.size>0&&grantResults[0]==
                    PackageManager.PERMISSION_GRANTED){
                    pickImageFromGallery()

                }
                else{
                    Toast.makeText(this ,"Permission Denied",Toast.LENGTH_LONG ).show()




                }


            }



        }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent,
          IMAGE_PICK_CODE
        )
    }
  companion object{
      private val PERMISSION_CODE = 77
      private val IMAGE_PICK_CODE = 2021



  }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
       if (resultCode==Activity.RESULT_OK&&requestCode== IMAGE_PICK_CODE){
           pic.setImageURI(data?.data)


       }



        super.onActivityResult(requestCode, resultCode, data)
    }
}