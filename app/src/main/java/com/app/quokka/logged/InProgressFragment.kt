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
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.quokka.R
import com.app.quokka.databinding.FragmentInProgressBinding
import com.app.quokka.recyclerview.TaskAdapter
import com.app.quokka.tasks.FullShowTaskActivity
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_in_progress.*
import kotlinx.android.synthetic.main.*

/**
 * A simple [Fragment] subclass.
 * Use the [InProgressFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 * Holds Tasks that logged user said they'll complete
 */

// TODO check tasks filtering

data class IPTaskModel(
    val taskName: String = "",
    var taskDescription: String = "",
    val startDate: Map<String, Int> = mapOf("k" to -1),
    val endDate: Map<String, Int> = mapOf("k" to -1),
    val points: Int = -1,
    var ownerId: String = "",
    val takerId: String = ""
)

class IPTaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

class InProgressFragment : Fragment() {

    private lateinit var binding: FragmentInProgressBinding
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
        binding = DataBindingUtil.inflate<FragmentInProgressBinding>(inflater,
            R.layout.fragment_in_progress, container, false)

        val sharedPreferences: SharedPreferences =
            requireActivity().getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val sharedIdValue = sharedPreferences.getString("id_key", "default_value")

        val query = db.collection("tasks")
            .whereEqualTo("takerId", sharedIdValue)
            .whereEqualTo("status", "unavailable")
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


                //Log.i(TAG, "The task id: ${model.taskId}")
                tvName.text = model.taskName
                val start = mapToDate(model.startDate["day"], model.startDate["month"], model.startDate["year"])
                tvStartDate.text = start
                //tvStartDate.text = model.startDate.values.toString()
                val end = mapToDate(model.endDate["day"], model.endDate["month"], model.endDate["year"])
                tvEndDate.text = end
                //tvEndDate.text = model.endDate.values.toString()
                tvPoints.text = model.points.toString()

                holder.itemView.setOnClickListener() { view ->
                    Toast.makeText(context, "CLICK!!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(context, FullShowTaskActivity::class.java)
                    intent.putExtra("fullTaskName", model.taskName) // put image data in Intent
                    intent.putExtra("fullTaskDescription", model.taskDescription)
                    intent.putExtra("fullStartDate", start)
                    intent.putExtra("fullEndDate", end)
                    intent.putExtra("fullPoints", model.points)
                    intent.putExtra("fullTaskOwnerId", model.ownerId)


                    context!!.startActivity(intent) // start Intent


                }
            }


        }

        binding.recyclerViewTasksInProgress.adapter = adapter

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
        Log.d(UserTasksFragment.TAG, "The map to date is: $newDate")
        return newDate
    }
}