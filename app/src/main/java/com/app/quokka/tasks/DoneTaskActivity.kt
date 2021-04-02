package com.app.quokka.tasks

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.app.quokka.R
import com.app.quokka.databinding.ActivityDoneTaskBinding
import com.app.quokka.logged.FinishedTasksActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DoneTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDoneTaskBinding
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_done_task)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_done_task)

        // take owner from db and put into binding.text --> done
        // update status if button pressed
        // update user rating if button pressed

        // Taking data from CardView
        val taskName = intent.getStringExtra("fullTaskName")
        val taskDescription = intent.getStringExtra("fullTaskDescription")
        val taskStart = intent.getStringExtra("fullStartDate")
        val taskEnd = intent.getStringExtra("fullEndDate")
        val taskPoints = intent.getStringExtra("fullPoints")
        val taskOwner = intent.getStringExtra("fullTaskOwnerId").toString()
        val taskTaker = intent.getStringExtra("fullTaskTakerId").toString()

        // Showing data in .xml
        binding.doneTaskNameEdit.setText(taskName)

        // Taking taker name from db
        db.collection("users").document(taskTaker).get().addOnSuccessListener { document ->
            if (document != null) {
                val takerName = document.data?.getValue("name").toString()
                // Showing data in .xml
                binding.doneTaskDoneByEdit.setText(takerName)
            }
        }
        binding.doneButtonConfirm.setOnClickListener {
            val rating: Float = binding.doneTaskUserRatingBar.rating
            Log.d("rate", "User rated: $rating stars")
            db.collection("users").document(taskTaker).get().addOnSuccessListener { document ->
                if (document != null) {
                    val r = document.data?.getValue("rating").toString()
                    val ratingMap = r.substring(1, r.length-1).split(", ").associate {
                        val (left, right) = it.split("=")
                        left to right.toFloat()
                    }
                    Log.d("UserRating", "Rating Map: $ratingMap")
                    val newSum = ratingMap.getValue("sum") + rating
                    val newDivider = ratingMap.getValue("divider") + 1

                    val newUserRating = newSum / newDivider
                    Log.i("UserRating","New Rating: $newUserRating")

                    val newRatingMap = mapOf("rating" to newUserRating, "sum" to newSum, "divider" to newDivider)
                    db.collection("users").document(taskTaker).update("rating", newRatingMap).addOnSuccessListener {
                        Log.d("UserRating", "Document updated")

                        db.collection("tasks")
                            .whereEqualTo("name", taskName)
                            .whereEqualTo("ownerId", taskOwner)
                            .whereEqualTo("takerId", taskTaker)
                            .get()
                            .addOnSuccessListener { documents ->
                            for (document in documents) {
                                val taskId = document.id
                                db.collection("tasks").document(taskId).update("status", "archived")
                            }
                        }
                    }

                    val intent = Intent(this, FinishedTasksActivity::class.java)
                    this.startActivity(intent)
                }
            }
        }

    }
}