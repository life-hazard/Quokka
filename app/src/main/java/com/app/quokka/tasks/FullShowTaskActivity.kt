package com.app.quokka.tasks

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.app.quokka.R
import com.app.quokka.databinding.ActivityFullShowTaskBinding
import com.app.quokka.logged.UserHomePageActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FullShowTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFullShowTaskBinding
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_full_show_task)
        Log.i("Opened", "FullShowTaskActivity")

        // Taking data from CardView
        val taskName = intent.getStringExtra("fullTaskName")
        val taskDescription = intent.getStringExtra("fullTaskDescription")
        val taskStart = intent.getStringExtra("fullStartDate")
        val taskEnd = intent.getStringExtra("fullEndDate")
        val taskPoints = intent.getStringExtra("fullPoints")
        val taskOwner = intent.getStringExtra("fullTaskOwnerId").toString()

        // Putting data from CardView into TextViews
        binding.showTaskNameEdit.setText(taskName)
        binding.showTaskDescriptionEdit.setText(taskName)
        binding.showTaskStartDateEdit.setText(taskStart)
        binding.showTaskEndDateEdit.setText(taskEnd)
        db.collection("users")
            .document(taskOwner)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val data = document.data
                    val gotName = data?.getValue("name")
                    Log.d(TAG, "Owner name: $gotName, and ID: ${document.id}")
                    binding.showTaskOwnerEdit.setText("For $gotName")
                }
            }

        binding.showButtonConfirmCompletion.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Task completion")
                .setMessage("Are you sure you want to mark the task as COMPLETE?")
                .setPositiveButton("Yes", DialogInterface.OnClickListener { _, _ ->

                    // set in db task as completed
                    db.collection("tasks").whereEqualTo("taskName", taskName)
                        .whereEqualTo("ownerId", taskOwner).get()
                        .addOnSuccessListener { documents ->
                            for (document in documents) {
                                val currentTask = document.id

                                db.collection("tasks").document(currentTask)
                                    .update("status", "complete")
                            }
                        }

                    Snackbar.make(binding.root, "TASK COMPLETED", Snackbar.LENGTH_LONG).show()
                    val intent = Intent(this, UserHomePageActivity::class.java)
                    this.startActivity(intent)
                })
                .setNegativeButton("No", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show()
        }
    }

    companion object {
        const val TAG = "completeTask"
    }
}