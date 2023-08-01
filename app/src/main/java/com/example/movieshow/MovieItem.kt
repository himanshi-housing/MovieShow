package com.example.movieshow

import androidx.compose.ui.text.resolveDefaults
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp

@Entity(tableName = "Watchlist")
data class MovieItem(
    @PrimaryKey(autoGenerate = false)
    var id : Int,

    @ColumnInfo(name = "Time Stamp")
    var time : Long = System.currentTimeMillis(),

    @ColumnInfo(name  = "Long Poster")
    var longPoster : String,

    @ColumnInfo(name = "Short Poster")
    var shortPoster : String,

    @ColumnInfo(name = "Title")
    var title : String,

    @ColumnInfo(name = "Description")
    var descrptn : String
)
