package com.example.voyager

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_code_gen.*


class CodeGen : AppCompatActivity() {
    var db = FirebaseFirestore.getInstance()

    private var myClipboard: ClipboardManager? = null
    private var myClip: ClipData? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_code_gen)
        val tripID= intent.getStringExtra(KEY_TRIP_ID)

        myClipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val Code: HashMap<String, String?> = HashMap()


        var code: String

        genbtn.setOnClickListener {
            val alphabet: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')


            val randomString: String = List(8) { alphabet.random() }.joinToString("")
            genCod.text = randomString

            code = randomString
            Code["code"] = code
            Code["tripID"] = tripID

            db.collection("Code")
                .add(Code)
                .addOnSuccessListener { documentReference ->
                    Log.d(
                        "TAG",
                        "DocumentSnapshot added with ID: " + documentReference.id
                    )
                }
                .addOnFailureListener { e -> Log.w("TAG", "Error adding document", e) }

        }
        copy.setOnClickListener {


            ClipData.newPlainText("text", genCod.text);
            myClip?.let { it1 -> myClipboard?.setPrimaryClip(it1) };

            Toast.makeText(this, "Text Copied", Toast.LENGTH_SHORT).show();


        }

    }

    companion object{
        public const val KEY_TRIP_ID= "keyTripID"
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
        else if (item.itemId == R.id.bills) {
            startActivity(Intent(this, BillMenu::class.java))


        }
        else if (item.itemId == R.id.end) {

            startActivity(Intent(this, EndTrip::class.java))



        }

        return super.onOptionsItemSelected(item)
    }


}