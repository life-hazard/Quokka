package com.example.quokka.tasks

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.example.quokka.R
import com.example.quokka.databinding.ActivityFullUserTaskBinding
import com.example.quokka.logged.AddTaskActivity
import com.example.quokka.logged.UserHomePageActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class FullUserTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFullUserTaskBinding
    private val sharedPrefFile = "kotlinsharedpreference"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_full_user_task)

        // Taking data from CardView
        val taskName = intent.getStringExtra("fullTaskName")
        val taskDescription = intent.getStringExtra("fullTaskDescription")
        val taskStart = intent.getStringExtra("fullStartDate")
        val taskEnd = intent.getStringExtra("fullEndDate")
        val taskPoints = intent.getStringExtra("fullPoints")
        val taskOwner = intent.getStringExtra("fullTaskOwnerId")
        // Putting data from CardView into TextViews
        binding.taskNameEdit.setText(taskName)
        binding.taskDescriptionEdit.setText(taskDescription)
        binding.taskStartDateEdit.setText(taskStart)
        binding.taskEndDateEdit.setText(taskEnd)
        binding.taskOwnerEdit.setText("For $taskOwner")
        // TODO get the taskOwner name from users collection

        // Adding parameter
        val currentUserId = ""
        val sharedPreferences: SharedPreferences =
            this.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        //var taskTakerId: String = sharedPreferences.getString("id_key", "default_value").toString()
        var taskId = ""

        val db = Firebase.firestore

        val data = db.collection("tasks").document()

        binding.fullButtonEditTask.setOnClickListener() {

            // TODO START HERE overwritten two buttons needs to change to focusable and clickable
            // Changing to editable
            changeToEditable()

            binding.fullButtonDiscard.setOnClickListener {
                changeToUneditable()
            }

            binding.fullButtonSave.setOnClickListener {
                // Getting new information
                val newTaskName = binding.taskNameEdit.text.toString()
                val newTaskDescription = binding.taskDescriptionEdit.text.toString()
                val newStartDate = dateToTimeMap(binding.taskStartDateEdit.text.toString())
                val newEndDate = dateToTimeMap(binding.taskEndDateEdit.text.toString())

                // Updating this task
                db.collection("tasks")
                    .whereEqualTo("taskName", taskName)
                    .whereEqualTo("ownerId", taskOwner)
                    .whereEqualTo("takerId", "")
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            Log.d(FullTaskActivity.TAG, "${document.id}")
                            Log.d(FullTaskActivity.TAG, "Task owner: $taskOwner")
                            taskId = document.id
                            Log.d(FullTaskActivity.TAG, "task id: $taskId")

                            db.collection("tasks").document(taskId).update("taskName", newTaskName, "taskDescription", newTaskDescription, "startDate", newStartDate, "endDate", newEndDate).addOnSuccessListener {
                                Log.d(FullTaskActivity.TAG, "Task updated!!")
                            }
                        }
                    }
                Log.d(FullTaskActivity.TAG, "Task id: $taskId")
                Snackbar.make(binding.root, "Task updated.", Snackbar.LENGTH_LONG).show()
                changeToUneditable()
                //intent = Intent(this, UserHomePageActivity::class.java)
                //this.startActivity(intent)
            }
        }

    }

    private fun changeToEditable() {
        // hide buttons
        binding.fullButtonEditTask.visibility = View.GONE
        binding.fullButtonBack.visibility = View.GONE
        // show buttons
        binding.fullButtonSave.visibility = View.VISIBLE
        binding.fullButtonDiscard.visibility = View.VISIBLE
        // enable editing views
        binding.taskNameFrame.isFocusable = true
        binding.taskNameFrame.isFocusableInTouchMode = true
        binding.taskNameEdit.isClickable = true
        binding.taskNameEdit.isFocusable = true
        binding.taskNameEdit.isFocusableInTouchMode = true
        binding.taskDescriptionFrame.isClickable = true
        binding.taskDescriptionFrame.isFocusable = true
        binding.taskDescriptionFrame.isFocusableInTouchMode = true
        binding.taskDescriptionEdit.isClickable = true
        binding.taskDescriptionEdit.isFocusable = true
        binding.taskDescriptionEdit.isFocusableInTouchMode = true
        binding.taskStartDateFrame.isClickable = true
        binding.taskStartDateFrame.isFocusable = true
        binding.taskStartDateFrame.isFocusableInTouchMode = true
        binding.taskStartDateEdit.isClickable = true
        binding.taskStartDateEdit.isFocusable = true
        binding.taskStartDateEdit.isFocusableInTouchMode = true
        binding.taskEndDateFrame.isClickable = true
        binding.taskEndDateFrame.isFocusable = true
        binding.taskEndDateFrame.isFocusableInTouchMode = true
        binding.taskEndDateEdit.isClickable = true
        binding.taskEndDateEdit.isFocusable = true
        binding.taskEndDateEdit.isFocusableInTouchMode = true
        binding.taskOwnerFrame.isEnabled = false
    }

    private fun changeToUneditable() {
        // show buttons
        binding.fullButtonEditTask.visibility = View.VISIBLE
        binding.fullButtonBack.visibility = View.VISIBLE
        // hide buttons
        binding.fullButtonSave.visibility = View.GONE
        binding.fullButtonDiscard.visibility = View.GONE
        // enable editing views
        binding.taskNameFrame.isFocusable = false
        binding.taskNameFrame.isFocusableInTouchMode = false
        binding.taskNameEdit.isClickable = false
        binding.taskNameEdit.isFocusable = false
        binding.taskNameEdit.isFocusableInTouchMode = false
        binding.taskDescriptionFrame.isClickable = false
        binding.taskDescriptionFrame.isFocusable = false
        binding.taskDescriptionFrame.isFocusableInTouchMode = false
        binding.taskDescriptionEdit.isClickable = false
        binding.taskDescriptionEdit.isFocusable = false
        binding.taskDescriptionEdit.isFocusableInTouchMode = false
        binding.taskStartDateFrame.isClickable = false
        binding.taskStartDateFrame.isFocusable = false
        binding.taskStartDateFrame.isFocusableInTouchMode = false
        binding.taskStartDateEdit.isClickable = false
        binding.taskStartDateEdit.isFocusable = false
        binding.taskStartDateEdit.isFocusableInTouchMode = false
        binding.taskEndDateFrame.isClickable = false
        binding.taskEndDateFrame.isFocusable = false
        binding.taskEndDateFrame.isFocusableInTouchMode = false
        binding.taskEndDateEdit.isClickable = false
        binding.taskEndDateEdit.isFocusable = false
        binding.taskEndDateEdit.isFocusableInTouchMode = false
        binding.taskOwnerFrame.isEnabled = true
    }

    private fun dateToTimeMap(date: String): Map<String, Int> {
        Log.i(TAG, "The Date: $date")
        val day = (date[0].toString() + date[1].toString()).toInt()
        var month: Int
        var year: Int
        if (date.length == 9) {
            month = date[3].toString().toInt()
            Log.i(TAG, "Date[3] == ${date[3]} month == $month")
            year = (date[5].toString() + date[6].toString() + date[7].toString() + date[8].toString()).toInt()
            Log.i(TAG, "The day: $day, month: $month, year: $year")
        } else {
            month = (date[3].toString() + date[4].toString()).toInt()
            year = (date[6].toString() + date[7].toString() + date[8].toString() + date[9].toString()).toInt()
            Log.i(TAG, "The day: $day, month: $month, year: $year")
        }

        val mapDate = mapOf("day" to day, "month" to month, "year" to year)
        Log.i(AddTaskActivity.TAG, "Map of date: ${mapDate.values}")
        return mapDate
    }

    companion object {
        const val TAG = "usertask"
    }
}
// android:backgroundTint="@color/material_on_background_disabled"