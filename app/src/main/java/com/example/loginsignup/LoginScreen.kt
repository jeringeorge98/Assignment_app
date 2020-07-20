package com.example.loginsignup

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthMultiFactorException
import com.google.firebase.auth.FirebaseUser
//import com.google.firebase.auth.ktx.auth
//import com.google.firebase.ktx.Firebase
//import com.google.firebase.quickstart.auth.R


class LoginScreen : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)

        val newIntent : Intent
        auth = FirebaseAuth.getInstance()
//        Email edit text
        var email = findViewById<EditText>(R.id.email).text
        var pwd = findViewById<EditText>(R.id.password).text


//        Login button
        var LoginBtn = findViewById<Button>(R.id.login)
        LoginBtn?.setOnClickListener() {
            LoginFunc(email.toString().trim(), pwd.toString().trim())
        }
    }

    fun LoginFunc(email: String, pwd: String){
        Log.i("info", email)
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(applicationContext, "Please enter a valid email", Toast.LENGTH_LONG).show()
            return
        }
        else if(pwd==""){
            val error="Please enter password"
            Toast.makeText(applicationContext, error, Toast.LENGTH_LONG).show()
            return
        }
        else{
            auth.signInWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(this) { task ->
                    if(task.isSuccessful){
                        Log.i("info", "success")
                        Toast.makeText(applicationContext, "Login Successful", Toast.LENGTH_LONG).show()

                        var user = FirebaseAuth.getInstance().currentUser
                        if(user?.email.toString().equals("admin@admin.com")){
                            val newInt = Intent(this, AdminScreen::class.java)
                            newInt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(newInt)
                            finishAffinity()
                        }
                        else{
                            val newerInt = Intent(this, userScreen::class.java)
                            newerInt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(newerInt)
                            finishAffinity()
                        }
                    }
                    else {
                        Log.i("info", task.exception.toString())
                        Toast.makeText(applicationContext, "Incorrect email or password", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }



}