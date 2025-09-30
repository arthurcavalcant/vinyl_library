package br.ifsp.dm2.vinyllibrary.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import br.ifsp.dm2.vinyllibrary.data.model.Vinyl
import kotlinx.coroutines.flow.Flow

@Dao
interface VinylDao {

    @Query("SELECT * FROM vinyls ORDER BY albumName ASC")
    fun getAllVinyls(): Flow<List<Vinyl>>

    @Query("SELECT * FROM vinyls WHERE id = :id")
    suspend fun getVinylById(id: Long): Vinyl?

    @Query("SELECT * FROM vinyls WHERE albumName LIKE '%' || :searchQuery || '%' OR artistName LIKE '%' || :searchQuery || '%'")
    fun searchVinyls(searchQuery: String): Flow<List<Vinyl>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVinyl(vinyl: Vinyl): Long

    @Update
    suspend fun updateVinyl(vinyl: Vinyl)

    @Delete
    suspend fun deleteVinyl(vinyl: Vinyl)

    @Query("DELETE FROM vinyls WHERE id = :id")
    suspend fun deleteVinylById(id: Long)
}

