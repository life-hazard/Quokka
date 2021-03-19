package com.example.quokka.recyclerview

//'''Model class''' represents data from Firebase

class TaskModel {
    var taskId: String? = null
    var taskName: String? = null
    var taskStart: String? = null
    var taskEnd: String? = null
    var taskPoints: Int? = null

    constructor() {
        // Default constructor required for calls to DataSnapshot.getValue(TaskModel.class) (i guess..)
    }

    constructor(taskId :String, taskName: String, taskStart: String, taskEnd: String, taskPoints: Int) {
        this.taskId = taskId
        this.taskName = taskName
        this.taskStart = taskStart
        this.taskEnd = taskEnd
        this.taskPoints = taskPoints
    }
}