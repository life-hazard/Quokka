package com.example.quokka.logged

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quokka.R
import com.example.quokka.databinding.FragmentAvailableTasksBrowserBinding
import com.example.quokka.recyclerview.TaskAdapter
import com.example.quokka.recyclerview.TaskModel
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_available_tasks_browser.*

//  Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AvailableTasksBrowser.newInstance] factory method to
 * create an instance of this fragment.
 */

// Model - make it an outside class later
// ...but model is a data class so I can use those I made to write into the db...
// but I need a default element for each of the variables
data class TaskModel1(
    val taskName: String = "",
    val startDate: Map<String, Int> = mapOf("k" to -1),
    val endDate: Map<String, Int> = mapOf("k" to -1),
    val points: Int = -1
)

class TaskViewHolder1(itemView: View) : RecyclerView.ViewHolder(itemView)

class AvailableTasksBrowser : Fragment() {

    private lateinit var binding: FragmentAvailableTasksBrowserBinding
    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
    private val sharedPrefFile = "kotlinsharedpreference"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate<FragmentAvailableTasksBrowserBinding>(inflater, R.layout.fragment_available_tasks_browser, container, false)

        auth = Firebase.auth

        val sharedPreferences: SharedPreferences =
            requireActivity().getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val sharedIdValue = sharedPreferences.getString("id_key", "default_value")

        Log.d(UserTasksFragment.TAG, "The id from shared preferences: ${sharedIdValue.toString()}")

        val query = db.collection("tasks").whereNotEqualTo("ownerId", sharedIdValue)

        val options = FirestoreRecyclerOptions.Builder<TaskModel1>().setQuery(query, TaskModel1::class.java)
            .setLifecycleOwner(this).build()

        // SHOULD BE A SEPARATE CLASS FILE
        val adapter = object: FirestoreRecyclerAdapter<TaskModel1, TaskViewHolder1>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder1 {
                val view: View = LayoutInflater.from(context).inflate(R.layout.recyclerview_task_item, parent, false)
                return TaskViewHolder1(view)
            }

            override fun onBindViewHolder(
                holder: TaskViewHolder1,
                position: Int,
                model: TaskModel1
            ) {
                val tvName: TextView = holder.itemView.findViewById(R.id.itemTaskName)
                val tvStartDate: TextView = holder.itemView.findViewById(R.id.itemTaskStartDate)
                val tvEndDate: TextView = holder.itemView.findViewById(R.id.itemTaskEndDate)
                val tvPoints: TextView = holder.itemView.findViewById(R.id.itemTaskPoints)

                tvName.text = model.taskName
                val start = "${model.startDate["day"]}.${model.startDate["month"]}.${model.startDate["year"]}"
                tvStartDate.text = start
                //tvStartDate.text = model.startDate.values.toString()
                val end = "${model.startDate["day"]}.${model.startDate["month"]}.${model.startDate["year"]}"
                tvEndDate.text = end
                //tvEndDate.text = model.endDate.values.toString()
                tvPoints.text = model.points.toString()
            }

        }

        binding.recyclerViewAvailableTasks.adapter = adapter

        return binding.root
    }

    // TODO: Add CardView to recyclerview_item_row and edit the points cuz it doesn't scale
}