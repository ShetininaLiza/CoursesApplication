package com.example.courses.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

//моделька для БД
@Entity(tableName = "favourite_courses")
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