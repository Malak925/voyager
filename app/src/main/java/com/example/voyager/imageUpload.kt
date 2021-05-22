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
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_image_upload.*
import java.io.ByteArrayOutputStream


class imageUpload : AppCompatActivity() {
    val storageRef= Firebase.storage.reference.child("trips")
    var db = FirebaseFirestore.getInstance()

    private var images : ArrayList<Uri> = ArrayList()
    private var postion = 0;
    private var tripId= ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_upload)

        loadTripId()

      VwImg.setFactory { ImageView(applicationContext) }
      PkImg.setOnClickListener {
          if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
              if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)==
                      PackageManager.PERMISSION_DENIED){
                       val permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                       requestPermissions(permissions, PERMISSION_CODE)

              }
              else{
                  pickImageFromGallery()



              }


          }
          else{
              pickImageFromGallery()



          }




      }

      NxtImg.setOnClickListener {
          if (postion < images!!.size-1){
              postion++
              VwImg.setImageURI(images!![postion])

          }
          else{
              Toast.makeText(this, "No More Images",Toast.LENGTH_LONG).show()



          }


      }
      PreImg.setOnClickListener {
          if (postion > 0)
          {
              postion--
              VwImg.setImageURI(images!![postion])


          }
          else{
              Toast.makeText(this , "No More Images",Toast.LENGTH_LONG).show()


          }

      }

        UpImg.setOnClickListener {
            Log.d("Upload", "size: ${images?.size}")
            images?.forEach{imageUri ->
                Log.d("Upload", imageUri.toString())
                val imagesStorageRef= storageRef.child("$tripId/${System.currentTimeMillis()}")
                val bitmap = MediaStore.Images.Media
                    .getBitmap(
                        contentResolver,
                        imageUri)
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data = baos.toByteArray()

                var uploadTask = imagesStorageRef.putBytes(data)
                uploadTask.addOnFailureListener {
                    Log.d("Upload","Fail")
                }.addOnSuccessListener { taskSnapshot ->
                    Log.d("Upload","Success")
                }
            }


        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE , true )
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent,"select Images"), IMAGE_PICK_CODE)
    }

    companion object{
        private val IMAGE_PICK_CODE = 2021
        private val PERMISSION_CODE = 2022



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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode== IMAGE_PICK_CODE){
            if(resultCode==Activity.RESULT_OK){
                if (data!!.clipData!=null){
                    val count = data.clipData!!.itemCount
                    for (i in 0 until count){
                        val imageUri = data.clipData!!.getItemAt(i).uri
                        images?.add(imageUri)


                    }
                    VwImg.setImageURI(images!![0])
                    postion = 0




                }
                else {
                    val imageUri = data.data
                    VwImg.setImageURI(imageUri)
                    images?.add(imageUri!!)
                    postion =0



                }




            }






        }
            super.onActivityResult(requestCode, resultCode, data)

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

        return super.onOptionsItemSelected(item)
    }


    fun loadTripId() {
        db.collection("tripUsers")
            .whereEqualTo("userId", FirebaseAuth.getInstance().currentUser?.email)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful && task.result?.documents?.isEmpty()==false) {

                    task?.result?.documents?.get(0).let{
                        Log.d("TripResult",it?.get("tripId").toString())
                        tripId= it?.get("tripId").toString()
                    }
                } else {
                    Log.w("TAG", "Error getting documents.", task.exception)
                }
            }
    }
}
