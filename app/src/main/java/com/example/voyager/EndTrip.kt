package com.example.voyager

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_end_trip.*
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class EndTrip : AppCompatActivity() {
    private var db = FirebaseFirestore.getInstance()
    private var tripId=""
    private var tripName=""
    private val imagesList= ArrayList<String>()
    private val storage= Firebase.storage
    private var downloadedCount= 0
    private var documentId="";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_end_trip)
        endtrip.setOnClickListener {
            loadTripId()
        }
    }

    private fun loadTripId(){
        db.collection("tripUsers")
            .whereEqualTo("userId",FirebaseAuth.getInstance().currentUser?.email)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful && task.result?.documents?.isEmpty()==false) {
                    task?.result?.documents?.get(0).let{
                        Log.d("TripResult",it?.get("tripId").toString())
                        tripId= it?.get("tripId").toString()
                        tripName= it?.get("trip name").toString()
                        documentId= it?.id?:""

                        loadImages()
                    }
                } else {
                    Log.w("TAG", "Error getting documents.", task.exception)
                }
            }
    }

    private fun loadImages() {
        db.collection("images")
            .whereEqualTo("tripId", tripId)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful && task.result?.documents?.isEmpty() == false) {
                    Log.d("Images", task?.result?.toString()?:"null")

                    task?.result?.documents?.forEach { document ->
                        val imageItem= document?.get("imageUrl").toString()
                        imagesList.add(imageItem)
                    }
                    checkPermission()
                } else {
                    Log.w("TAG", "Error getting documents.", task.exception)
                }
            }
    }

    private fun checkPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                PackageManager.PERMISSION_DENIED){
                val permissions = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                requestPermissions(permissions, PERMISSION_CODE)
            }
            else{
                downloadImages()
            }
        }
        else{
            downloadImages()

        }
    }
    private fun downloadImages(){
        val dir = File(Environment.getExternalStorageDirectory().toString() + "/voyager/trip - $tripName/photos")
        Log.d("DownloadImages", "total count: ${imagesList.size}")
        imagesList.forEach { imageUrl->
            val file = File(dir,"${ UUID.randomUUID().toString()}.jpg")
            try {
                if (!dir.exists()) {
                    val isCreated= dir.mkdirs()
                    Log.d("UploadImage","isCreated: $isCreated")
                }
                file.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            val storageRef= storage.getReferenceFromUrl(imageUrl)
            storageRef.getFile(file)
                .addOnSuccessListener {
                    Log.d("DownloadImages", "$imageUrl finished, total downloaded count: ${++downloadedCount}")
                    if(downloadedCount== imagesList.size){
                        Toast.makeText(this,"Download Complete.. thank you", Toast.LENGTH_SHORT).show()
                        removeUserFromTrip()
                    }
                }.addOnFailureListener {
                    Log.d("DownloadImages", "$imageUrl failed, total downloaded count: ${++downloadedCount}")
                    Toast.makeText(this,"Download Complete.. thank you", Toast.LENGTH_SHORT).show()
                    if(downloadedCount== imagesList.size){
                        removeUserFromTrip()
                    }
                }
        }
    }

    private fun removeUserFromTrip(){
        db.collection("tripUsers")
            .document(documentId)
            .delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("TAG", "documents removed.")
                    // todo action
                } else {
                    Log.w("TAG", "Error getting documents.", task.exception)
                }
            }
    }

    companion object{
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
                    downloadImages()
                }
                else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}