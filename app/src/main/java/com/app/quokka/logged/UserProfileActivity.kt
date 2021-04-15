package com.app.quokka.logged

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.app.quokka.R
import com.app.quokka.databinding.ActivityUserProfileBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserProfileBinding
    var db = Firebase.firestore
    private val sharedPrefFile = "kotlinsharedpreference"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_profile)

        Log.i("Opened", "UserProfileActivity")
        val sharedPreferences: SharedPreferences =
            this.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("id_key", "default_value").toString()
        Log.d(TAG, "Current user id: $userId")

        intent = Intent(this, UserProfileEditActivity::class.java)

        db.collection("users").document(userId).get().addOnSuccessListener { document ->
            if (document != null) {
                Log.d(TAG, "The user is: ${document.data}")
                val userName = document.getString("name")
                val userSurname = document.getString("surname")
                val userEmail = document.getString("email")
                val userAddress = document.getString("address")

                val r = document.data?.get("rating").toString()
                Log.d("showRating", "The string of rating: ${r.substring(1, r.length - 1)}")

                val ratingMap = r.substring(1, r.length - 1).split(", ").associate {
                    val (left, right) = it.split("=")
                    left to right.toFloat()
                }
                val rate = ratingMap.getValue("rating")

                Log.d("showRATING", "The rating is: $ratingMap")
                val userRating = ratingMap.getValue("rating")
                val userPoints = document.getLong("points")?.toInt()

                binding.userNameText.setText(userName)
                binding.userSurnameText.setText(userSurname)
                binding.userEmailText.setText(userEmail)
                binding.numberOfPoints.text = userPoints.toString()
                binding.UserRatingBar.rating = rate


                intent.putExtra("oldName", userName)
                intent.putExtra("oldSurname", userSurname)
                intent.putExtra("oldEmail", userEmail)
                intent.putExtra("rating", rate)
            }
        }

        binding.editProfileButton.setOnClickListener {

            this.startActivity(intent)

        }

        binding.root
    }

    override fun onBackPressed() {
        val intent = Intent(this, UserHomePageActivity::class.java)
        startActivity(intent)
    }

    companion object {
        const val TAG = "userProfile"
    }
}