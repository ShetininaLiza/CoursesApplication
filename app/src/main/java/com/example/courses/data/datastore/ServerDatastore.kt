package com.example.courses.data.datastore

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

//import okhttp3.Call
//import okhttp3.Callback
import okhttp3.ResponseBody
//import okhttp3.Response

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import okhttp3.OkHttpClient

import retrofit2.Retrofit


import retrofit2.converter.gson.GsonConverterFactory
import  com.example.courses.data.entity.CourseDTO
import com.example.courses.data.entity.DataDTO
import com.example.courses.data.mapper.CourseDtoMapper
import com.example.courses.domain.CourseBusinessModel
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.withContext

val testJSON=
    "{" +
       "\"courses\": [" +
            "{"+
                "\"id\": 101,"+
                "\"title\": \"3D-дженералист\","+
                "\"text\": \"text\","+
                "\"price\": \"12 000\","+
                "\"rate\": \"3.9\","+
                "\"startDate\": \"2024-09-10\","+
                "\"hasLike\": false,"+
                "\"publishDate\": \"2024-01-20\""+
            "},"+
            "{"+
                "\"id\": 102,"+
                "\"title\": \"3D-дженералист\","+
                "\"text\": \"text\","+
                "\"price\": \"12 000\","+
                "\"rate\": \"3.9\","+
                "\"startDate\": \"2024-09-10\","+
                "\"hasLike\": false,"+
                "\"publishDate\": \"2024-01-20\""+
            "},"+
            "{"+
                "\"id\": 103,"+
                "\"title\": \"3D-дженералист\","+
                "\"text\": \"text\","+
                "\"price\": \"12 000\","+
                "\"rate\": \"3.9\","+
                "\"startDate\": \"2024-09-10\","+
                "\"hasLike\": false,"+
                "\"publishDate\": \"2024-01-20\""+
            "}"+
            "]"+
      "}"

class ServerDatastore {
    val mapper = CourseDtoMapper()
    val gson = GsonBuilder().create()
    val mutex = Mutex()

    sealed class CoursesResult<out T> {
        data class Success<out T>(val data: T) : CoursesResult<T>()
        data class Failure(val exception: Exception) : CoursesResult<Nothing>()
    }

    private fun createHttpClient() : OkHttpClient{
        return OkHttpClient.Builder()
            .build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun sendRequest() : List<CourseBusinessModel>{
        //var result: Result<List<CourseBusinessModel>> = Result.failure(Exception())
        val liveData = MutableLiveData<List<CourseBusinessModel>>()
        var courseList : List<CourseBusinessModel> = emptyList()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://drive.usercontent.google.com/u/0/")
            .client(createHttpClient())
            //.addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val courseApi = retrofit.create(ApiService::class.java)
        val call = courseApi.getData()
        //val response = courseApi.getData().execute() // Блокирующий вызов
        Log.v("Datastore", "__________________CALL")

        //ЭТО НАДО ДЛЯ ПРОВЕРКИ
        //var list = gson.fromJson<DataDTO>(testJSON, DataDTO::class.java)
        //Log.v("MainActivity", "Test_JSON: $testJSON")
        //Log.v("MainActivity", "List: ${list.courses.size}")
        //Попытка выполнить запрос асинхронно
            call.enqueue(object : Callback<ResponseBody> {
                @SuppressLint("NewApi")
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        // Получаем сырой ответ
                        val responseBody = response.body()?.string()
                        Log.d("MainActivity", "Response: $responseBody")
                        val res = gson.fromJson<DataDTO>(responseBody, DataDTO::class.java)
                        if (!res.courses.isNullOrEmpty())
                            courseList = res.courses.map {
                                mapper.map(it)
                            }
                        Log.v("MainActivity", "DATASTORE || Res: ${res.courses.size}")
                        //result = Result.success(courseList)
                    } else {
                        // Обработка ошибки
                        Log.d("MainActivity", "Request failed: ${response.code()}")
                        //result = Result.failure(Exception("HTTP error: ${response.code()}"))
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.v(
                        "MainActivity",
                        "___________onFailure ___${call.request().body.toString()}"
                    )
                    // Обработка ошибки
                    Log.d("MainActivity", "Network request failed: ${t.message}")
                    //result = Result.failure(Exception("Network request failed: ${t.message}"))
                }
            })
        //liveData.value = courseList
        //Log.v("DATASTORE", "DATASTRE || SAVE IN VIEW MODEL ${liveData.value?.size}")
        //return liveData
        return courseList
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getCoursesData(): CoursesResult<List<CourseBusinessModel>> = withContext(Dispatchers.IO) {
        try {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://drive.usercontent.google.com/u/0/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

            val courseApi = retrofit.create(ApiService::class.java)
            val response = courseApi.getData().execute() // Блокирующий вызов

            if (response.isSuccessful) {
                val responseBody = response.body()?.string() ?: ""
                Log.w("get Data", "getData")
                var courseList : List<CourseBusinessModel> = emptyList()
                val res = gson.fromJson<DataDTO>(responseBody, DataDTO::class.java)
                if (!res.courses.isNullOrEmpty())
                    courseList = res.courses.map {
                        mapper.map(it)
                    }
                CoursesResult.Success(courseList)
            } else {
                CoursesResult.Failure(Exception("HTTP error: ${response.code()}"))
            }
        } catch (e: Exception) {
            CoursesResult.Failure(e)
        }
    }

    suspend fun readData(call : Call<ResponseBody>) : List<CourseBusinessModel>{
        Log.v("READ DATA", "READ DATA")
        var courseList : List<CourseBusinessModel> = emptyList()
        call.enqueue(object : Callback<ResponseBody> {
            @SuppressLint("NewApi")
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
               Log.v("Read data", "onResponse")
                if (response.isSuccessful) {
                    // Получаем сырой ответ
                    val responseBody = response.body()?.string()
                    Log.d("MainActivity", "Response: $responseBody")
                    val res = gson.fromJson<DataDTO>(responseBody, DataDTO::class.java)
                    if (!res.courses.isNullOrEmpty())
                        courseList = res.courses.map {
                            mapper.map(it)
                        }
                    Log.v("MainActivity", "Res: ${res.courses.size}")
                } else {
                    // Обработка ошибки
                    Log.d("MainActivity", "Request failed: ${response.code()}")
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.v(
                    "MainActivity",
                    "___________onFailure ___${call.request().body.toString()}"
                )
                // Обработка ошибки
                Log.d("MainActivity", "Network request failed: ${t.message}")
            }
        })
        return courseList
    }
}