package com.app.quokka.logged

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.quokka.R
import com.app.quokka.databinding.ActivityFinishedTasksBinding
import com.app.quokka.tasks.DoneTaskActivity
import com.app.quokka.tasks.FullShowTaskActivity
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


// TODO I have to make a recycler view with tasks with status completed

data class FinTaskModel (
    var taskName: String = "",
    var taskDescription: String = "",
    var startDate: Map<String, Int> = mapOf("k" to -1),
    var endDate: Map<String, Int> = mapOf("k" to -1),
    var points: Int = -1,
    var ownerId: String = "",
    var takerId: String = "",
    var status: String = ""
        )

class FinTaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

class FinishedTasksActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFinishedTasksBinding
    val db = Firebase.firestore
    private val sharedPrefFile = "kotlinsharedpreference"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_finished_tasks)

        val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val sharedIdValue = sharedPreferences.getString("id_key", "default_value")

        val query = db.collection("tasks")
            .whereEqualTo("ownerId", sharedIdValue)
            .whereEqualTo("status", "complete")

        // TODO I think there are errors with other options val in recyclerview fragments -> data class not used
        val options = FirestoreRecyclerOptions.Builder<UserTaskModel>().setQuery(query, UserTaskModel::class.java).setLifecycleOwner(this).build()

        val adapter = object: FirestoreRecyclerAdapter<UserTaskModel, UserTaskViewHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserTaskViewHolder {
                val view: View = layoutInflater.inflate( R.layout.recyclerview_task_item,
                    parent,
                    false)
                return UserTaskViewHolder(view)
            }

            override fun onBindViewHolder(
                holder: UserTaskViewHolder,
                position: Int,
                model: UserTaskModel
            ) {
                // holder for getting ids from itemView which is recycler_view_item
                val tvName: TextView = holder.itemView.findViewById(R.id.itemTaskName)
                val tvStartDate: TextView = holder.itemView.findViewById(R.id.itemTaskStartDate)
                val tvEndDate: TextView = holder.itemView.findViewById(R.id.itemTaskEndDate)
                val tvPoints: TextView = holder.itemView.findViewById(R.id.itemTaskPoints)

                tvName.text = model.taskName
                val start = mapToDate(model.startDate["day"], model.startDate["month"], model.startDate["year"])
                tvStartDate.text = start
                //tvStartDate.text = model.startDate.values.toString()
                val end = mapToDate(model.endDate["day"], model.endDate["month"], model.endDate["year"])
                tvEndDate.text = end
                //tvEndDate.text = model.endDate.values.toString()
                tvPoints.text = model.points.toString()

                holder.itemView.setOnClickListener() { view ->
                    //Toast.makeText(this@FinishedTasksActivity, "CLICK!!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@FinishedTasksActivity, DoneTaskActivity::class.java)
                    intent.putExtra("fullTaskName", model.taskName) // put image data in Intent
                    intent.putExtra("fullTaskDescription", model.taskDescription)
                    intent.putExtra("fullStartDate", start)
                    intent.putExtra("fullEndDate", end)
                    intent.putExtra("fullPoints", model.points.toString())
                    intent.putExtra("fullTaskOwnerId", model.ownerId)
                    intent.putExtra("fullTaskTakerId", model.takerId)

                    this@FinishedTasksActivity.startActivity(intent) // start Intent
                }
            }

        }
        binding.finishedTasksRecyclerView.adapter = adapter

        binding.goBackArrow.setOnClickListener {
            val intent = Intent(this, UserHomePageActivity::class.java)
            this.startActivity(intent)
        }

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