package com.example.moviesapp.data.repositoty

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.moviesapp.data.api.TheMovieDBInterface
import com.example.moviesapp.data.vo.popular.Movie
import io.reactivex.disposables.CompositeDisposable


class MovieDataSourceFactory(
    private val apiService: TheMovieDBInterface,
    private val compositeDisposable: CompositeDisposable
): DataSource.Factory<Int,Movie>() {

    val moviesLiveDataSource = MutableLiveData<MovieDataSource>()

    override fun create(): DataSource<Int, Movie> {
        val movieDataSource = MovieDataSource(apiService,compositeDisposable)
        moviesLiveDataSource.postValue(movieDataSource)
        return movieDataSource
    }

}