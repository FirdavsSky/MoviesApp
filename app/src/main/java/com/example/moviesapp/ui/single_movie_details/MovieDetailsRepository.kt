package com.example.moviesapp.ui.single_movie_details

import androidx.lifecycle.LiveData
import com.example.moviesapp.data.api.TheMovieDBInterface
import com.example.moviesapp.data.repositoty.MovieDetailNetworkDataSource
import com.example.moviesapp.data.repositoty.NetworkState
import com.example.moviesapp.data.vo.detail.MovieDetail
import io.reactivex.disposables.CompositeDisposable

class MovieDetailsRepository(
    private val apiService: TheMovieDBInterface
) {
    lateinit var movieDetailNetworkDataSource: MovieDetailNetworkDataSource

    fun fetchSingleMovieDetails(
        compositeDisposable: CompositeDisposable,
        movieId: Int
    ): LiveData<MovieDetail>{
        movieDetailNetworkDataSource = MovieDetailNetworkDataSource(apiService,compositeDisposable)
        movieDetailNetworkDataSource.fetchMovieDetail(movieId)

        return movieDetailNetworkDataSource.downloadedMovieDetailsResponse
    }

    fun getMovieDetailsNetworkState(): LiveData<NetworkState>{
        return movieDetailNetworkDataSource.networkState
    }
}