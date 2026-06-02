package com.example.courses.data.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.courses.data.entity.CourseDTO
import com.example.courses.data.entity.CourseDbModel
import com.example.courses.domain.CourseBusinessModel
import com.example.courses.presentation.models.CourseViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.Boolean
import kotlin.text.replace

class FavouriteCourseMapper {
    @RequiresApi(Build.VERSION_CODES.O)
    fun map(data: CourseDbModel) : CourseBusinessModel {
        //val startData = LocalDate.parse(data.startDate, DateTimeFormatter.ISO_DATE_TIME)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return CourseBusinessModel(data.id,
            data.title,
            data.text,
            data.price.replace(" ", "").toInt(),
            data.rate,
            //LocalDate.parse(data.startDate, DateTimeFormatter.ISO_DATE_TIME),
            LocalDate.parse(data.startDate, formatter),
            true,
            LocalDate.parse(data.publishDate, formatter)
            //LocalDate.parse(data.publishDate, DateTimeFormatter.ISO_DATE_TIME)
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun mapToBusinessModel(data : CourseViewModel) : CourseBusinessModel{
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return CourseBusinessModel(
            data.id,
            data.title,
            data.text,
            data.price,
            data.rate,
            LocalDate.parse(data.startDate, formatter),
            data.hasLike,
            LocalDate.parse(data.publishDate, formatter)
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun mapToDbModel(data : CourseBusinessModel) : CourseDbModel{
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return CourseDbModel(
            data.id,
            data.title,
            data.text,
            data.price.toString(),
            data.rate,
            data.startDate.format(formatter),
            data.publishDate.format(formatter)
        )
    }
}