package com.example.quokka.logged

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.quokka.FullTaskActivity
import com.example.quokka.R
import com.example.quokka.databinding.FragmentUserTasksBinding
import com.example.quokka.recyclerview.TaskAdapter
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_task.*
import kotlinx.android.synthetic.main.fragment_user_tasks.*

//  Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UserTasksFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 * Holds the tasks made by the logged user
 */
data class UserTaskModel(
    val taskName: String = "",
    var taskDescription: String = "",
    val startDate: Map<String, Int> = mapOf("k" to -1),
    val endDate: Map<String, Int> = mapOf("k" to -1),
    val points: Int = -1,
    var ownerId: String = ""
)

class UserTaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    init {
        itemView.setOnClickListener() {
            Snackbar.make(itemView, "CLICKED", Snackbar.LENGTH_SHORT).show()
        }
    }
}

class UserTasksFragment : Fragment() {

    private lateinit var binding: FragmentUserTasksBinding

    lateinit var adapter: TaskAdapter

    val db = Firebase.firestore

    private val sharedPrefFile = "kotlinsharedpreference"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentUserTasksBinding>(
            inflater,
            R.layout.fragment_user_tasks,
            container,
            false
        )

        // getting from shared preferences


        val sharedPreferences: SharedPreferences =
            requireActivity().getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val sharedIdValue = sharedPreferences.getString("id_key", "default_value")

        Log.d(TAG, "The id from shared preferences: ${sharedIdValue.toString()}")

        val query = db.collection("tasks").whereEqualTo("ownerId", sharedIdValue)
        val options = FirestoreRecyclerOptions.Builder<UserTaskModel>().setQuery(
            query,
            UserTaskModel::class.java
        ).setLifecycleOwner(this).build()

        val adapter = object: FirestoreRecyclerAdapter<UserTaskModel, UserTaskViewHolder>(options) {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserTaskViewHolder {
                val view: View = LayoutInflater.from(context).inflate(
                    R.layout.recyclerview_task_item,
                    parent,
                    false
                )
                return UserTaskViewHolder(view)
            }

            override fun onBindViewHolder(
                holder: UserTaskViewHolder,
                position: Int,
                model: UserTaskModel
            ) {
                val tvName: TextView = holder.itemView.findViewById(R.id.itemTaskName)
                val tvStartDate: TextView = holder.itemView.findViewById(R.id.itemTaskStartDate)
                val tvEndDate: TextView = holder.itemView.findViewById(R.id.itemTaskEndDate)
                val tvPoints: TextView = holder.itemView.findViewById(R.id.itemTaskPoints)


                //Log.i(TAG, "The task id: ${model.taskId}")
                tvName.text = model.taskName
                val start = "${model.startDate["day"]}.${model.startDate["month"]}.${model.startDate["year"]}"
                tvStartDate.text = start
                //tvStartDate.text = model.startDate.values.toString()
                val end = "${model.startDate["day"]}.${model.startDate["month"]}.${model.startDate["year"]}"
                tvEndDate.text = end
                //tvEndDate.text = model.endDate.values.toString()
                tvPoints.text = model.points.toString()

                holder.itemView.setOnClickListener() { view ->
//                    val navController = Navigation.findNavController(view)
//                    navController.navigate(R.id.action_userTasksFragment_to_fullTaskFragment)
                    Toast.makeText(context, "CLICK!!", Toast.LENGTH_SHORT).show()
                    //Log.i(TAG, "The task id: ${}")
                    //view.findNavController().navigate(R.id.action_userTasksFragment_to_fullTaskFragment)
//                    val intent = Intent(context, FullTaskFragment::class.java)
//                    context?.startActivity(intent)
                    val intent = Intent(context, FullTaskActivity::class.java)
                    intent.putExtra("fullTaskName", model.taskName) // put image data in Intent
                    intent.putExtra("fullTaskDescription", model.taskDescription)
                    intent.putExtra("fullStartDate", start)
                    intent.putExtra("fullEndDate", end)
                    intent.putExtra("fullPoints", model.points)
                    intent.putExtra("fullTaskOwnerId", model.ownerId)



                    context!!.startActivity(intent) // start Intent


                }
            }


        }

        binding.userTasksRecyclerView.adapter = adapter

        return binding.root
    }

    companion object {
        const val TAG = "tagz"
    }
}