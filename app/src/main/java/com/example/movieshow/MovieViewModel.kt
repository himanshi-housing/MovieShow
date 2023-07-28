package com.example.movieshow

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlin.Exception

class MovieViewModel : ViewModel() {
    var pageNo by mutableStateOf(0)
    var popular  : List<Movie> by mutableStateOf(listOf())
    var upcoming  : List<Movie> by mutableStateOf(listOf())
    var trending  : List<Movie> by mutableStateOf(listOf())
    var topRated  : List<Movie> by mutableStateOf(listOf())

    fun getPopular(){
        viewModelScope.launch {
            val apiService = MovieApi.getInstance()
            try {
                popular = apiService.getPopularMovies().results
            }
            catch (ex: Exception){
                throw Exception(ex.message)
            }
        }
    }
    fun getUpcoming(){
        viewModelScope.launch {
            val apiService = MovieApi.getInstance()
            try {
                upcoming = apiService.getUpcomingMovies().results
            }
            catch (ex: Exception){
                throw Exception(ex.message)
            }
        }
    }
    fun getTrending(){
        viewModelScope.launch {
            val apiService = MovieApi.getInstance()
            try {
                trending = apiService.getTrendingMovies().results
            }
            catch (ex: Exception){
                throw Exception(ex.message)
            }
        }
    }
    fun getTopRated(){
        viewModelScope.launch {
            val apiService = MovieApi.getInstance()
            try {
                topRated = apiService.getTopRatedMovies().results
            }
            catch (ex: Exception){
                throw Exception(ex.message)
            }
        }
    }
}