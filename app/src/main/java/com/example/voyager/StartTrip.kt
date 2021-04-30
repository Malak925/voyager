package com.example.voyager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_start_trip.*

class StartTrip : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_trip)
        var st = Intent(this,CreateTrip::class.java)
        var jn =Intent(this , JoinAtrip::class.java)

        stTrip.setOnClickListener {
            startActivity(st)


        }
     jnTrip.setOnClickListener {


         startActivity(jn)
     }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
       menuInflater.inflate(R.menu.voymen,menu)


        return super.onCreateOptionsMenu(menu)
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
        return super.onOptionsItemSelected(item)
    }
}