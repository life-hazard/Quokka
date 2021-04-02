package com.app.quokka.logged

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.app.quokka.R
import com.app.quokka.databinding.ActivityAddTaskBinding
import com.app.quokka.util.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class AddTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding

    private val sharedPrefFile = "kotlinsharedpreference"

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_task)


        binding.addButton.setOnClickListener() {

            // disable button and display loading
            showProgressBar()

            validateData()
            createTask()
        }

    }

    private fun createTask() {
        if (!validateData()) {
            Log.i(TAG, "invalid task info")
            hideProgressBar()
            return
        }
        Log.i(TAG, "valid task info")

        auth = Firebase.auth

        val sharedPreferences: SharedPreferences =
            this.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)

        val ownerId = sharedPreferences.getString("id_key", "default_value").toString()
        Log.i(TAG, "Task Owner Id value: $ownerId")

        val name = binding.taskNameEdit.text.toString().trim()
        val description = binding.taskDescriptionEdit.text.toString()
        // Change to date format
        val startDate = binding.startDateEdit.text.toString()
        //var dateStart: Timestamp
        val startDateMap = if (startDate == "") {
            dateToTimeMap(getCurrentTime().toString())
        } else {
            dateToTimeMap(startDate)
        }

        val endDate = binding.endDateEdit.text.toString()
        val endDateMap = if (endDate == "") {
            dateToTimeMap(
                mapToDate(
                    startDateMap["day"],
                    startDateMap["month"],
                    startDateMap["year"]
                )
            )
        } else {
            dateToTimeMap(endDate)
        }

        val points = binding.pointsForTask.text.toString().toInt()
        Log.i(TAG, "Points: $points")

        val db = Firebase.firestore
        Log.i(TAG, "I made a database value!!")


        val task = Task(
            name,
            description,
            startDateMap,
            endDateMap,
            points,
            ownerId,
            takerId = "",
            status = "available"
        )
        Log.i(TAG, "Task was created: $task")


        // decrease user points
        db.collection("users").document(ownerId).get().addOnSuccessListener { document ->
            if (document != null) {
                val userPoints = document.data?.getValue("points").toString().toInt()
                if (userPoints >= points) {
                    val decreasedPoints = userPoints - points
                    db.collection("users").document(ownerId).update("points", decreasedPoints)

                    // adding task
                    db.collection("tasks").add(task).addOnSuccessListener { documentReference ->
                        Log.d(TAG, "DocumentSnapshot written with ID: ${documentReference.id}")
                        Log.i("addTask", "created document")
                    }.addOnFailureListener { e ->
                        Log.w(TAG, "Error adding document", e)
                    }

                    val intent = Intent(this, UserHomePageActivity::class.java)
                    this.startActivity(intent)

                    // The snackbar disappears too quickly but the toast stays longer
                    Snackbar.make(binding.root, "Task successfully added.", Snackbar.LENGTH_LONG).show()
                    Toast.makeText(this, "Task successfully added.", Toast.LENGTH_LONG).show()

                } else {
                    Snackbar.make(binding.root, "Not enough points", Snackbar.LENGTH_LONG).show()
                    binding.addButton.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                }
            }
        }.addOnFailureListener { e ->
            Log.w("Error adding document", e)
        }
        // TODO("Change to date format")
    }

    private fun dateToTimeMap(date: String): Map<String, Int> {
        val day = (date[0].toString() + date[1].toString()).toInt()
        val month = (date[3].toString() + date[4].toString()).toInt()
        val year =
            (date[6].toString() + date[7].toString() + date[8].toString() + date[9].toString()).toInt()

        val mapDate = mapOf("day" to day, "month" to month, "year" to year)
        Log.i(TAG, "Map of date: ${mapDate.values}")
        return mapDate
    }

    private fun getCurrentTime(): String? {
        val date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
        Log.i(TAG, "Current date: $date")
        return date
    }

    private fun validateData(): Boolean {
        var valid = true

        val currentDateTime = LocalDateTime.now()

        val taskName = binding.taskNameEdit.text.toString().trim()
        val taskDescription = binding.taskDescriptionEdit.text.toString()
        var startDate = binding.startDateEdit.text.toString()
        startDate = if (startDate == "") {
            getCurrentTime().toString()
        } else binding.startDateEdit.text.toString()

        val endDate = binding.endDateEdit.text.toString()
        val taskPoints = binding.pointsForTask.text.toString()

        if (taskName.isEmpty()) {
            binding.taskNameEdit.error = "Required"
            valid = false
        }

        if (taskDescription.isEmpty()) {
            binding.taskDescriptionEdit.error = "Required"
            valid = false
        }

        if (startDate.isNotEmpty()) {
            val day = (startDate[0].toString() + startDate[1].toString()).toInt()
            val month = (startDate[3].toString() + startDate[4].toString()).toInt()
            val year =
                ((startDate[6].toString() + startDate[7].toString() + startDate[8].toString() + startDate[9].toString()).toInt())

            if ("$year-$day-$month" >= currentDateTime.format(DateTimeFormatter.ISO_DATE) && (month < 13) && (day < 32)) {
                Log.i("add task", "Day: $day, Month: $month, Year: $year")
            } else {
                binding.startDateEdit.error = "Incorrect date"
                valid = false
            }
        }

        if (endDate.isNotEmpty()) {
            val dayS = (startDate[0].toString() + startDate[1].toString()).toInt()
            val monthS = (startDate[3].toString() + startDate[4].toString()).toInt()
            val yearS =
                ((startDate[6].toString() + startDate[7].toString() + startDate[8].toString() + startDate[9].toString()).toInt())

            val dayE = (endDate[0].toString() + endDate[1].toString()).toInt()
            val monthE = (endDate[3].toString() + endDate[4].toString()).toInt()
            val yearE =
                ((endDate[6].toString() + endDate[7].toString() + endDate[8].toString() + endDate[9].toString()).toInt())


            if (yearS >= yearE && ((monthS == monthE && dayS <= dayE) || (monthS < monthE)) && (monthE < 13) && (dayE < 32)) {
                Log.i("add task", "Day: $dayE, Month: $monthE, Year: $yearE")
            } else {
                binding.endDateEdit.error = "Incorrect date"
                valid = false
            }
            // Add that a smaller day and month is acceptable if year is bigger
        }
        if (taskPoints.isNotEmpty() && taskPoints.toInt() > 0) {
            Log.i("add task", "Correct task points: $taskPoints")
        } else {
            binding.pointsForTask.error = "Incorrect amount"
            valid = false
        }
        return valid
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
        binding.addButton.visibility = View.GONE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
        binding.addButton.visibility = View.VISIBLE
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

    companion object {
        const val TAG = "addTask"
    }
}
