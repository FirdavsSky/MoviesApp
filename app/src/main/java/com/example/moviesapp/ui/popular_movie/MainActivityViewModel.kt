package com.example.moviesapp.ui.popular_movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.moviesapp.data.repositoty.NetworkState
import com.example.moviesapp.data.vo.popular.Movie
import com.example.moviesapp.ui.single_movie_details.MovieDetailsRepository
import io.reactivex.disposables.CompositeDisposable

class MainActivityViewModel(
    private val movieRepository: MoviePageListRepository
): ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    val moviePagedList: LiveData<PagedList<Movie>> by lazy {
        movieRepository.fetchLiveMoviePagedList(compositeDisposable)
    }

    val networkState: LiveData<NetworkState> by lazy {
        movieRepository.getNetworkState()
    }

    fun listIsEmpty(): Boolean{
        return moviePagedList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()

        compositeDisposable.dispose()
    }
}