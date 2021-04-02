package com.app.quokka.logged

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.app.quokka.R
import com.app.quokka.databinding.ActivityUserProfileEditBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_user_profile.*

class UserProfileEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserProfileEditBinding
    var db = Firebase.firestore
    private val sharedPrefFile = "kotlinsharedpreference"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_profile_edit)


        val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("id_key", "default_value").toString()
        Log.d(TAG, "Current user id: $userId")
        Log.i(TAG, "Current user id: $userId")

        val oldName = intent.getStringExtra("oldName")
        val oldSurname = intent.getStringExtra("oldSurname")
        val oldEmail = intent.getStringExtra("oldEmail")

        binding.userNameEdit.hint = oldName
        binding.userSurnameEdit.hint = oldSurname
        binding.userEmailEdit.hint = oldEmail

        binding.userNameEdit.setOnFocusChangeListener() { _, hasFocus ->
            if(hasFocus) {
                binding.userNameEditFrame.hint = "Name"
            } else {
                binding.userNameEditFrame.hint = oldName
            }
        }

        binding.userSurnameEdit.setOnFocusChangeListener() {_, hasFocus ->
            if(hasFocus) {
                binding.userSurnameEditFrame.hint = "Surname"
            } else {
                binding.userSurnameEditFrame.hint = oldSurname
            }
        }

        binding.userEmailEdit.setOnFocusChangeListener() {_, hasFocus ->
            if(hasFocus) {
                binding.userEmailEditFrame.hint = "E-mail"
            } else {
                binding.userEmailEditFrame.hint = oldEmail
            }
        }

        val userDoc = db.collection("users").document(userId)

        db.collection("users").document(userId).get().addOnSuccessListener { document ->
            if (document != null) {
                val points = document.data?.getValue("points").toString()
                binding.numberOfPoints.text = points
            }
        }

        binding.saveProfileButton.setOnClickListener {

            val newName = binding.userNameEdit.text.toString()
            val newSurname = binding.userSurnameEdit.text.toString()
            val newEmail = binding.userEmailEdit.text.toString()
            val newPassword = binding.userPasswordEdit.text.toString()

            if (newName.isNotEmpty()) {
                Log.d(TAG, "New Name: $newName")
                Log.i(TAG, "New Name: $newName")
                db.collection("users").document(userId).update("name", newName).addOnSuccessListener { document ->
                    Log.d(TAG, "Document Updated: $document")
                }.addOnFailureListener { e ->
                    Log.d(TAG, "Error occurred: $e")
                }

            }

            if (newSurname.isNotEmpty()) {
                Log.d(TAG, "New Surname: $newSurname")
                userDoc.update("surname", newSurname)
            }

            if (newEmail.isNotEmpty()) {
                Log.d(TAG, "New Email: $newEmail")
                userDoc.update("email", newEmail)

            }
            // TODO check if it works
            if (newPassword.isNotEmpty()) {
                Log.d(TAG, "New Password")
                userDoc.update("password", newPassword)
            }
//
//            db.collection("users").document(userId).get().addOnSuccessListener { document ->
//                if (document != null) {
//                    val points = document.data?.getValue("points").toString()
//                    binding.numberOfPoints.text = points
//                }
//            }

            intent = Intent(this, UserProfileActivity::class.java)
            this.startActivity(intent)
        }

        binding.root
    }

    companion object {
        const val TAG = "editProfile"
    }
}