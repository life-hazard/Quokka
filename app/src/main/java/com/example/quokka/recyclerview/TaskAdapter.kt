package com.example.quokka.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quokka.R
import kotlinx.android.synthetic.main.recyclerview_item_row.view.*
import com.example.quokka.recyclerview.TaskHolder.*
import com.example.quokka.recyclerview.TaskModel.*


class TaskAdapter(val context: Context, val taskList: ArrayList<TaskModel>): RecyclerView.Adapter<TaskAdapter.TaskHolder>() {

    var data = listOf<com.example.quokka.recyclerview.TaskHolder>()
    var query = FirebaseManager()


    inner class TaskHolder(view: View?): RecyclerView.ViewHolder(view!!) {
        val txtName = view?.findViewById<TextView>(R.id.itemTaskName)
        val txtStart = view?.findViewById<TextView>(R.id.itemTaskStartDate)
        val txtEnd = view?.findViewById<TextView>(R.id.itemTaskEndDate)
        val txtPoints = view?.findViewById<TextView>(R.id.itemTaskPoints)

        fun bind(tasks: TaskModel, context: Context) {
            txtName?.text = tasks.taskName
            txtStart?.text = tasks.taskStart
            txtEnd?.text = tasks.taskEnd
            txtPoints?.text = tasks.taskPoints.toString()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recyclerview_item_row, parent, false)
        return TaskHolder(view)
    }

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        holder?.bind(taskList[position], context)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }


//
//    internal var taskList: MutableList<com.example.quokka.recyclerview.TaskHolder>
//
//    init {
//        this.taskList = ArrayList()
//    }
//
//    val lastItemId: String?
//    get() = taskList[taskList.size - 1].taskId
//
//    fun addAll(newTasks:List<com.example.quokka.recyclerview.TaskHolder>) {
//        val init = taskList.size
//        taskList.addAll(newTasks)
//        notifyItemRangeChanged(init, newTasks.size)
//    }
//
//    fun removeListItem() {
//        taskList.removeAt(taskList.size - 1)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
//        val itemView = LayoutInflater.from(context).inflate(R.layout.recyclerview_item_row, parent, false)
//        return TaskHolder(itemView)
//    }
//
//    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
//        holder.txtName.text = taskList[position].taskName
//        holder.txtStart.text = taskList[position].taskStart
//        holder.txtEnd.text = taskList[position].taskEnd
//        holder.txtPoints.text = taskList[position].taskPoints.toString()
//    }
//
//    override fun getItemCount(): Int {
//        return taskList.size
//    }
//
//    inner class TaskHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
//        internal var txtName: TextView
//        internal var txtStart: TextView
//        internal var txtEnd: TextView
//        internal var txtPoints: TextView
//
//        init {
//            txtName = itemView.findViewById(R.id.itemTaskName)
//            txtStart = itemView.findViewById(R.id.itemStartDate)
//            txtEnd = itemView.findViewById(R.id.itemEndDate)
//            txtPoints = itemView.findViewById(R.id.itemPointsForTask)
//        }
//
//    }

}