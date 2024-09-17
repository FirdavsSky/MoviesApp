package com.example.moviesapp.ui.single_movie_details

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.moviesapp.R
import com.example.moviesapp.data.api.POSTER_BASE_URL
import com.example.moviesapp.data.api.TheMovieDBClient
import com.example.moviesapp.data.api.TheMovieDBInterface
import com.example.moviesapp.data.repositoty.NetworkState
import com.example.moviesapp.data.vo.detail.MovieDetail
import com.example.moviesapp.databinding.ActivitySingleMovieBinding
import java.text.NumberFormat
import java.util.Locale

class SingleMovie : AppCompatActivity() {

    lateinit var binding: ActivitySingleMovieBinding
    private lateinit var viewModel: SingleMovieViewModel
    private lateinit var movieDetailsRepository: MovieDetailsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingleMovieBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val movieId: Int = intent.getIntExtra("id",1)

        val apiService: TheMovieDBInterface = TheMovieDBClient.getClient()
        movieDetailsRepository = MovieDetailsRepository(apiService)

        viewModel = getViewModel(movieId)

        viewModel.movieDetails.observe(this) {
            bindUi(it)
        }

        viewModel.networkState.observe(this){
            binding.progressBar.visibility = if (it == NetworkState.LOADING) View.VISIBLE else View.GONE
            binding.txtError.visibility = if (it == NetworkState.ERROR) View.VISIBLE else View.GONE
        }
    }

    fun bindUi(it: MovieDetail) {
       binding.movieTitle.text = it.title
       binding.movieTitle.text = it.tagline
       binding.movieReleaseDate.text = it.releaseDate
       binding.movieRating.text = it.voteAverage.toString()
       binding.movieRuntime.text = it.runtime.toString() + " minutes"
       binding.movieOverview.text = it.overview

        val formatCurrency = NumberFormat.getCurrencyInstance(Locale.US)
        binding.movieBudget.text = formatCurrency.format(it.budget)
        binding.movieRevenue.text = formatCurrency.format(it.revenue)

        val moviePostUrl = POSTER_BASE_URL + it.posterPath
        Glide.with(this)
            .load(moviePostUrl)
            .into(binding.ivMoviePoster)
    }

    private fun getViewModel(movieId: Int): SingleMovieViewModel {
        return ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return SingleMovieViewModel(movieDetailsRepository, movieId) as T
            }
        })[SingleMovieViewModel::class.java]
    }

}
