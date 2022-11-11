package com.example.datingapp.model.repository

import androidx.paging.*
import androidx.paging.rxjava3.flowable
import com.example.datingapp.model.data.User
import com.example.datingapp.model.domain.Repository
import com.example.datingapp.model.paging.UsersPagingSource
import com.example.datingapp.model.retrofit.RemoteDataSource
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

class RepositoryImpl(val remote: RemoteDataSource) : Repository {
    override fun getUsers(): Flowable<PagingData<User>> {
        return Pager(
            config = PagingConfig(
            pageSize = 5,
            enablePlaceholders = false
        ),
            pagingSourceFactory = {
                UsersPagingSource(remote)
            }
        ).flowable
    }
}
