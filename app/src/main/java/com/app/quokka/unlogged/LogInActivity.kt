package com.app.quokka.unlogged

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.app.quokka.R
import com.app.quokka.databinding.ActivityLogInBinding
import com.app.quokka.logged.UserHomePageActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.android.material.snackbar.Snackbar

class LogInActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLogInBinding
    private lateinit var db: FirebaseFirestore

    // Shared preferences
    private val sharedPrefFile = "kotlinsharedpreference"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_log_in)
        Log.i("Opened", "LogInActivity")

        // Intent for SignUpActivity
        val intent1 = Intent(this, SignUpActivity::class.java)

        val sharedPreferences: SharedPreferences =
            this.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)

        binding.logInButton.setOnClickListener {
            logIn()
            val sharedIdValue = sharedPreferences.getString("id_key", "default_value")
            Log.i("login", "Shared Id value: ${sharedIdValue.toString()}")
        }

        binding.signUpButton.setOnClickListener {
            // Go to SignUpActivity
            this.startActivity(intent1)
        }
    }

    private fun logIn() {
        // Read input
        val db = Firebase.firestore

        val email = binding.emailEdit.text.toString().trim()
        val password = binding.passwordEdit.text.toString()
        var sharedUserId = " "

        val intent = Intent(this, UserHomePageActivity::class.java)

        db.collection("users").whereEqualTo("email", email)
            .whereEqualTo("password", password)
            .get()
            .addOnSuccessListener { documents ->
                Log.i(TAG, "Empty document looks like that: ${documents.isEmpty}")
                if (documents.isEmpty) {
                    Snackbar.make(binding.root, "Wrong Email or Password", Snackbar.LENGTH_LONG)
                        .show()
                } else {
                    for (document in documents) {
                        Log.d(TAG, "${document.id} => $document.data")
                        //val dbPassword = document.getString("password")
                        sharedUserId = document.id

                        // Setting user id into shared preferences so that its available always:
                        // Set a shared preference
                        val sharedPreferences: SharedPreferences =
                            this.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
                        val editor: SharedPreferences.Editor = sharedPreferences.edit()
                        editor.putString("id_key", sharedUserId)
                        editor.apply()
                        editor.commit()

                        Log.d(TAG, "The user ID in shared preferences: $sharedUserId ")

                        Log.d(TAG, sharedUserId)
                        this.startActivity(intent)
                    }
                }
            }.addOnFailureListener { e ->
                Log.w(TAG, "Error getting documents: ", e)
                Toast.makeText(this, "Error accessing the database.", Toast.LENGTH_SHORT).show()
            }

    }

    companion object {
        val TAG = "login"
    }
}