package com.example.moviesapp.ui.popular_movie

import android.media.MediaDataSource
import androidx.lifecycle.LiveData
import androidx.lifecycle.switchMap
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.moviesapp.data.api.POST_PER_PAGE
import com.example.moviesapp.data.api.TheMovieDBInterface
import com.example.moviesapp.data.repositoty.MovieDataSourceFactory
import com.example.moviesapp.data.repositoty.NetworkState
import com.example.moviesapp.data.vo.popular.Movie
import io.reactivex.disposables.CompositeDisposable




class MoviePageListRepository(val apiService: TheMovieDBInterface) {
    lateinit var moviePagedList: LiveData<PagedList<Movie>>
    lateinit var moviesDataSourceFactory: MovieDataSourceFactory

    fun fetchLiveMoviePagedList(compositeDisposable: CompositeDisposable): LiveData<PagedList<Movie>>{
        moviesDataSourceFactory = MovieDataSourceFactory(apiService,compositeDisposable)

        val config: PagedList.Config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        moviePagedList = LivePagedListBuilder(moviesDataSourceFactory,config).build()

        return moviePagedList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return moviesDataSourceFactory.moviesLiveDataSource.switchMap { dataSource ->
            dataSource.networkState
        }
    }
}