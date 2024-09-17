package com.example.moviesapp.data.api

import com.example.moviesapp.data.vo.detail.MovieDetail
import com.example.moviesapp.data.vo.popular.MovieResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TheMovieDBInterface {

    //https://api.themoviedb.org/3/movie/popular?language=en-US&page=1
    //https://api.themoviedb.org/3/movie/533535
    //https://api.themoviedb.org/3

    //"https://api.themoviedb.org/3/movie/popular?api_key=YOUR_API_KEY&language=en-US&page=1"

    @GET("movie/popular")
    fun getMoviePopular(@Query("page") page: Int): Single<MovieResponse>

    @GET("movie/{movie_id}")
    fun getMovieDetail(@Path("movie_id") id: Int): Single<MovieDetail>
}