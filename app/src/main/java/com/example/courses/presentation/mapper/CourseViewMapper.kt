package com.example.courses.presentation.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.courses.domain.CourseBusinessModel
import com.example.courses.presentation.models.CourseViewModel


class CourseViewMapper {
    //преобразование модели бизнес-логики в модель отображения
    @RequiresApi(Build.VERSION_CODES.O)
    fun map(data: CourseBusinessModel) : CourseViewModel{
        return CourseViewModel(data.id,
            data.title,
            data.text,
            data.price,
            data.rate,
            data.startDate.format(CourseViewModel.convertTime.formatter),
            data.hasLike,
            data.publishDate.format(CourseViewModel.convertTime.formatter)
            )
    }
}