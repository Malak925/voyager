package com.example.voyager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_bill_menu.*

class BillMenu : AppCompatActivity() {
    var db = FirebaseFirestore.getInstance()
    val Bills = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bill_menu)
     loadTripBill()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.perpro) {

            startActivity(Intent(this, personal_profile::class.java))
        } else if (item.itemId == R.id.tripro) {

            startActivity(Intent(this, trip_info::class.java))
        } else if (item.itemId == R.id.UpImg) {
            startActivity(Intent(this, imageUpload::class.java))


        } else if (item.itemId == R.id.Bill) {
            startActivity(Intent(this, Bill::class.java))


        } else if (item.itemId == R.id.parLst) {
            startActivity(Intent(this, participants::class.java))


        }else if (item.itemId == R.id.bills) {
            startActivity(Intent(this, BillMenu::class.java))


        }
        else if (item.itemId == R.id.end) {

            startActivity(Intent(this, EndTrip::class.java))



        }


        return super.onOptionsItemSelected(item)
    }


    fun loadTripBill() {
        db.collection("Bills")
            .whereEqualTo("/00", FirebaseAuth.getInstance().currentUser?.email)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful && task.result?.documents?.isEmpty() == false) {

                    task?.result?.documents?.get(0).let {
                        Log.d("TripResult", it?.get("tripId").toString())

                        val tripId = it?.get("tripId").toString()
                        loadBills(tripId)
                    }
                } else {
                    Log.w("TAG", "Error getting documents.", task.exception)
                }
            }
    }

    private fun loadBills(tripId: String) {
        db.collection("tripBills")
            .whereEqualTo("tripId", tripId)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task?.result?.documents?.let { documents ->

                        documents.forEach { documentSnapshot ->
                            Bills.add(documentSnapshot.get("tripBills").toString())
                        }
                        setRecyclerView()
                    }
                } else {
                    Log.w("TAG", "Error getting documents.", task.exception)
                }
            }


    }

        private fun setRecyclerView() {

          val list = findViewById<ListView>(R.id.bills)
            var adapter = ArrayAdapter<String>(this , android.R.layout.simple_list_item_1,Bills )
            list.adapter=adapter

            list.setOnItemClickListener(){parent , view , position , id ->
                Toast.makeText(this, "Item  ${Bills[position]} +Clicked",Toast.LENGTH_SHORT).show()
                val intent= Intent(this, personal_profile::class.java)
                intent.putExtra("email",Bills[position])
            }
        }
    }
