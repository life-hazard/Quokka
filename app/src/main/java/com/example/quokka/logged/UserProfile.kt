package com.example.quokka.logged

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.quokka.R
import com.example.quokka.databinding.FragmentUserProfileBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


/**
 * A simple [Fragment] subclass.
 * Use the [UserProfile.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserProfile : Fragment() {

    private lateinit var binding: FragmentUserProfileBinding
    var db = Firebase.firestore
    private val sharedPrefFile = "kotlinsharedpreference"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentUserProfileBinding>(inflater,
            R.layout.fragment_user_profile, container, false)

        val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("id_key", "default_value").toString()
        Log.d(TAG, "Current user id: $userId")

        db.collection("users").document(userId).get().addOnSuccessListener { document ->
            if(document != null) {
                Log.d(TAG, "The user is: ${document.data}")
                val userName = document.getString("name")
                val userSurname = document.getString("surname")
                val userEmail = document.getString("email")
                val userAddress = document.getString("address")
                val userRating = document.getString("rating")

                binding.userName.text = userName
                binding.userSurname.text = userSurname
                binding.userEmailAddress.text = userEmail
                if (userRating != null) {
                    binding.UserRatingBar.numStars = userRating.toInt()
                }
            }

        }

        return binding.root
    }
    companion object {
        const val TAG = "userProfile"
    }
}