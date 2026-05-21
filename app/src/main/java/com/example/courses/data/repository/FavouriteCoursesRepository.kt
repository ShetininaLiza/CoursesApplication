package com.example.courses.data.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.courses.data.datastore.AppDatabase
import com.example.courses.data.mapper.FavouriteCourseMapper
import com.example.courses.domain.CourseBusinessModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavouriteCoursesRepository(
    private val database: AppDatabase
) {
    val mapper = FavouriteCourseMapper()
    val scope = CoroutineScope(Dispatchers.IO)

    @RequiresApi(Build.VERSION_CODES.O)
    fun getFavouriteCouresList() : List<CourseBusinessModel>{
        var result = emptyList<CourseBusinessModel>()
        //запускаем в общем пуле потоков
        scope.launch {
            val buf = database.courseDao().getFavouriteCourseList()
            result = buf.map { mapper.map(it) }
            Log.v("REPOSYTORY", "getRecordsList || ${result.size}")
        }
        return result
    }

    fun addFavouriteCourseList(course : CourseBusinessModel){
        //scope.launch {
        //    database.courseDao().addCourseInFavourite(course)
        //}
    }
}