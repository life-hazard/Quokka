package com.example.quokka.tasks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.quokka.R
import com.example.quokka.databinding.ActivityFullShowTaskBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_full_show_task.*

class FullShowTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFullShowTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_full_show_task)

        // Taking data from CardView
        val taskName = intent.getStringExtra("fullTaskName")
        val taskDescription = intent.getStringExtra("fullTaskDescription")
        val taskStart = intent.getStringExtra("fullStartDate")
        val taskEnd = intent.getStringExtra("fullEndDate")
        val taskPoints = intent.getStringExtra("fullPoints")
        val taskOwner = intent.getStringExtra("fullTaskOwnerId")
        // Putting data from CardView into TextViews
        binding.showTaskNameEdit.setText(taskName)
        binding.showTaskDescriptionEdit.setText(taskName)
        binding.showTaskStartDateEdit.setText(taskStart)
        binding.showTaskEndDateEdit.setText(taskEnd)
        binding.showTaskOwnerEdit.setText("For $taskOwner")
        // TODO get the taskOwner name from users collection
        var taskId = ""

        binding.showButtonConfirmCompletion.setOnClickListener {
            // TODO show pop-up to confirm or cancel
            val window = PopupWindow(this)
            val view = layoutInflater.inflate(R.layout.confirm_popup, null)
            window.contentView = view

            val answerYes = view.findViewById<TextView>(R.id.popupYes)
            answerYes.setOnClickListener {
                Snackbar.make(binding.root, "TASK MARKED AS COMPLETE", Snackbar.LENGTH_LONG).show()

                // Set task status as completed
                val db = Firebase.firestore
                db.collection("tasks").whereEqualTo("taskName", taskName).whereEqualTo("ownerId", taskOwner).get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            taskId = document.id
                            db.collection("tasks").document(taskId).update("status", "complete")
                        }
                    }
            }
            val answerNo = view.findViewById<TextView>(R.id.popupNo)
            answerNo.setOnClickListener {
                window.dismiss()
            }
            //window.showAsDropDown(showButtonConfirmCompletion)
            window.showAtLocation(view, Gravity.CENTER, 20,20)
        }
    }
}