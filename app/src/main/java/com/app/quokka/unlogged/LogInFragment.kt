package com.app.quokka.unlogged

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.app.quokka.databinding.FragmentLogInBinding
import kotlinx.android.synthetic.main.fragment_log_in.*
import kotlinx.android.synthetic.main.fragment_sign_up.*
import com.app.quokka.*
import com.app.quokka.logged.UserHomePageActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

/**
 * A simple [Fragment] subclass.
 * Use the [LogInFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LogInFragment : Fragment() {

    //private lateinit var userViewModel: AppViewModel

    private lateinit var binding: FragmentLogInBinding

    private lateinit var db: FirebaseFirestore

    private val sharedPrefFile = "kotlinsharedpreference"
    private val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)


    // called during fragment lifecycle
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_log_in, container, false
        )

        // get shared preferences file

        //val intent = Intent(activity, UserHomePageActivity::class.java)
        //userViewModel = ViewModelProvider(this).get(AppViewModel::class.java)

        val intent1 = Intent(activity, SignUpActivity::class.java)

        // dis gon be gud
        val application = requireNotNull(this.activity).application
        binding.lifecycleOwner = this

        binding.logInButton.setOnClickListener {
            logIn()
            //login()
            //activity?.startActivity(intent)
            val sharedIdValue = sharedPreferences?.getString("key_id","default_name")
            Log.i("login", "Shared Id value: $sharedIdValue")
        }

        binding.signUpButton.setOnClickListener { //view: View ->
            activity?.startActivity(intent1)
            //view.findNavController().navigate(R.id.action_logInFragment_to_signUpFragment)
        }


        return binding.root
    }

    //@SuppressLint("CommitPrefEdits")
    private fun logIn() {
        // Read input
        val db = Firebase.firestore

        val email = binding.editUserEmailAddress.text.toString().trim()
        val password = binding.editPassword.text.toString()
        var sharedUserId = " "

//        val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)

        val intent = Intent(activity, UserHomePageActivity::class.java)

        db.collection("users").whereEqualTo("email", email)
            .whereEqualTo("password", password)
            .get()
            .addOnSuccessListener { documents ->
            for (document in documents) {
                Log.d(TAG, "${document.id} => $document.data")
                //val dbPassword = document.getString("password")
                sharedUserId = document.id

                // Setting user id into shared preferences so that its available always:
                // Set a shared preference
                val editor: SharedPreferences.Editor? = sharedPreferences?.edit()
                editor?.putString("id_key", sharedUserId)
                editor?.apply()
                editor?.commit()

                Log.d(TAG, "The user ID in shared preferences: $sharedUserId ")

                Log.d(TAG, sharedUserId)
                activity?.startActivity(intent)
            }
        }.addOnFailureListener { e ->
            Log.w(TAG, "Error getting documents: ", e)
            Toast.makeText(context, "Wrong email or password.", Toast.LENGTH_SHORT).show()
        }

    }

    private fun getUserId() {

    }

    fun login() {
        val db = Firebase.firestore

        val usersRef = db.collection("users")

        var email = editUserEmailAddress.text.toString().trim()
        var password = editPassword.text.toString()

        val query = usersRef.whereEqualTo("email", "$email")
        Log.i("login", "$query")
    }

    companion object {
        val TAG = "login"
    }
}
