package com.example.courses.data.datastore

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.courses.data.entity.CourseDbModel

@Database(entities = [CourseDbModel::class], version = 1)
abstract class AppDatabase : RoomDatabase(){
    abstract  fun courseDao(): DatabaseDatastore
}