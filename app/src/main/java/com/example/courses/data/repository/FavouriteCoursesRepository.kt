package com.example.courses.data.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.room.Room
import com.example.courses.data.datastore.AppDatabase
import com.example.courses.data.mapper.FavouriteCourseMapper
import com.example.courses.domain.CourseBusinessModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavouriteCoursesRepository(

) {
    lateinit var  database: AppDatabase

    constructor(database: AppDatabase) : this() {
        this.database = database
    }
    val mapper = FavouriteCourseMapper()
    val scope = CoroutineScope(Dispatchers.IO)

    @RequiresApi(Build.VERSION_CODES.O)
    fun getFavouriteCouresList() : List<CourseBusinessModel>{
        /*
        var result = emptyList<CourseBusinessModel>()
        //запускаем в общем пуле потоков
        scope.launch {
            val buf = database.courseDao().getFavouriteCourseList()
            result = buf.map { mapper.map(it) }
            Log.v("REPOSYTORY", "REPOSYTORY || getRecordsList (1) || ${result.size}")
        }
        Log.v("REPOSYTORY", "REPOSYTORY || getRecordsList (2) || ${result.size}")
        return result
        */
        var result = emptyList<CourseBusinessModel>()
        val buf = database.courseDao().getFavouriteCourseList()
        result = buf.map { mapper.map(it) }
        return result
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addFavouriteCourseList(course : CourseBusinessModel){
        Log.v("FavouriteCoursesRepository", "FavouriteCoursesRepository || addFavouriteCourseList")
        scope.launch {
            Log.v("FavouriteCoursesRepository", "FavouriteCoursesRepository || 1")
            val dao = database.courseDao()
            Log.v("FavouriteCoursesRepository", "FavouriteCoursesRepository || 2")
            val mapperModel = mapper.mapToDbModel(course)
            Log.v("FavouriteCoursesRepository", "FavouriteCoursesRepository || 3")
            Log.v("FavouriteCoursesRepository", "FavouriteCoursesRepository || ${mapperModel.toString()}")
            dao.addCourseInFavourite(mapperModel)
            Log.v("FavouriteCoursesRepository", "FavouriteCoursesRepository || SAVE IN FAVOURITE COURSE")
        }
    }

    fun removeFavouriteCourseList(course : CourseBusinessModel){
        Log.v("FavouriteCoursesRepository", "FavouriteCoursesRepository || removeFavouriteCourseList")
        scope.launch {
            Log.v("FavouriteCoursesRepository", "remove || 1")
            val dao = database.courseDao()
            Log.v("FavouriteCoursesRepository", "remove || 2")
            val mapperModel = mapper.mapToDbModel(course)
            Log.v("FavouriteCoursesRepository", "remove || 3")
            Log.v("FavouriteCoursesRepository", "remove || ${mapperModel.toString()}")
            dao.deleteCourseFromFavourite(mapperModel)
            Log.v("FavouriteCoursesRepository", "remove || SAVE IN FAVOURITE COURSE")
        }
    }
}