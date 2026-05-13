package com.example.courses.domain

import java.time.LocalDate

data class CourseBusinessModel(
    var id : Int,
    var title : String,
    var text : String,
    var price : Int,
    val rate : Double,
    val startDate : LocalDate,
    val hasLike : Boolean,
    val publishDate: LocalDate
) {
}