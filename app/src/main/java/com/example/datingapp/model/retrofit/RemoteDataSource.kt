package com.example.datingapp.model.retrofit

import android.util.Log
import com.example.datingapp.model.data.Users
import com.example.datingapp.model.domain.ImageApi
import com.google.gson.GsonBuilder
import io.reactivex.rxjava3.core.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

private const val URL = "https://en8717w8khxi59n.m.pipedream.net/"

class RemoteDataSource {

    private val imageAPI : ImageApi = Retrofit.Builder()
        .baseUrl(URL)
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().setLenient().create()
            )
        )
        .build().create(ImageApi::class.java)

    fun getUsers(page : Int): Single<Users> {
        val response = imageAPI.getUsers(page)
        Log.d("TAG", response.toString())
        return response
    }
}