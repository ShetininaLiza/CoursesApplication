package com.example.courses.data.entity
//модель данных, которые мы получаем от сервера
data class CourseDTO (
    val id : Int,
    val title : String,
    val  text : String,
    val price : String,
    val rate : Double,
    val startDate : String,
    val hasLike : Boolean,
    val publishDate: String
){
}
