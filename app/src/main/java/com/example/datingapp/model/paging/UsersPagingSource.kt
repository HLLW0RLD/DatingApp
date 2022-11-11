package com.example.datingapp.model.paging

import android.util.Log
import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import com.example.datingapp.model.data.User
import com.example.datingapp.model.retrofit.RemoteDataSource
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class UsersPagingSource(private val remote: RemoteDataSource) : RxPagingSource<Int, User>(){

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, User>> {
        Log.d("TAG", "UsersPagingSource")
        val pageSize = params.loadSize.coerceAtMost(20)
        val pageNumber = params.key ?: 0
        return remote.getUsers(pageNumber)
            .subscribeOn(Schedulers.io())
            .map {
                LoadResult.Page(
                it.users,
                nextKey = if (it.has_more < 1) null else pageNumber + 1,
                prevKey = if (pageNumber > 1) pageNumber - 1 else null
                ) as LoadResult<Int, User>
            }
            .onErrorReturn {
                LoadResult.Error(it)
            }

//        return try {
//            val pageSize = params.loadSize.coerceAtMost(20)
//            val pageNumber = params.key ?: 0
//            Log.d("TAG", "UsersPagingSource key // ${pageNumber}")
//            val response = remote.getUsers(pageNumber)
//            Log.d("TAG", "UsersPagingSource response // ${response.users}")
//            val users = checkNotNull(response.users)
//            val morePage = response.has_more
//            val nextPageNumber = if (morePage < 1) null else pageNumber + 1
//            val prevPageNumber = if (pageNumber > 1) pageNumber - 1 else null
//            LoadResult.Page(
//                response.users,
//                prevPageNumber,
//                nextPageNumber)
//
//        } catch (e: HttpException) {
//            return LoadResult.Error(e)
//        } catch (e: Exception) {
//            return LoadResult.Error(e)
//        }
    }

    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition) ?: return null
        return anchorPage.prevKey?.plus(1) ?: anchorPage.nextKey?.minus(1)
    }
}