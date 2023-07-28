package com.example.movieshow
import androidx.room.*

@Dao
interface MovieDao {
    @Query("SELECT * from Watchlist ORDER BY time DESC")
    fun getAll() : List<MovieItem>

    @Insert
    suspend fun insert(item:MovieItem)

    @Query("DELETE FROM Watchlist where time = :ts")
    suspend fun deleteId(ts: Long)
}