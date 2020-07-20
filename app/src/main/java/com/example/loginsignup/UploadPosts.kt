package com.example.loginsignup

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class UploadPosts : AppCompatActivity() {
    private lateinit var db:FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
         db= FirebaseFirestore.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_posts)

        // getting post data
        var data=findViewById<EditText>(R.id.UploadText)
        var SaveBtn= findViewById<Button>(R.id.Uploadbutton)


        // save button on click listener
        SaveBtn.setOnClickListener(){

            Log.i("info", "datais"+data)

           Upload(data.text.toString().trim())

        }
    }

//    override fun onBackPressed() {
////        var intentGoBack = Intent(this, AdminScreen::class.java)
////        startActivity(intentGoBack)
//        finishActivity(Activity.RESULT_OK)
//    }

    fun Upload(data:String){
        var likes= arrayListOf<String>()
        var comments= arrayListOf<String>()
        var date= Date()
        Log.i("info","date"+date)
        if(data.isEmpty()){
            Toast.makeText(applicationContext,"Please fill in the fields",Toast.LENGTH_LONG).show()
            return
        }
  val post= hashMapOf<String,Any>()
        post["data"] = data
        post["likes"]= likes
        post["comments"]= comments
        post["Date"]=date
        db.collection("/posts")
            .add(post)
            .addOnSuccessListener { documentReference ->
                Log.i("info", "DocumentSnapshot added with ID: ${documentReference.id}")
                Toast.makeText(applicationContext,"Succesfully uploaded",Toast.LENGTH_LONG).show()

                var returnIntent = Intent()
                setResult(Activity.RESULT_OK, returnIntent)
                finish()
//                finishActivity(Activity.RESULT_OK)

            }
            .addOnFailureListener { e ->
                Toast.makeText(applicationContext, "Error uploading post", Toast.LENGTH_LONG)
            }

    }

}

class User{

}