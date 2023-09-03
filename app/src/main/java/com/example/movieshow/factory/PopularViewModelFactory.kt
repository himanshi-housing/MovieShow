package com.example.movieshow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.movieshow.repository.MovieRepo
import com.example.movieshow.viewModels.PopularViewModel

@Suppress("UNCHECKED_CAST")
class PopularViewModelFactory(private val repo: MovieRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(PopularViewModel::class.java)){
            return PopularViewModel(repo) as T
        }
        throw IllegalArgumentException("ViewModel not created")
    }
}
