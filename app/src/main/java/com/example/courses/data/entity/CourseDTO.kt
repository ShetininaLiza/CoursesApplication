package com.example.courses.data.entity
//модель данных, которые мы получаем от сервера
data class CourseDTO (
    var id : Int,
    var title : String,
    var  text : String,
){
}