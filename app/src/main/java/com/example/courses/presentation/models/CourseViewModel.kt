package com.example.courses.presentation.models

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.Format
import java.time.LocalDate
import java.time.format.DateTimeFormatter
data class CourseViewModel(
    var id : Int,
    var title : String,
    var text : String,
    var price : Int,
    val rate : Double,
    val startDate : String,
    val hasLike : Boolean,
    val publishDate: String,
) {
    object convertTime{
        @RequiresApi(Build.VERSION_CODES.O)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    }
}