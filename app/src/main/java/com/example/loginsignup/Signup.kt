package com.example.loginsignup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class SignUp : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        auth = FirebaseAuth.getInstance()
        var useremail = findViewById<EditText>(R.id.signupemail)
        var password= findViewById<EditText>(R.id.signuppass)
        var confirmpass = findViewById<EditText>(R.id.signupconfirm)

        var Signupbtn= findViewById<Button>(R.id.signup)

        Signupbtn?.setOnClickListener(){
            SignUpUser(useremail.text.toString().trim(),password.text.toString().trim(),confirmpass.text.toString().trim())
        }
    }
    fun SignUpUser( useremail:String,password:String,confirmpass:String){
        var error=""
        Log.i("info","check"+password.isEmpty())
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(useremail).matches()){
          error="Please enter valid email"
            Toast.makeText(applicationContext, error, Toast.LENGTH_LONG).show()
            return
        }
        else if(password==""){
            error="Please enter password"
            Toast.makeText(applicationContext, error, Toast.LENGTH_LONG).show()
            return
        }
        else if(confirmpass != password){
            error=" The passwords do not match"
            Toast.makeText(applicationContext, error, Toast.LENGTH_LONG).show()
            return
        }
         Log.i("info" ,"check")
        auth.createUserWithEmailAndPassword(useremail, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.i("info", "createUserWithEmail:success")

                    val currentUser = auth.currentUser

                    Toast.makeText(applicationContext,"Sign in Succesful",Toast.LENGTH_LONG).show()
                    val userIntent = Intent(this, userScreen::class.java)
                    userIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(userIntent)
                    finishAffinity()
//                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.i("info", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(applicationContext,task.exception.toString(),
                        Toast.LENGTH_SHORT).show()
//                    updateUI(null)
                }

                // ...
            }
    }


}