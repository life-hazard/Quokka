package com.example.quokka.recyclerview

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirebaseManager() {

    public val db = Firebase.firestore

    fun getAllTasks(): Task<QuerySnapshot> {
         return db.collection("tasks").get().addOnSuccessListener { documents ->
            for (document in documents) {
                Log.d("query", "${document.id} => ${document.data}")
                Log.i("query", "${document.id} => ${document.data}")
            }
        }.addOnFailureListener { e ->
            Log.w("query", "Error getting documents: ", e)
            Log.i("query", "Error getting documents: ", e)
        }
    }

    fun getTaskByUser(userId: String): Task<QuerySnapshot> {
        return db.collection("tasks")
            .whereEqualTo("ownerId", userId)
            .get().addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d("query", "${document.id} => ${document.data}")
                    Log.i("query", "${document.id} => ${document.data}")
                }
            }.addOnFailureListener { e ->
                Log.w("query", "Error getting documents: ", e)
                Log.i("query", "Error getting documents: ", e)
            }
    }
}