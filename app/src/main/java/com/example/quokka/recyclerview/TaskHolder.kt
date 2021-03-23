package com.example.quokka.recyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recyclerview_item_row.view.*

// ViewHolder class contains Android UI fields that point to layout items

class TaskHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

//    fun bindTask(task: TaskModel?) {
//        with(task!!) {
//            itemView.itemTaskName.text = taskName
//            itemView.itemStartDate.text = taskStart
//            itemView.itemEndDate.text = taskEnd
//            itemView.itemPointsForTask.text = taskPoints.toString()
//        }
//    }
}