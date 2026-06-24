package com.example.courses.presentation

import android.app.ProgressDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.transition.Visibility
import com.example.courses.R
import com.example.courses.data.datastore.AppDatabase
import com.example.courses.data.datastore.ServerDatastore
import com.example.courses.data.datastore.ServerDatastore.CoursesResult
import com.example.courses.data.repository.FavouriteCoursesRepository
import com.example.courses.databinding.ActivityMainBinding
import com.example.courses.domain.CourseBusinessModel
import com.example.courses.presentation.customView.CourseItemViewAdapter
import com.example.courses.presentation.mapper.CourseViewMapper
import com.example.courses.presentation.models.CourseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import okhttp3.internal.wait

class CoursesFragment : Fragment(R.layout.fragment_courses) {
    private val scope = CoroutineScope(Dispatchers.IO)
    val mapper = CourseViewMapper()
    val store = ServerDatastore()
    val mutex = Mutex()
    lateinit var courseAdapter: CourseItemViewAdapter
    var courseList = emptyList<CourseViewModel>()
    var favoruriteCoursesList: List<CourseViewModel> = emptyList()
    lateinit var coursesRecycle: RecyclerView
    var progressLoad : ProgressBar? = null
    lateinit var repository: FavouriteCoursesRepository

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //var courseList = emptyList<CourseViewModel>()
        Log.v("CoursesFragment", "Try get Course List")
        val db = context.let {
            Room.databaseBuilder(it!!, AppDatabase::class.java, "favourites_courses.db")
                //.fallbackToDestructiveMigration(true)
                .build() }
        repository = db.let {FavouriteCoursesRepository(it)}
        courseAdapter = CourseItemViewAdapter(repository)
        //загрузка
        progressLoad = view?.findViewById(R.id.progressLoad)
        //отобоажаем загрузку
        progressLoad?.visibility = View.VISIBLE
        runBlocking {
            load()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun load(){
        scope.async {
            Log.v("FRAGMENT", "FRAGMENT_LAUNCH || LOAD")
            readData()
            readFavouriteCourse()
            Log.v("FRAGMENT", "FRAGMENT_LAUNCH || LOAD DATA")
        }
    }
    //метод для получения списка избранных курсов
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun readFavouriteCourse(){
        scope.launch {
            Log.v("FRAGMENT", "FRAGMENT || readFavouriteCourse")
            val buf = repository.getFavouriteCouresList()
            favoruriteCoursesList = buf.map { mapper.map(it) }
        }.join()
        Log.v("FRAGMENT", "FRAGMENT || FavouriteCourse_SIZE ${favoruriteCoursesList.size}")
    }

    //метод для получения курсов
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun readData(){
        val job = scope.launch {
            // Ждём завершения запроса
            val result = store.getCoursesData()
            //когда все законсчилось
            when (result) {
                is CoursesResult.Success -> {
                    Log.v("FRAGMENT", "get Course List")
                    val courses = result.data
                    Log.v("FRAGMENT", "get Course List (buf) || ${courses.size}")
                    courseList = courses.map { mapper.map(it) }
                    Log.v("FRAGMENT", "get Course List || ${courseList.size}")
                }

                is CoursesResult.Failure -> {
                    Log.v("FRAGMENT", "get Course List || ERROR ${result.exception.message}")
                    //showError("Ошибка загрузки: ${result.exception.message}")
                }
            }
            Log.v("FRAGMENT", "FRAGMENT || readData")
        }
        job.join()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.v("CoursesFragment", "CoursesFragment || onViewCreated")
        Log.v("LIST", "FAVOURITE COURSE LIST || ${favoruriteCoursesList?.size}")
        Log.v("LIST", "COURSE LIST || ${courseList.size}")

        //скрываем загрузку
        progressLoad?.setVisibility(View.GONE)

        //список курсов
        coursesRecycle = view.findViewById(R.id.coursesList)

        coursesRecycle.layoutManager = LinearLayoutManager(view.context)
        courseAdapter.courseList = courseList

        if (favoruriteCoursesList.isNullOrEmpty())
            favoruriteCoursesList = emptyList()
        courseAdapter.favouriteCourseList = favoruriteCoursesList
        //courseAdapter.repository = repository
        coursesRecycle.adapter = courseAdapter
        Log.v("FRAGMENT", "FRAGMENT || onViewCreated (2)")
    }
}