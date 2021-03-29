package com.example.quokka.tasks

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.example.quokka.R
import com.example.quokka.databinding.ActivityFullTaskBinding
import com.example.quokka.logged.UserHomePageActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FullTaskActivity : AppCompatActivity() {

    lateinit var binding: ActivityFullTaskBinding
    private val sharedPrefFile = "kotlinsharedpreference"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_full_task)

        // Taking data from CardView
        val taskName = intent.getStringExtra("fullTaskName")
        val taskDescription = intent.getStringExtra("fullTaskDescription")
        val taskStart = intent.getStringExtra("fullStartDate")
        val taskEnd = intent.getStringExtra("fullEndDate")
        val taskPoints = intent.getStringExtra("fullPoints")
        val taskOwner = intent.getStringExtra("fullTaskOwnerId")
        // Putting data from CardView into TextViews
        binding.fullTaskName.text = taskName
        binding.fullTaskDescription.text = taskDescription
        binding.fullStartDate.text = taskStart
        binding.fullEndDate.text = taskEnd
        binding.fullTaskOwner.text = "For $taskOwner"
        // TODO get the taskOwner name from users collection

        // Adding parameter
        val currentUserId = ""
        val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        var taskTakerId: String = sharedPreferences.getString("id_key", "default_value").toString()
        var taskId = ""

        val db = Firebase.firestore

        val data = db.collection("tasks").document()


        binding.fullButtonTakeTask.setOnClickListener(){
//            db.collection("tasks").document(taskId).update("takerId", currentUserId).addOnSuccessListener { document ->
//                binding.fullTaskOwner.text = taskOwner
//                Log.d(TAG, "Updated task: $document")
//            }
            //binding.fullTaskOwner.text = taskOwner
            //Log.d(TAG, "Updated task")
            val doc = db.collection("tasks")
                .whereEqualTo("taskName", taskName)
                .whereEqualTo("ownerId", taskOwner)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        Log.d(TAG, "${document.id}")
                        Log.d(TAG, "Task owner: $taskOwner")
                        taskId = document.id
                        Log.d(TAG, "task id: $taskId")

                        db.collection("tasks").document(taskId).update("takerId", taskTakerId)
                    }
                }
            Log.d(TAG, "Task id: $taskId")
            Snackbar.make(binding.root, "Task added to tasks In Progress.", Snackbar.LENGTH_LONG).show()
            intent = Intent(this, UserHomePageActivity::class.java)
            this.startActivity(intent)
        }



    }

    companion object {
        const val TAG = "ftask"
    }
}