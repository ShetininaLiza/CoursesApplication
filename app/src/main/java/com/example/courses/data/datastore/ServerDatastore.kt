package com.example.courses.data.datastore

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

class ServerDatastore {
    private fun createHttpClient() : OkHttpClient{
        return OkHttpClient.Builder()
            .build()
    }

    fun sendRequest(){
        val retrofit = Retrofit.Builder()
            .baseUrl("https://drive.usercontent.google.com/u/0/")
            .client(createHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val courseApi: ApiService = retrofit.create(ApiService::class.java)
        val call = courseApi.getData()
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    // Получаем сырой ответ
                    val responseBody = response.body()?.string()
                    Log.d("MainActivity", "Response: $responseBody")
                } else {
                    // Обработка ошибки
                    Log.d("MainActivity", "Request failed: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Обработка ошибки
                Log.d("MainActivity", "Network request failed: ${t.message}")
            }
        })

    }
}