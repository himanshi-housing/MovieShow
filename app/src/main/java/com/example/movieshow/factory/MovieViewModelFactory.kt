package com.example.movieshow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.movieshow.database.MovieDao
import com.example.movieshow.network.MovieApi
import com.example.movieshow.viewModels.MovieViewModel

@Suppress("UNCHECKED_CAST")
class MovieViewModelFactory(private val dbname: MovieDao, private val movieApi: MovieApi) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MovieViewModel::class.java)){
            return MovieViewModel(dbname, movieApi) as T
        }
        throw IllegalArgumentException("ViewModel not created")
    }
}
