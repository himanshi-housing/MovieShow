package com.example.movieshow.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.movieshow.models.Movie
import com.example.movieshow.models.Response
import com.example.movieshow.network.MovieApi

class MoviePagingSource(private val movieApi: MovieApi, private val type : String) : PagingSource<Int, Movie>(){
    override fun getRefreshKey(state: PagingState<Int, Movie>) : Int?{
        var page : Int? = null
        if(state.anchorPosition!=null){
            val anchorPage = state.closestPageToPosition(state.anchorPosition!!)
            if(anchorPage?.prevKey!=null){
                page =  anchorPage!!.prevKey?.plus(1)
            }
            else if (anchorPage?.nextKey!=null){
                page =  anchorPage!!.nextKey?.minus(1)
            }
        }
        return page
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val position = params.key ?: 1
            lateinit var response : Response
            when(type){
                "popular" -> response = movieApi.getPopularMovies(page = position)
                "upcoming" -> response = movieApi.getUpcomingMovies(page = position)
                "trending" -> response = movieApi.getTrendingMovies(page = position)
                "toprated" -> response = movieApi.getTopRatedMovies(page = position)
            }
            LoadResult.Page(
                data = response.results ,
                prevKey = if(position==1) null else position-1,
                nextKey = if(position==response.totalPages) null else position+1
            )
        } catch (ex : Exception){
            LoadResult.Error(ex)
        }
    }

}