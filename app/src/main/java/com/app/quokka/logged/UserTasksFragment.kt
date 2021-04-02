package com.app.quokka.logged

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.app.quokka.R
import com.app.quokka.databinding.FragmentUserTasksBinding
import com.app.quokka.recyclerview.TaskAdapter
import com.app.quokka.tasks.FullUserTaskActivity
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_task.*
import kotlinx.android.synthetic.main.fragment_user_tasks.*

//  Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UserTasksFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 * Holds the tasks made by the logged user
 */

data class UserTaskModel(
    val taskName: String = "",
    var taskDescription: String = "",
    val startDate: Map<String, Int> = mapOf("k" to -1),
    val endDate: Map<String, Int> = mapOf("k" to -1),
    val points: Int = -1,
    var ownerId: String = "",
    var takerId: String = "",
    val status: String = ""
)

class UserTaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    init {
        itemView.setOnClickListener() {
            Snackbar.make(itemView, "CLICKED", Snackbar.LENGTH_SHORT).show()
        }
    }
}

class UserTasksFragment : Fragment() {

    private lateinit var binding: FragmentUserTasksBinding

    lateinit var adapter: TaskAdapter

    val db = Firebase.firestore

    private val sharedPrefFile = "kotlinsharedpreference"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentUserTasksBinding>(
            inflater,
            R.layout.fragment_user_tasks,
            container,
            false
        )

        // getting from shared preferences


        val sharedPreferences: SharedPreferences =
            requireActivity().getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val sharedIdValue = sharedPreferences.getString("id_key", "default_value")

        Log.d(TAG, "The id from shared preferences: ${sharedIdValue.toString()}")

        val query = db.collection("tasks").whereEqualTo("ownerId", sharedIdValue)
        val options = FirestoreRecyclerOptions.Builder<UserTaskModel>().setQuery(
            query,
            UserTaskModel::class.java
        ).setLifecycleOwner(this).build()

        val adapter = object: FirestoreRecyclerAdapter<UserTaskModel, UserTaskViewHolder>(options) {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserTaskViewHolder {
                val view: View = LayoutInflater.from(context).inflate(
                    R.layout.recyclerview_task_item,
                    parent,
                    false
                )
                return UserTaskViewHolder(view)
            }

            override fun onBindViewHolder(
                holder: UserTaskViewHolder,
                position: Int,
                model: UserTaskModel
            ) {
                val tvName: TextView = holder.itemView.findViewById(R.id.itemTaskName)
                val tvStartDate: TextView = holder.itemView.findViewById(R.id.itemTaskStartDate)
                val tvEndDate: TextView = holder.itemView.findViewById(R.id.itemTaskEndDate)
                val tvPoints: TextView = holder.itemView.findViewById(R.id.itemTaskPoints)

                tvName.text = model.taskName
                val start = mapToDate(model.startDate["day"], model.startDate["month"], model.startDate["year"])
                tvStartDate.text = start
                val end = mapToDate(model.endDate["day"], model.endDate["month"], model.endDate["year"])
                tvEndDate.text = end
                tvPoints.text = model.points.toString()

                holder.itemView.setOnClickListener() { view ->
                    Toast.makeText(context, "CLICK!!", Toast.LENGTH_SHORT).show()

                    val intent = Intent(context, FullUserTaskActivity::class.java)
                    intent.putExtra("fullTaskName", model.taskName) // put data in Intent
                    intent.putExtra("fullTaskDescription", model.taskDescription)
                    intent.putExtra("fullStartDate", start)
                    intent.putExtra("fullEndDate", end)
                    intent.putExtra("fullPoints", model.points)
                    intent.putExtra("fullTaskOwnerId", model.ownerId)
                    intent.putExtra("fullTaskTakerId", model.takerId)

                    context!!.startActivity(intent) // start Intent
                }
            }


        }
        binding.userTasksRecyclerView.adapter = adapter

        return binding.root
    }

    private fun mapToDate(day: Int?, month: Int?, year: Int?) : String {
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
        Log.d(TAG, "The map to date is: $newDate")
        return newDate
    }

    companion object {
        const val TAG = "tagz"
    }
}