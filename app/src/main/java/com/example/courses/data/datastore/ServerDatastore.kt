package com.example.courses.data.datastore

import android.annotation.SuppressLint
import android.util.Log

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
    private fun createHttpClient() : OkHttpClient{
        return OkHttpClient.Builder()
            .build()
    }

    fun sendRequest() : List<CourseBusinessModel>{
        var courseList : List<CourseBusinessModel> = emptyList()

        val gson = GsonBuilder().create();
        val retrofit = Retrofit.Builder()
            .baseUrl("https://drive.usercontent.google.com/u/0/")
            .client(createHttpClient())
            //.addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val courseApi: ApiService = retrofit.create(ApiService::class.java)
        val call = courseApi.getData()
        Log.v("Datastore", "__________________CALL")

        //ЭТО НАДО ДЛЯ ПРОВЕРКИ
        //var list = gson.fromJson<DataDTO>(testJSON, DataDTO::class.java)
        //Log.v("MainActivity", "Test_JSON: $testJSON")
        //Log.v("MainActivity", "List: ${list.courses.size}")

        call.enqueue(object : Callback<ResponseBody> {
            @SuppressLint("NewApi")
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    // Получаем сырой ответ
                    val responseBody = response.body()?.string()
                    Log.d("MainActivity", "Response: $responseBody")
                    val res = gson.fromJson<DataDTO>(responseBody, DataDTO::class.java)
                    if(!res.courses.isNullOrEmpty())
                        courseList = res.courses.map{
                            mapper.map(it)
                        }
                    Log.v("MainActivity", "Res: ${res.courses.size}")
                } else {
                    // Обработка ошибки
                    Log.d("MainActivity", "Request failed: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.v("MainActivity", "___________onFailure ___${call.request().body.toString()}")
                // Обработка ошибки
                Log.d("MainActivity", "Network request failed: ${t.message}")
            }
        })
        return courseList
    }
}