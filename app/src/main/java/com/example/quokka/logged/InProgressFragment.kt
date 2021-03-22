package com.example.quokka.logged

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quokka.R
import com.example.quokka.databinding.FragmentInProgressBinding
import kotlinx.android.synthetic.main.fragment_in_progress.*
import kotlinx.android.synthetic.main.*

/**
 * A simple [Fragment] subclass.
 * Use the [InProgressFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 * Holds Tasks that logged user said they'll complete
 */
class InProgressFragment : Fragment() {

    private lateinit var binding: FragmentInProgressBinding
    //private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentInProgressBinding>(inflater,
            R.layout.fragment_in_progress, container, false)

        //linearLayoutManager = LinearLayoutManager(context)
        //recyclerView.layoutManager = linearLayoutManager

        //val adapter = UserListAdapter()
        //binding.inProgressRecyclerView.adapter = adapter
        //binding.inProgressRecyclerView.layoutManager = LinearLayoutManager(context)

        return binding.root
    }
}