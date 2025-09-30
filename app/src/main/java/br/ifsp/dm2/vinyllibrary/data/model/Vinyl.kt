package br.ifsp.dm2.vinyllibrary.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vinyls")
data class Vinyl(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val albumName: String,
    val artistName: String,
    val releaseYear: Int,
    val recordLabel: String
)

