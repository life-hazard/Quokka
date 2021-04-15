package com.app.quokka.unlogged

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Toast
import com.app.quokka.R
import androidx.databinding.DataBindingUtil
import com.app.quokka.databinding.ActivitySignUpBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.app.quokka.util.User
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        Log.i("Opened", "SignUpActivity")

        binding.signUpButtonSignUpA.setOnClickListener { view ->
            showProgressBar()
            createDoc(view)
        }
    }

    override fun onStart() {
        super.onStart()
    }

    private fun createDoc(view: View) {
        hideProgressBar()

        if (!validateUser()) {
            Log.i("dblog", "invalid user")
            return
        }

        Log.i("dblog", "valid user")

        val name = binding.nameTextEditSignUpA.text.toString().trim()
        val surname = binding.surnameEditTextSignUpA.text.toString().trim()
        val email = binding.emailEditTextSignUpA.text.toString().trim()
        val password = binding.passwordEditTextSignUpA.text.toString()
        val address = binding.addressEditTextSignUpA.text.toString()
        val rating = mapOf("sum" to 0f, "divider" to 0f, "rating" to 0f)
        // user gets 100 points on start
        val points = 100

        val db = Firebase.firestore

        val user = User(name, surname, email, password, address, rating, points)

        db.collection("users").add(user).addOnSuccessListener { documentReference ->
            Log.d(TAG, "DocumentSnapshot written with ID: ${documentReference.id}")
            Log.i("dblog", "created document")
        }.addOnFailureListener { e ->
            Log.w(TAG, "Error adding document", e)
            Log.i("dblog", "error adding document")
        }

        Snackbar.make(view, "Snackbar: User created", Snackbar.LENGTH_LONG).show()

        Handler().postDelayed({ goToLogIn() }, 1000)
    }

    private fun goToLogIn() {
        val intent = Intent(this, LogInActivity::class.java)
        this.startActivity(intent)
    }

    private fun validateUser(): Boolean {
        var valid = true
        val name = binding.nameTextEditSignUpA.text.toString().trim()
        if (TextUtils.isEmpty(name)) {
            binding.nameTextEditSignUpA.error = "Required."
            valid = false
        } else {
            binding.nameTextEditSignUpA.error = null
        }

        val surname = binding.surnameEditTextSignUpA.text.toString().trim()
        if (TextUtils.isEmpty(surname)) {
            binding.surnameEditTextSignUpA.error = "Required."
            valid = false
        } else {
            binding.surnameEditTextSignUpA.error = null
        }

        val db = Firebase.firestore

        val email = binding.emailEditTextSignUpA.text.toString().trim()
        if (TextUtils.isEmpty(email)) {
            binding.emailEditTextSignUpA.error = "Required."
            valid = false
        } else {
            binding.emailEditTextSignUpA.error = null
        }

        val password = binding.passwordEditTextSignUpA.text.toString()
        val passwordCheck = binding.password2EditTextSignUpA.text.toString()
        if (TextUtils.isEmpty(password) && TextUtils.isEmpty(passwordCheck)) {
            binding.passwordEditTextSignUpAFrame.isPasswordVisibilityToggleEnabled = false
            binding.password2EditTextSignUpAFrame.isPasswordVisibilityToggleEnabled = false
            binding.passwordEditTextSignUpA.error = "Required."
            binding.password2EditTextSignUpA.error = "Required"
            valid = false
        } else {
            binding.passwordEditTextSignUpA.error = null
            if (password != passwordCheck) {
                binding.password2EditTextSignUpA.error = "Wrong password."
                valid = false
            } else {
                valid = true
            }
        }

        val address = binding.addressEditTextSignUpA.text.toString().trim()
        if (TextUtils.isEmpty(address)) {
            binding.addressEditTextSignUpA.error = "Required."
            valid = false
        } else {
            binding.addressEditTextSignUpA.error = null
        }

        return valid
    }

    private fun showProgressBar() {
        binding.progressBarSignUpA.visibility = View.VISIBLE
        binding.signUpButtonSignUpA.visibility = View.GONE
    }

    private fun hideProgressBar() {
        binding.progressBarSignUpA.visibility = View.GONE
        binding.signUpButtonSignUpA.visibility = View.VISIBLE
    }

    companion object {
        private const val TAG = "dblog"
    }
}