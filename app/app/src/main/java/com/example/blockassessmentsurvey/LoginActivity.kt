package com.example.blockassessmentsurvey

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {
    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var userEmail: EditText? = null
    private var userPassword: EditText? = null
    private var loginBtn: Button? = null
    private var progressBar: ProgressBar? = null

    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)



        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference!!.child("Users")
        mAuth = FirebaseAuth.getInstance()

        initializeUI()

        loginBtn!!.setOnClickListener { loginUserAccount() }
    }

    private fun loginUserAccount() {
        progressBar!!.visibility = View.VISIBLE

        // Todo : Retrieve eamil and password, make sure it's not empty
        val email_string: String = userEmail!!.text.toString()
        val pass_string: String = userPassword!!.text.toString()
        if (TextUtils.isEmpty(email_string)) {
            Toast.makeText(applicationContext, "Please enter email...", Toast.LENGTH_LONG).show()
            return
        }
        if (TextUtils.isEmpty(pass_string)) {
            Toast.makeText(applicationContext, "Please enter password!", Toast.LENGTH_LONG).show()
            return
        }

        // Todo : Sign in with given Email and Password
        // Retrieve UID for Current User if Login successful and store in intent, for the key UserID
        // Start Intent DashboardActivity if Registration Successful
        mAuth!!.signInWithEmailAndPassword(email_string, pass_string)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val curr_uid = mAuth!!.currentUser!!.uid
                    //val curr_uid = mDatabaseReference!!.push().key
                    Log.i("Lab-Fire", "Sending $curr_uid")
                    val intent = Intent(this@LoginActivity, GpsLocationActivity::class.java)
                    intent.putExtra("UserID", curr_uid)
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Login failed! Please try again later",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

    }

    private fun initializeUI() {
        userEmail = findViewById(R.id.email)
        userPassword = findViewById(R.id.password)

        loginBtn = findViewById(R.id.login)
        progressBar = findViewById(R.id.progressBar)
    }

    companion object {
        val UserMail = "com.example.tesla.myhomelibrary.UMail"
        val UserID = "com.example.tesla.myhomelibrary.UID"

    }
}