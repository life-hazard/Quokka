package com.app.quokka

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
//import com.example.quokka.database.User
import com.app.quokka.databinding.FragmentShowUsersBinding
import com.app.quokka.unlogged.SignUpActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


/**
 * A simple [Fragment] subclass.
 * Use the [ShowUsers.newInstance] factory method to
 * create an instance of this fragment.
 */
class ShowUsers : Fragment() {

    //private lateinit var mUserViewModel: AppViewModel
    // ???
    //lateinit var userList: MutableLiveData<List<User>>
    //var adapter = ListAdapter()

    private lateinit var binding: FragmentShowUsersBinding
    private var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentShowUsersBinding>(inflater,
            R.layout.fragment_show_users, container, false)

        val intent = Intent(activity, SignUpActivity::class.java)

        // RecyclerView
        //val adapter = ListAdapter()
        //binding.signUpRecyclerView.adapter = adapter
        //binding.signUpRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // UserViewModel
        //mUserViewModel = ViewModelProvider(this).get(AppViewModel::class.java)
        //mUserViewModel.readUserData.observe(viewLifecycleOwner, Observer { user ->
        //    adapter.setData(user)
        //})

        binding.floatingActionButton3.setOnClickListener {
            // findNavController().navigate(R.id.action_showUsers_to_signUpFragment)
            showUsers()
            activity?.startActivity(intent)
        }

        return binding.root
    }

    private fun showUsers() {
        val users = db.collection("users").get().result.toString()
        Log.i("show users", "All users: $users")

    }

}