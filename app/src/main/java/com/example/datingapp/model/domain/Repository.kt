package com.example.datingapp.model.domain

import androidx.paging.PagingData
import com.example.datingapp.model.data.User
import io.reactivex.rxjava3.core.Flowable

interface Repository {
    fun getUsers() : Flowable<PagingData<User>>
}