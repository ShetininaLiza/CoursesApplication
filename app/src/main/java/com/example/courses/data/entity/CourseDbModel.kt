package com.example.courses.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

//моделька для БД
@Entity(tableName = "courses_favourite")
class CourseDbModel(
    @PrimaryKey
    val id : Int,
    val title : String,
    val  text : String,
    val price : String,
    val rate : Double,
    val startDate : String,
    val publishDate: String
)