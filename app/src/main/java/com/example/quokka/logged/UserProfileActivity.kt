package com.example.quokka.logged

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.quokka.R
import com.example.quokka.databinding.ActivityUserProfileBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserProfileBinding
    var db = Firebase.firestore
    private val sharedPrefFile = "kotlinsharedpreference"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_profile)

        // TODO add editing

        val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("id_key", "default_value").toString()
        Log.d(UserProfile.TAG, "Current user id: $userId")

        intent = Intent(this, UserProfileEditActivity::class.java)

        db.collection("users").document(userId).get().addOnSuccessListener { document ->
            if(document != null) {
                Log.d(UserProfile.TAG, "The user is: ${document.data}")
                val userName = document.getString("name")
                val userSurname = document.getString("surname")
                val userEmail = document.getString("email")
                val userAddress = document.getString("address")
                val userRating = document.getLong("rating")?.toInt()
                val userPoints = document.getLong("points")?.toInt()

                binding.userNameText.setText(userName)
                binding.userSurnameText.setText(userSurname)
                binding.userEmailText.setText(userEmail)
                binding.numberOfPoints.text = userPoints.toString()
                if (userRating != null) {
                    binding.UserRatingBar.rating = userRating.toFloat()
                }

                intent.putExtra("oldName", userName)
                intent.putExtra("oldSurname", userSurname)
                intent.putExtra("oldEmail", userEmail)
            }
        }
//
//        val name = binding.userName.text
//        val surname = binding.userSurname.text
//        val email = binding.userEmailAddress.text
//        Log.d(TAG, "Current user: $name, $surname, $email")

        binding.editProfileButton.setOnClickListener {

//            intent = Intent(this, UserProfileEditActivity::class.java)
            this.startActivity(intent)
//            binding.editProfileButton.visibility = View.GONE
//            binding.saveProfileButton.visibility = View.VISIBLE
//
//            binding.userName.visibility = View.GONE
//            binding.userNameEdit.visibility = View.VISIBLE
//
//            binding.userSurname.visibility = View.GONE
//            binding.userSurnameEdit.visibility = View.VISIBLE
//
//            binding.userEmailAddress.visibility = View.GONE
//            binding.userEmailAddressEdit.visibility = View.VISIBLE

            //updateUserInfo()
        }

        binding.root
    }

    fun updateUserInfo() {
        intent = Intent(this, UserProfileEditActivity::class.java)
        this.startActivity(intent)
//        val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
//        val userId = sharedPreferences.getString("id_key", "default_value").toString()
//
//        binding.editProfileButton.visibility = View.GONE
//        binding.saveProfileButton.visibility = View.VISIBLE
//
//        binding.userName.visibility = View.GONE
//        binding.userNameEdit.visibility = View.VISIBLE
//
//        binding.userSurname.visibility = View.GONE
//        binding.userSurnameEdit.visibility = View.VISIBLE
//
//        binding.userEmailAddress.visibility = View.GONE
//        binding.userEmailAddressEdit.visibility = View.VISIBLE

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
//                val newName = binding.userNameEdit.text
//                val newSurname = binding.userSurnameEdit.text
//                val newEmail = binding.userEmailAddressEdit.text
//                val newAddress: String
//
//                binding.saveProfileButton.setOnClickListener {
//                if(!newName.equals(userName)) {
//                    db.collection("users").document(userId).update("name", newName)
//                }
//                if(!newSurname.equals(userSurname)) {
//                    db.collection("users").document(userId).update("surname", newSurname)
//                }
//                if(!newEmail.equals(userEmail)) {
//                    db.collection("users").document(userId).update("email", newEmail)
//                }
//                    Toast.makeText(this, "Profile saved", Toast.LENGTH_SHORT).show()
//                    binding.userNameEdit.visibility = View.GONE
//                    binding.userName.visibility = View.VISIBLE
//                }
//            }
//        }

    }

    companion object {
        const val TAG = "userProfile"
    }
}