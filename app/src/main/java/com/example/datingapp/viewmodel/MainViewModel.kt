package com.example.datingapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import androidx.paging.rxjava3.cachedIn
import com.example.datingapp.model.domain.Repository
import com.example.datingapp.model.repository.RepositoryImpl
import com.example.datingapp.model.data.User
import com.example.datingapp.model.retrofit.RemoteDataSource
import io.reactivex.rxjava3.disposables.CompositeDisposable

class MainViewModel : ViewModel() {

    val data : MutableLiveData<PagingData<User>> = MutableLiveData()
    val repository: Repository = RepositoryImpl(RemoteDataSource())
    private val composite : CompositeDisposable = CompositeDisposable()

    fun getUsers(){
        composite.add(
            repository
                .getUsers()
                .cachedIn(viewModelScope)
                .subscribe {
                    data.value = it
                }
        )
    }

    override fun onCleared() {
        super.onCleared()
        composite.clear()
    }
}