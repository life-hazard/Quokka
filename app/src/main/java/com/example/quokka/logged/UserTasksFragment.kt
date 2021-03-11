package com.example.quokka.logged

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.quokka.R
import com.example.quokka.databinding.FragmentUserTasksBinding

//  Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UserTasksFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserTasksFragment : Fragment() {

    private lateinit var binding: FragmentUserTasksBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentUserTasksBinding>(inflater, R.layout.fragment_user_tasks, container, false)
        return binding.root
    }


}