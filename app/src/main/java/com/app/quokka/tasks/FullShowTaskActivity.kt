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
        var taskId = ""


        binding.showButtonConfirmCompletion.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Task completion")
                .setMessage("Are you sure you want to mark the task as COMPLETE?")
                .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
                    // set in db task as completed
                    db.collection("tasks").whereEqualTo("taskName", taskName).whereEqualTo("ownerId", taskOwner).get().addOnSuccessListener { documents ->
                        for (document in documents) {
                            val currentTask = document.id
                            db.collection("tasks").document(currentTask).update("status", "complete")
                        }
                    }

                    Snackbar.make(binding.root, "TASK COMPLETED", Snackbar.LENGTH_LONG).show()
                    val intent = Intent(this, UserHomePageActivity::class.java)
                    this.startActivity(intent)
                    // do something
                })
                .setNegativeButton("No", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show()


//            val window = PopupWindow(this)
//            val view = layoutInflater.inflate(R.layout.confirm_popup, null)
//            window.contentView = view
//
//            val answerYes = view.findViewById<TextView>(R.id.popupYes)
//            answerYes.setOnClickListener {
//                Snackbar.make(binding.root, "TASK MARKED AS COMPLETE", Snackbar.LENGTH_LONG).show()
//
//                // Set task status as completed
//                val db = Firebase.firestore
//                db.collection("tasks").whereEqualTo("taskName", taskName).whereEqualTo("ownerId", taskOwner).get()
//                    .addOnSuccessListener { documents ->
//                        for (document in documents) {
//                            taskId = document.id
//                            db.collection("tasks").document(taskId).update("status", "complete")
//                        }
//                    }
//            }
//            val answerNo = view.findViewById<TextView>(R.id.popupNo)
//            answerNo.setOnClickListener {
//                window.dismiss()
//            }
//            //window.showAsDropDown(showButtonConfirmCompletion)
//            window.showAtLocation(view, Gravity.CENTER, 20,20)
        }
    }
    companion object {
        const val TAG = "completeTask"
    }
}