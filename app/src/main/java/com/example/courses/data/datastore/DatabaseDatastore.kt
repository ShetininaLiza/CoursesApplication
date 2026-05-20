package com.example.courses.data.datastore

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.courses.data.entity.CourseDbModel

@Dao
interface DatabaseDatastore {
    @Query("SELECT * FROM courses_favourite")
    //метод для получения списка заметок
    fun getFavouriteCourseList() : List<CourseDbModel>

    @Insert
    //метод для добавления записи
    fun addCourseInFavourite(course : CourseDbModel)

    @Delete
    //метод для удаления записи
    fun deleteCourseFromFavourite(course : CourseDbModel)

    @Query("DELETE FROM courses_favourite WHERE id = :courseId")
    fun deleteCourseFromFavouriteById(courseId : Int)
}