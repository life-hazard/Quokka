package com.app.quokka.tasks

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.app.quokka.R
import com.app.quokka.databinding.ActivityFullUserTaskBinding
import com.app.quokka.logged.AddTaskActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class FullUserTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFullUserTaskBinding
    private val sharedPrefFile = "kotlinsharedpreference"
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_full_user_task)

        // Taking data from CardView
        val taskName = intent.getStringExtra("fullTaskName")
        val taskDescription = intent.getStringExtra("fullTaskDescription")
        val taskStart = intent.getStringExtra("fullStartDate").toString()
        val taskEnd = intent.getStringExtra("fullEndDate").toString()
        val taskPoints = intent.getStringExtra("fullPoints")
        val taskOwner = intent.getStringExtra("fullTaskOwnerId").toString()
        val taskTaker = intent.getStringExtra("fullTaskTakerId").toString()

        // Putting data from CardView into TextViews
        binding.taskNameEdit.setText(taskName)
        binding.taskDescriptionEdit.setText(taskDescription)
        binding.taskStartDateEdit.setText(taskStart)
        binding.taskEndDateEdit.setText(taskEnd)
        db.collection("users")
            .document(taskOwner)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val data = document.data
                    val gotName = data?.getValue("name")
                    Log.d(TAG, "Owner name: $gotName, and ID: ${document.id}")
                    binding.taskOwnerEdit.setText("For $gotName")
                }
            }

        if (taskTaker != "") {
            binding.fullTaskTakerFrame.visibility = View.VISIBLE
            binding.fullButtonEditTask.visibility = View.GONE

            db.collection("users")
                .document(taskTaker)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val data = document.data
                        val gotName = data?.getValue("name")
                        Log.d(TAG, "Taker name: $gotName, and ID: ${document.id}")
                        binding.fullTaskTakerEdit.setText("Taken by $gotName")
                    }
                }
        }

        // Adding parameter
        val currentUserId = ""
        val sharedPreferences: SharedPreferences =
            this.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        var taskId = ""

        val data = db.collection("tasks").document()

        binding.fullButtonEditTask.setOnClickListener() {

            // Changing to editable
            changeToEditable()

            binding.fullButtonDiscard.setOnClickListener {
                changeToUneditable()
            }

            binding.fullButtonSave.setOnClickListener {
                // Getting new information
                val newTaskName = binding.taskNameEdit.text.toString()
                val newTaskDescription = binding.taskDescriptionEdit.text.toString()

                // check if new start date is not earlier then old start date
                val oldStartDate = dateToTimeMap(taskStart)
                val newStartDate = dateToTimeMap(binding.taskStartDateEdit.text.toString())

                //check if new end date is equal or later then new task date
                val oldEndDate = dateToTimeMap(taskEnd)
                val newEndDate = dateToTimeMap(binding.taskEndDateEdit.text.toString())
                if (isDateValid(oldEndDate, newEndDate) && isDateValid(oldStartDate, newStartDate)) {
                    Log.d(TAG, "The dates are valid")

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

                                db.collection("tasks").document(taskId)
                                    .update(
                                        "taskName", newTaskName,
                                        "taskDescription", newTaskDescription,
                                        "startDate", newStartDate,
                                        "endDate", newEndDate
                                    )
                                    .addOnSuccessListener {
                                        Log.d(FullTaskActivity.TAG, "Task updated!!")
                                    }
                            }
                        }
                } else {
                    Log.d(TAG, "The dates are not valid")
                }

                Log.d(FullTaskActivity.TAG, "Task id: $taskId")
                Snackbar.make(binding.root, "Task updated.", Snackbar.LENGTH_LONG).show()
                changeToUneditable()
            }
        }
    }

    private fun changeToEditable() {
        // hide buttons
        binding.fullButtonEditTask.visibility = View.GONE

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
        val month: Int
        val year: Int

        if (date.length == 9) {
            month = date[3].toString().toInt()
            Log.i(TAG, "Date[3] == ${date[3]} month == $month")
            year =
                (date[5].toString() + date[6].toString() + date[7].toString() + date[8].toString()).toInt()
            Log.i(TAG, "The day: $day, month: $month, year: $year")
        } else {
            month = (date[3].toString() + date[4].toString()).toInt()
            year =
                (date[6].toString() + date[7].toString() + date[8].toString() + date[9].toString()).toInt()
            Log.i(TAG, "The day: $day, month: $month, year: $year")
        }

        val mapDate = mapOf("day" to day, "month" to month, "year" to year)
        Log.i(AddTaskActivity.TAG, "Map of date: ${mapDate.values}")
        return mapDate
    }

    private fun isDateValid(firstDate: Map<String, Int>, lastDate: Map<String, Int>): Boolean {
        // old date
        val oDay = firstDate.getValue("day")
        val oMonth = firstDate.getValue("month")
        val oYear = firstDate.getValue("year")

        // new date
        val nDay = lastDate.getValue("day")
        val nMonth = lastDate.getValue("month")
        val nYear = lastDate.getValue("year")

        var valid = false

        if (// day can be equal and bigger | month can be equal or bigger | year can be equal or bigger
            nDay >= oDay && nMonth >= oMonth && nYear >= oYear ||
            // day can be anything | month can be bigger | year can be equal or bigger
            nMonth > oMonth && nYear >= oYear ||
            // day can be anything | month can be anything | year can be bigger
            nYear > oYear) {
            valid = true
        }
        return valid
    }

    companion object {
        const val TAG = "usertask"
    }
}