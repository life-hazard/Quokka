package com.app.quokka.logged

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.quokka.tasks.FullTaskActivity
import com.app.quokka.R
import com.app.quokka.databinding.FragmentAvailableTasksBrowserBinding
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_available_tasks_browser.*

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
    var taskDescription: String = "",
    val startDate: Map<String, Int> = mapOf("k" to -1),
    val endDate: Map<String, Int> = mapOf("k" to -1),
    val points: Int = -1,
    var ownerId: String = "",
    var takerId: String = ""
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
        Log.i("Opened", "AvailableTasksBrowser")
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_available_tasks_browser,
            container,
            false
        )

        auth = Firebase.auth

        val sharedPreferences: SharedPreferences =
            requireActivity().getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val sharedIdValue = sharedPreferences.getString("id_key", "default_value")

        Log.d(UserTasksFragment.TAG, "The id from shared preferences: ${sharedIdValue.toString()}")

        val query = db.collection("tasks")
            .whereEqualTo("status", "available")
            .whereNotEqualTo("ownerId", sharedIdValue)

        val options =
            FirestoreRecyclerOptions.Builder<TaskModel1>().setQuery(query, TaskModel1::class.java)
                .setLifecycleOwner(this).build()

        // SHOULD BE A SEPARATE CLASS FILE LATER
        val adapter = object : FirestoreRecyclerAdapter<TaskModel1, TaskViewHolder1>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder1 {
                val view: View = LayoutInflater.from(context)
                    .inflate(R.layout.recyclerview_task_item, parent, false)
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
                val start = mapToDate(
                    model.startDate["day"],
                    model.startDate["month"],
                    model.startDate["year"]
                )
                tvStartDate.text = start
                val end = mapToDate(
                    model.endDate["day"],
                    model.endDate["month"],
                    model.endDate["year"]
                )
                tvEndDate.text = end
                tvPoints.text = model.points.toString()

                holder.itemView.setOnClickListener() {

                    val intent = Intent(context, FullTaskActivity::class.java)
                    intent.putExtra("fullTaskName", model.taskName) // put image data in Intent
                    intent.putExtra("fullTaskDescription", model.taskDescription)
                    intent.putExtra("fullStartDate", start)
                    intent.putExtra("fullEndDate", end)
                    intent.putExtra("fullPoints", model.points.toString())
                    intent.putExtra("fullTaskOwnerId", model.ownerId)

                    context!!.startActivity(intent) // start Intent
                }
            }
        }
        binding.recyclerViewAvailableTasks.adapter = adapter

        return binding.root
    }

    private fun mapToDate(day: Int?, month: Int?, year: Int?): String {
        val setDay = if (day.toString().toInt() < 10) {
            "0$day"
        } else {
            "$day"
        }
        val setMonth = if (month.toString().toInt() < 10) {
            "0$month"
        } else {
            "$month"
        }
        val setYear = "$year"

        val newDate = "$setDay.$setMonth.$setYear"
        Log.d(UserTasksFragment.TAG, "The map to date is: $newDate")
        return newDate
    }
}