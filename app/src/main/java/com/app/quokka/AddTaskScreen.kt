package com.app.quokka

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.*
import androidx.databinding.DataBindingUtil
import com.app.quokka.databinding.FragmentAddTaskScreenBinding
import kotlinx.android.synthetic.main.fragment_add_task_screen.*
import kotlinx.android.synthetic.main.fragment_sign_up.*

//  Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddTaskScreen.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddTaskScreen : Fragment() {
    //  Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentAddTaskScreenBinding

    private val taskInfo: TaskInfo = TaskInfo("Dog Walk", "Walk my dog plz", 10)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_task_screen,container, false)

        binding.addButton.setOnClickListener() {
            addData()
        }


        return binding.root
    }

    private fun addData() {
        val name = taskNameEdit.text.toString()
        val description = taskDescriptionEdit.text.toString()
        val points = pointsForTask.text.toString()
        Log.i("Adding data", name)
    }


}