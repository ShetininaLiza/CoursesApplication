package com.example.courses.presentation

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.annotation.RequiresApi
import com.example.courses.R
import com.example.courses.data.datastore.ServerDatastore
import com.example.courses.data.datastore.ServerDatastore.CoursesResult
import com.example.courses.domain.CourseBusinessModel
import com.example.courses.presentation.mapper.CourseViewMapper
import com.example.courses.presentation.models.CourseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class CoursesFragment : Fragment(R.layout.fragment_courses) {
    private val scope = CoroutineScope(Dispatchers.IO)
    val mapper = CourseViewMapper()
    val store = ServerDatastore()
    val mutex = Mutex()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var courseList = emptyList<CourseViewModel>()
        Log.v("CoursesFragment", "Try get Course List")
        scope.launch {
            // Ждём завершения запроса
            val result = store.getCoursesData()
            //когда все законсчилось
            when (result) {
                is CoursesResult.Success -> {
                    var courses = result.data.toList()
                    Log.v("FRAGMENT", "get Course List || ${courses.size}")
                }

                is CoursesResult.Failure -> {
                    Log.v("FRAGMENT", "get Course List || ERROR ${result.exception.message}")
                    //showError("Ошибка загрузки: ${result.exception.message}")
                }
            }
        }
    }
}