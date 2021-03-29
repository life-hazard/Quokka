package com.example.quokka.unlogged

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.quokka.R
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.quokka.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthMultiFactorException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.MultiFactor
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.lang.Exception
import com.example.quokka.util.User
import com.google.android.gms.common.api.internal.GoogleServices.initialize
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    private lateinit var auth: FirebaseAuth

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_sign_up)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)

        auth = Firebase.auth

        binding.signUpButtonSignUpA.setOnClickListener {
            //insertDataToDatabase()
            showProgressBar()

            createDoc()
        }
    }

    override fun onStart() {
        super.onStart()
        //val currentUser = auth.currentUser
        //if (currentUser != null) {
        //    reload()
        //}
    }

    private fun createDoc() {
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
        val rating = -1
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
    }

    private fun reload() {
        auth.currentUser!!.reload().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                updateUI(auth.currentUser)
                Toast.makeText(this, "Reload Successful!", Toast.LENGTH_SHORT).show()
            } else {
                Log.e("reload", task.exception.toString())
                Toast.makeText(this, "Failed to load user.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        //update what user sees if reloaded
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
//            val isEmpty = db.collection("users").whereEqualTo("email", email).get().result?.isEmpty
//            if (isEmpty == true) {
//                Log.i(TAG, "There are no users with this mail: $isEmpty")
                binding.emailEditTextSignUpA.error = null
//            } else {
//                Log.i(TAG, "User already in db: $isEmpty")
//                binding.emailEditTextSignUpA.error = "User with this email already exists"
//                valid = false
//            }
            // If email in db
//            val db = Firebase.firestore
//
//            db.collection("users").whereEqualTo("email", email).get().addOnCompleteListener {document ->
//                if (!document.result?.isEmpty!!) {
//                    Log.i(TAG, "User already in db: ${document.toString()}")
//                    binding.emailEditTextSignUpA.error = "User with this email already exists"
//                    valid = false
//                } else {
//                    Log.i(TAG, "There are no users with this mail: $document")
//                    binding.emailEditTextSignUpA.error = null
//
//                }
//            }
        }

        val password = binding.passwordEditTextSignUpA.text.toString()
        val passwordCheck = binding.password2EditTextSignUpA.text.toString()
        if (TextUtils.isEmpty(password) && TextUtils.isEmpty(passwordCheck)) {
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


    private fun createAccount(
        name: String,
        username: String,
        email: String,
        password: String,
        passwordCheck: String,
        address: String,
        rating: Int
    ) {
        Log.d(ContentValues.TAG, "createAccount:$email")
        if (!validateUser()) {
            return
        }
        showProgressBar()
        //sign up
        hideProgressBar()
    }

    private fun signIn(email: String, password: String) {
        Log.d(TAG, "signIn:$email")
        if (!validateUser()) {
            return
        }
        showProgressBar()
        // sign in
        hideProgressBar()

    }

//    private fun checkForMultiFactorFailure(e: Exception) {
//        if (e is FirebaseAuthMultiFactorException) {
//            Log.w(TAG, "multiFactorFailure", e)
//            val intent = Intent()
//            val resolver = e.resolver
//            intent.putExtra("EXTRA_MFA_RESOLVER", resolver)
//            setResult(MultiFactorActivity.RESULT_NEEDS_MFA_SIGN_IN, intent)
//            finish()
//        }
//    }

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