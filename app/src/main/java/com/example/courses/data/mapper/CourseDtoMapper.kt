package com.example.courses.data.mapper

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.courses.data.entity.CourseDTO
import com.example.courses.domain.CourseBusinessModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CourseDtoMapper {

    //@RequiresApi(Build.VERSION_CODES.O)
    @RequiresApi(Build.VERSION_CODES.O)
    fun map(data: CourseDTO) : CourseBusinessModel {
        //val startData = LocalDate.parse(data.startDate, DateTimeFormatter.ISO_DATE_TIME)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return CourseBusinessModel(data.id,
            data.title,
            data.text,
            data.price.replace(" ", "").toInt(),
            data.rate,
            //LocalDate.parse(data.startDate, DateTimeFormatter.ISO_DATE_TIME),
            LocalDate.parse(data.startDate, formatter),
            data.hasLike,
            LocalDate.parse(data.publishDate, formatter)
            //LocalDate.parse(data.publishDate, DateTimeFormatter.ISO_DATE_TIME)
            )
    }
}