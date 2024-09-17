package com.example.moviesapp.data.repositoty

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.moviesapp.data.api.TheMovieDBInterface
import com.example.moviesapp.data.vo.detail.MovieDetail
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MovieDetailNetworkDataSource(
    private val apiService: TheMovieDBInterface,
    private val compositeDisposable: CompositeDisposable
) {
    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState> = _networkState

    private val _downloadedMovieDetailsResponse = MutableLiveData<MovieDetail>()
    val downloadedMovieDetailsResponse: LiveData<MovieDetail> = _downloadedMovieDetailsResponse

    fun fetchMovieDetail(movieId: Int) {
        _networkState.postValue(NetworkState.LOADING)

        try {
            compositeDisposable.addAll(
                apiService.getMovieDetail(movieId)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            _downloadedMovieDetailsResponse.postValue(it)
                            _networkState.postValue(NetworkState.LOADED)
                        },
                        {
                            _networkState.postValue(NetworkState.ERROR)
                            Log.e("MovieDetailsDataSource", it.message ?: "undefined error")
                        })
            )
        } catch (e: Exception) {
            Log.e("MovieDetailsDataSource", e.message ?: "undefined error")
        }
    }
}