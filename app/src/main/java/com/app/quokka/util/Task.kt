package com.app.quokka.util

data class Task (
    var taskName: String,
    var taskDescription: String,
    var startDate: Map<String, Int>,
    var endDate: Map<String, Int>,
    var points: Int,
    var ownerId: String,
    var takerId: String,
    var status: String
    )