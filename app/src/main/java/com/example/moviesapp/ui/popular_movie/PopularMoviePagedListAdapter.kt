package com.example.moviesapp.ui.popular_movie

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviesapp.R
import com.example.moviesapp.data.api.POSTER_BASE_URL
import com.example.moviesapp.data.repositoty.NetworkState
import com.example.moviesapp.data.vo.popular.Movie
import com.example.moviesapp.ui.single_movie_details.SingleMovie

class PopularMoviePagedListAdapter(private val context: Context): PagedListAdapter<Movie,RecyclerView.ViewHolder>(MovieDiffCallBack()) {

    val MOVIE_VIEW_TYPE = 1
    val NETWORK_VIEW_TYPE = 2

    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflate = LayoutInflater.from(parent.context)
        val view: View

        if (viewType == MOVIE_VIEW_TYPE){
            view = layoutInflate.inflate(R.layout.movie_list_item,parent,false)
            return MovieItemViewHolder(view)
        }
        else{
            view = layoutInflate.inflate(R.layout.network_state_item,parent,false)
            return NetworkStateItemViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == MOVIE_VIEW_TYPE){
            (holder as MovieItemViewHolder).bind(getItem(position),context)
        }
        else{
            (holder as NetworkStateItemViewHolder).bind(networkState)
        }
    }

    private fun hasExtraRow(): Boolean{
        return networkState != null && networkState != NetworkState.LOADED
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1){
            NETWORK_VIEW_TYPE
        }
        else{
            MOVIE_VIEW_TYPE
        }
    }

    class MovieDiffCallBack: DiffUtil.ItemCallback<Movie>(){
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }

    }

    class MovieItemViewHolder(view: View) : RecyclerView.ViewHolder(view){

        fun bind(movie: Movie?, context: Context){
           val cv_movie_title =  itemView.findViewById<TextView>(R.id.cv_movie_title)
           val cv_movie_release_date =  itemView.findViewById<TextView>(R.id.cv_movie_release_date)
           val cv_movie_poster =  itemView.findViewById<ImageView>(R.id.cv_iv_movie_poster)

            cv_movie_title.text = movie?.title
            cv_movie_release_date.text = movie?.releaseDate

            val moviePosterUrl = POSTER_BASE_URL + movie?.posterPath
            Glide.with(itemView.context)
                .load(moviePosterUrl)
                .into(cv_movie_poster)

            itemView.setOnClickListener {
                val intent = Intent(context, SingleMovie::class.java)
                intent.putExtra("id", movie?.id)
                context.startActivity(intent)
            }
        }
    }

    class NetworkStateItemViewHolder(view: View): RecyclerView.ViewHolder(view){

        val progress_bar_item =  itemView.findViewById<ProgressBar>(R.id.progress_bar_item)
        val error_msg_item =  itemView.findViewById<TextView>(R.id.error_msg)

        fun bind(networkState: NetworkState?){
            if (networkState != null && networkState == NetworkState.LOADING){
                progress_bar_item.visibility = View.VISIBLE
            }
            else{
                progress_bar_item.visibility = View.GONE
            }

            if (networkState != null && networkState == NetworkState.ERROR){
                error_msg_item.visibility = View.VISIBLE
                error_msg_item.text = networkState.msg
            }
            else if (networkState != null && networkState == NetworkState.ENDOFLIST){
                error_msg_item.visibility = View.VISIBLE
                error_msg_item.text = networkState.msg
            }
            else{
                error_msg_item.visibility = View.GONE
            }
        }
    }

    fun setNetworkState(newNetworkState: NetworkState){
        val previousState: NetworkState? = this.networkState
        val hadExtraRow:Boolean = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow: Boolean = hasExtraRow()

        if (hadExtraRow != hasExtraRow){
            if (hadExtraRow){
                notifyItemRemoved(super.getItemCount())
            }
            else{
                notifyItemRemoved(super.getItemCount())
            }
        }
        else if (hasExtraRow && previousState != networkState){
            notifyItemChanged(itemCount -1)
        }
    }
}