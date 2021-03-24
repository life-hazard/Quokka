package com.example.quokka.util

import android.app.ActivityManager
import com.google.type.DateTime
import java.time.LocalDate
import java.util.*

data class Task (
    var taskName: String,
    var taskDescription: String,
    var startDate: Map<String, Int>,
    var endDate: Map<String, Int>,
    var points: Int,
    var ownerId: String,
    var takerId: String
    )