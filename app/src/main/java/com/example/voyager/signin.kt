package com.example.voyager

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_signin.*
import java.io.ByteArrayOutputStream


class signin : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var userID= ""
    private var firebaseStore: FirebaseStorage? = null
    private var storageRef= Firebase.storage.reference.child("users")
    var db = FirebaseFirestore.getInstance()

    val user: MutableMap<String, Any> = HashMap()
    private var imageUri: Uri? = null
    private var userDocumentId:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        auth = FirebaseAuth.getInstance()

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

            }
        }

        sg2.setOnClickListener {


            if (em2.text.trim ().toString().isNotEmpty() && ps1.text.trim ().toString().isNotEmpty()) {
                creatUser(em2.text.trim().toString().toLowerCase(), ps1.text.trim().toString())
            }
            else {
                Toast.makeText(this, "Please Enter values", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun creatUser(email: String, password: String){

// Add a new document with a generated ID
        user["Name"] = uname.text.toString()
        user["phone number"] = phno.text.toString()
        user["Email"] = em2.text.toString().toLowerCase()

// Add a new document with a generated ID
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.e("task message", "sucessful")
                        db.collection("users")
                            .add(user)
                            .addOnSuccessListener { documentReference ->
                                Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.id)
                                userDocumentId= documentReference.id
                            }
                            .addOnFailureListener { e -> Log.w("TAG", "Error adding document", e) }
                        if(imageUri!= null){
                            uploadImage()
                        }else{
                            goToMainActivity()
                        }

                    } else {
                        Log.e("task messege", "failed ${task.exception?.message}")
                    }
                }

    }


    private fun goToMainActivity(){
        var i = Intent(this, MainActivity::class.java)
        startActivity(i)
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
                else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"

       startActivityForResult(intent, IMAGE_PICK_CODE)


    }
  companion object{
      private val PERMISSION_CODE = 77
      private val IMAGE_PICK_CODE = 2021

  }

    private fun uploadImage(){
        val imagesStorageRef= storageRef?.child("${ FirebaseAuth.getInstance().currentUser?.email}")
        val bitmap = MediaStore.Images.Media
            .getBitmap(
                contentResolver,
                imageUri)
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        var uploadTask = imagesStorageRef?.putBytes(data)

        val urlTask = uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {exception->
                    Log.d("setImage","error: ${exception.message}")
                }
            }
            imagesStorageRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                Log.d("upload","DownloadUrl: $downloadUri")
                setUserImageUrl(downloadUri.toString())
            } else {
                Log.d("upload","Failed")
                goToMainActivity()
            }
        }

    }

    private fun setUserImageUrl(imageUrl:String){
        val imageMap:MutableMap<String, Any> = HashMap()
        imageMap["imageUrl"]= imageUrl
        db.collection("users").document(userDocumentId?:"")
            ?.update(imageMap).addOnCompleteListener {
                Log.d("setImage","image set success")
                goToMainActivity()
            }.addOnFailureListener { exception->
                Log.d("setImage","error: ${exception.message}")
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
       if (resultCode==Activity.RESULT_OK&&requestCode== IMAGE_PICK_CODE){
           imageUri= data?.data
           pic.setImageURI(imageUri)
       }

        super.onActivityResult(requestCode, resultCode, data)
    }

    fun loadTripId() {
        db.collection("tripUsers")
            .whereEqualTo("userId", FirebaseAuth.getInstance().currentUser?.email)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful && task.result?.documents?.isEmpty()==false) {

                    task?.result?.documents?.get(0).let{
                        Log.d("TripResult",it?.get("tripId").toString())
                        userID= it?.get("UserId").toString()
                    }
                } else {
                    Log.w("TAG", "Error getting documents.", task.exception)
                }
            }
}





}