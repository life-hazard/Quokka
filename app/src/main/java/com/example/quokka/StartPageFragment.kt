package com.example.quokka

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.quokka.databinding.FragmentStartPageBinding

//  Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [StartPageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

/**
 *
 * A subclass which will be a navigation host
 *
 */

class StartPageFragment : Fragment() {

    private lateinit var binding: FragmentStartPageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_start_page, container, false)

        binding.imageButton.setOnClickListener {
            view : View ->
            view.findNavController().navigate(R.id.action_logInFragment_to_userMainScreenFragment)
        }

        return binding.root
    }

}