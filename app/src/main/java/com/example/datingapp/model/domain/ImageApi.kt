package com.example.datingapp.model.domain

import com.example.datingapp.model.data.Users
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ImageApi {
    @GET("https://en8717w8khxi59n.m.pipedream.net/")
    fun getUsers(@Query("page") page : Int): Single<Users>
}