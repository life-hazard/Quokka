package com.example.quokka.logged

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

        db.collection("users").document(userId).get().addOnSuccessListener { document ->
            if(document != null) {
                Log.d(UserProfile.TAG, "The user is: ${document.data}")
                val userName = document.getString("name")
                val userSurname = document.getString("surname")
                val userEmail = document.getString("email")
                val userAddress = document.getString("address")
                val userRating = document.getLong("rating")?.toInt()
                val userPoints = document.getLong("points")?.toInt()

                binding.userName.text = userName
                binding.userSurname.text = userSurname
                binding.userEmailAddress.text = userEmail
                binding.numberOfPoints.text = userPoints.toString()
                if (userRating != null) {
                    binding.UserRatingBar.rating = userRating.toFloat()
                }
            }

        }

        binding.root
    }
    companion object {
        const val TAG = "userProfile"
    }
}