package com.example.moviesapp.ui.single_movie_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.moviesapp.data.repositoty.NetworkState
import com.example.moviesapp.data.vo.detail.MovieDetail
import io.reactivex.disposables.CompositeDisposable

class SingleMovieViewModel(
    private val movieRepository: MovieDetailsRepository,
    movieId: Int
): ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    val movieDetails: LiveData<MovieDetail> by lazy {
        movieRepository.fetchSingleMovieDetails(compositeDisposable,movieId)
    }

    val networkState: LiveData<NetworkState> by lazy {
        movieRepository.getMovieDetailsNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}