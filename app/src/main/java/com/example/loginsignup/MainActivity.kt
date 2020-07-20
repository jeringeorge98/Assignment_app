package com.example.loginsignup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.autofill.OnClickAction
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {
    private val user : FirebaseUser?

    init {
        user = FirebaseAuth.getInstance().currentUser
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(user?.email.toString()=="admin@admin.com"){
            val makeintent = Intent(this, AdminScreen::class.java)
            startActivity(makeintent)
            finish()
        }
        else if(user?.email != null){
            val makeAnotherIntent = Intent(this, userScreen::class.java)
            startActivity(makeAnotherIntent)
            finish()
        }

        setContentView(R.layout.activity_main)
        val toolbar =findViewById<Toolbar>(R.id.toolbar)

        setSupportActionBar(toolbar)


        val loginButton = findViewById<Button>(R.id.loginButton)
        val signupButton = findViewById<Button>(R.id.signupButton)

//        Click Listener for login button
        loginButton?.setOnClickListener() {
            val intentLoginScreen = Intent(this, LoginScreen::class.java)
            startActivity(intentLoginScreen)
        }
//        Click Listener for Signup Button
        signupButton?.setOnClickListener(){
           val intentSignupScreen = Intent(  this, SignUp::class.java)
            startActivity(intentSignupScreen)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var itemview= item.itemId
        when(itemview){
            R.id.sign_out -> Toast.makeText(applicationContext,"Clicked",Toast.LENGTH_LONG).show()
        }
        return false
    }
}