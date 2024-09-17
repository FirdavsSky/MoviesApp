package com.example.moviesapp.data.repositoty

enum class Status{
    RUNNING,
    SUCCESS,
    FAILED
}

class NetworkState(val status: Status, val msg: String) {


    companion object{
        val LOADED:NetworkState
        val LOADING:NetworkState
        val ERROR:NetworkState
        val ENDOFLIST:NetworkState

        init {
            LOADED = NetworkState(Status.SUCCESS,"Success")
            LOADING = NetworkState(Status.RUNNING,"Running")
            ERROR = NetworkState(Status.FAILED,"Failed")
            ENDOFLIST = NetworkState(Status.FAILED,"You have reached the end")
        }
    }
}