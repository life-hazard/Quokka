package com.example.quokka.logged

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.quokka.R
import com.example.quokka.databinding.ActivityUserProfileBinding
import com.example.quokka.databinding.ActivityUserProfileEditBinding
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
        binding.userEmailAddressEdit.hint = oldEmail

        val userDoc = db.collection("users").document(userId)

        binding.saveProfileButton.setOnClickListener {

            val newName = binding.userNameEdit.text.toString()
            val newSurname = binding.userSurnameEdit.text.toString()
            val newEmail = binding.userEmailAddressEdit.text.toString()

            if (newName.isNotEmpty()) {
                Log.d(TAG, "New Name: $newName")
                Log.i(TAG, "New Name: $newName")
                db.collection("users").document(userId).update("name", newName).addOnSuccessListener { document ->
                    Log.d(TAG, "Document Updated: $document")
                }.addOnFailureListener { e ->
                    Log.d(TAG, "Error occurred: $e")
                }
                // TODO SHOULD UPDATE BUT DOESN'T

//                userDoc.get().addOnSuccessListener { document ->
//                    if (document != null) {
//                        Log.d(TAG, "I got a new document!! ${document.data}")
//                    }
//                }
            }

            if (newSurname.isNotEmpty()) {
                Log.d(TAG, "New Surname: $newSurname")
                userDoc.update("surname", newSurname)
                // TODO SHOULD UPDATE BUT DOESN'T
            }

            if (newEmail.isNotEmpty()) {
                Log.d(TAG, "New Email: $newEmail")
                userDoc.update("email", newEmail)
                // TODO SHOULD UPDATE BUT DOESN'T

            }

            intent = Intent(this, UserProfileActivity::class.java)
            this.startActivity(intent)
        }

//        db.collection("users").document(userId).get().addOnSuccessListener { document ->
//            if(document != null) {
//                Log.d(UserProfile.TAG, "The user is: ${document.data}")
//                val userName = document.getString("name")
//                val userSurname = document.getString("surname")
//                val userEmail = document.getString("email")
//                val userAddress = document.getString("address")
//
//                binding.userNameEdit.hint = userName
//                binding.userSurnameEdit.hint = userSurname
//                binding.userEmailAddressEdit.hint = userEmail
//
//                binding.saveProfileButton.setOnClickListener {
//                if(!newName.equals(oldName)) {
//                    db.collection("users").document(userId).update("name", newName).addOnSuccessListener { document ->
//                        Log.d(TAG, "User updated with name: $document")
//                    }
//                }
//                if(!newSurname.equals(oldSurname)) {
//                    db.collection("users").document(userId).update("surname", newSurname).addOnSuccessListener { document ->
//                        Log.d(TAG, "User updated with surname: $document")
//                    }
//                }
//                if(!newEmail.equals(oldEmail)) {
//                    db.collection("users").document(userId).update("email", newEmail).addOnSuccessListener { document ->
//                        Log.d(TAG, "User updated with email: $document")
//                    }
//                }
//                    Toast.makeText(this, "Profile saved", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }


//        binding.saveProfileButton.setOnClickListener {
//            intent = Intent(this, UserProfileActivity::class.java)
//            this.startActivity(intent)
//        }

        binding.root
    }

    companion object {
        const val TAG = "editProfile"
    }
}