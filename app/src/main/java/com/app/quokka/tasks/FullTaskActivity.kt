package com.app.quokka.tasks

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.app.quokka.R
import com.app.quokka.databinding.ActivityFullTaskBinding
import com.app.quokka.logged.UserHomePageActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FullTaskActivity : AppCompatActivity() {

    lateinit var binding: ActivityFullTaskBinding
    private val sharedPrefFile = "kotlinsharedpreference"
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_full_task)
        Log.i("Opened", "FullTaskActivity")

        // Taking data from CardView
        val taskName = intent.getStringExtra("fullTaskName")
        val taskDescription = intent.getStringExtra("fullTaskDescription")
        val taskStart = intent.getStringExtra("fullStartDate")
        val taskEnd = intent.getStringExtra("fullEndDate")
        val taskPoints = intent.getStringExtra("fullPoints")
        val taskOwner = intent.getStringExtra("fullTaskOwnerId").toString()

        // Putting data from CardView into TextViews
        binding.fullTaskNameEdit.setText(taskName)
        binding.fullTaskDescriptionEdit.setText(taskName)
        binding.fullTaskStartDateEdit.setText(taskStart)
        binding.fullTaskEndDateEdit.setText(taskEnd)

        db.collection("users")
            .document(taskOwner)
            .get()
            .addOnSuccessListener { document ->
            if (document != null) {
                val data = document.data
                val gotName = data?.getValue("name")
                Log.d(TAG, "Owner name: $gotName, and ID: ${document.id}")
                binding.fullTaskOwnerEdit.setText("For $gotName")
            }
        }

        // Adding parameter
        val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val taskTakerId: String = sharedPreferences.getString("id_key", "default_value").toString()
        var taskId = ""

        binding.fullButtonTakeTask.setOnClickListener{
            db.collection("tasks")
                .whereEqualTo("taskName", taskName)
                .whereEqualTo("ownerId", taskOwner)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        Log.d(TAG, document.id)
                        Log.d(TAG, "Task owner: $taskOwner")
                        taskId = document.id
                        Log.d(TAG, "task id: $taskId")

                        db.collection("tasks").document(taskId)
                            .update("takerId", taskTakerId, "status", "unavailable")
                    }
                }
            Log.d(TAG, "Task id: $taskId")
            Snackbar.make(binding.root, "Task added to tasks In Progress.", Snackbar.LENGTH_LONG).show()
            intent = Intent(this, UserHomePageActivity::class.java)
            this.startActivity(intent)
        }

    }

    companion object {
        const val TAG = "fullTask"
    }
}