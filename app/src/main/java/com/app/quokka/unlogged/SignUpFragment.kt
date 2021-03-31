package com.app.quokka.unlogged

import android.content.ContentValues.TAG
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast.*
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.app.quokka.R
import com.app.quokka.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_user_profile.view.*
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.android.synthetic.main.toolbar.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil.*

/**
 * A simple [Fragment] subclass.
 * Use the [SignUpFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    //private var _binding: FragmentSignUpBinding? = null
    //private val binding get() = _binding!!

//    private lateinit var googleSignInClient: GoogleSignInClient
//    private var RC_SIGN_IN: Int = 123
    private lateinit var auth: FirebaseAuth


    //private lateinit var mUserViewModel: AppViewModel
    //private val userInfo: UserInfo = UserInfo("Name", "Surname", "mail@mail.com", "12345", "Town")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            reload();
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                      savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false)
        //_binding = FragmentSignUpBinding.inflate(inflater, container, false)
        //val view = binding.root
        //binding.lifecycleOwner = this
//        // Configure Google Sign In
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getString(R.string.default_web_client_id))
//            .requestEmail()
//            .build()
//        googleSignInClient = GoogleSignIn.getClient(this.requireContext(), gso)
//        auth = Firebase.auth


        // Initialize Firebase Auth
        auth = Firebase.auth

        //mUserViewModel = ViewModelProvider(this).get(AppViewModel::class.java)

        binding.signUpSignUpButton.setOnClickListener {
            //insertDataToDatabase()
            view: View ->
            view. findNavController().navigate(R.id.action_signUpFragment_to_logInFragment)
        }

//        binding.signUpWithGoogleButton.setOnClickListener {
//            signIn()
//        }

        fun validateUser(): Boolean {
            var valid = true
            val name = binding.nameTextEdit.text.toString().trim()
            if(TextUtils.isEmpty(name)) {
                binding.nameTextEdit.error = "Required."
                valid = false
            } else {
                binding.nameTextEdit.error = null
            }

            val surname = binding.surnameEditText.text.toString().trim()
            if(TextUtils.isEmpty(surname)) {
                binding.surnameEditText.error = "Required."
                valid = false
            } else {
                binding.surnameEditText.error = null
            }

            val email = binding.emailEditText.text.toString().trim()
            if(TextUtils.isEmpty(email)) {
                binding.emailEditText.error = "Required."
                valid = false
            } else {
                binding.emailEditText.error = null
            }

            val password = binding.passwordEditText.text.toString()
            val passwordCheck = binding.password2EditText.text.toString()
            if(TextUtils.isEmpty(password) && TextUtils.isEmpty(passwordCheck)) {
                binding.passwordEditText.error = "Required."
                binding.password2EditText.error = "Required"
                valid = false
            } else {
                binding.passwordEditText.error = null
                if(password !== passwordCheck) {
                    binding.password2EditText.error = "Wrong password."
                    valid = false
                } else {
                    if(password.length < 10)
                        TODO("Add info about it being at least 10")
                }
            }

            val address = binding.addressEditText.text.toString().trim()
            if(TextUtils.isEmpty(address)) {
                binding.addressEditText.error = "Required."
                valid = false
            } else {
                binding.addressEditText.error = null
            }

            return valid
        }


        fun createAccount(name: String, username: String, email: String, password:String, passwordCheck: String, address:String, rating: Int) {
            Log.d(TAG, "createAccount:$email")
            if (!validateUser()) {
                return
            }
            //auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(context) {
            //    task ->

            //}
            }



        //return view
        //return inflater.inflate(R.layout.fragment_sign_up, container, false)
        return binding.root
    }

    private fun reload() {
        auth.currentUser!!.reload().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                updateUI(auth.currentUser)
                Toast.makeText(context, "Reload Successful!", Toast.LENGTH_SHORT).show()
            } else {
                Log.e("reload", task.exception.toString())
                Toast.makeText(context, "Failed to load user.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        //update what user sees if reloaded
    }
}


//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
//        if (requestCode == RC_SIGN_IN) {
//            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//            handleSignInResult(task)
////            try {
////                // Google Sign In was successful, authenticate with Firebase
////                val account = task.getResult(ApiException::class.java)!!
////                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
////                firebaseAuthWithGoogle(account.idToken!!)
////            } catch (e: ApiException) {
////                // Google Sign In failed, update UI appropriately
////                Log.w(TAG, "Google sign in failed", e)
////                // ...
////            }
//        }
//    }
//
//    private fun handleSignInResult(task: Task<GoogleSignInAccount>?) {
//            try {
//                // Google Sign In was successful, authenticate with Firebase
//                val account = task?.getResult(ApiException::class.java)!!
//                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
//                firebaseAuthWithGoogle(account.idToken!!)
//            } catch (e: ApiException) {
//                // Google Sign In failed, update UI appropriately
//                Log.w(TAG, "Google sign in failed", e)
//                // ...
//            }
//    }
//
//    private fun firebaseAuthWithGoogle(idToken: String) {
//        val credential = GoogleAuthProvider.getCredential(idToken, null)
//        signUpSignUpButton.visibility = View.GONE
//        progressBar.visibility = View.VISIBLE
//        GlobalScope.launch(Dispatchers.IO) {
//            val auth = auth.signInWithCredential(credential).await()
//            val firebaseUser = auth.user
//            withContext(Dispatchers.Main) {
//                updateUI(firebaseUser)
//            }
//        }
//    }
//
//    private fun updateUI(firebaseUser: FirebaseUser?) {
//        if (firebaseUser != null) {
//            val mainActivityIntent = Intent(this.requireContext(), MainActivity::class.java)
//            startActivity(mainActivityIntent)
//            //finish()
//        } else {
//            signUpSignUpButton.visibility = View.GONE
//            progressBar.visibility = View.VISIBLE
//        }
//
//    }
//
//    private fun signIn() {
//        val signInIntent = googleSignInClient.signInIntent
//        startActivityForResult(signInIntent, RC_SIGN_IN)
//    }






//    private fun insertDataToDatabase() {
//        val name = nameTextEdit.text.toString()
//        val surname = surnameEditText.text.toString()
//        val email = emailEditText.text.toString()
//        val password = passwordEditText.text.toString()
//        val password2 = password2EditText.text.toString()
//        val address = addressEditText.text.toString()
//        Log.i("signup", "Taken the user input")
//
//    }
//
//    private fun inputCheck(name: String, surname: String, email: String, password: String, password2: String, address: String): Boolean {
//        Log.i("signup", "Running input check")
//
//        return (name.isNotEmpty() && surname.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && password2.isNotEmpty() && address.isNotEmpty()
//                && password == password2
//                && isValidEmail(email))
//    }
//
//    private fun isValidEmail(email: String): Boolean {
//        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches())
//    }
//
//    private fun readData() {
//        val name = nameTextEdit.text.toString()
//        val surname = surnameEditText.text.toString()
//        val email = emailEditText.text.toString()
//        val password = passwordEditText.text.toString()
//        val password2 = password2EditText.text.toString()
//        val address = addressEditText.text.toString()
//        Log.i("signup", name)
//        Log.i("signup", surname)
//
//    }
