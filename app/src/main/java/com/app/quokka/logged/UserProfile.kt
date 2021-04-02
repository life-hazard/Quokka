package com.app.quokka.logged

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.app.quokka.R
import com.app.quokka.databinding.FragmentUserProfileBinding
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
                val r = document.data?.get("rating").toString()
                val ratingMap = r.substring(1, r.length-1).split(", ").associate {
                    val (left, right) = it.split("=")
                    left to right.toFloat()
                }
                val rating = ratingMap.getValue("rating")

                binding.userName.text = userName
                binding.userSurname.text = userSurname
                binding.userEmailAddress.text = userEmail
                binding.UserRatingBar.rating = rating

            }

        }

        return binding.root
    }
    companion object {
        const val TAG = "userProfile"
    }
}