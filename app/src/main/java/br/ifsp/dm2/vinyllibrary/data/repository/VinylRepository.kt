package br.ifsp.dm2.vinyllibrary.data.repository

import br.ifsp.dm2.vinyllibrary.data.dao.VinylDao
import br.ifsp.dm2.vinyllibrary.data.model.Vinyl
import kotlinx.coroutines.flow.Flow

class VinylRepository(private val vinylDao: VinylDao) {

    fun getAllVinyls(): Flow<List<Vinyl>> = vinylDao.getAllVinyls()

    suspend fun getVinylById(vinylId: Long): Vinyl? = vinylDao.getVinylById(vinylId)

    fun searchVinyls(searchQuery: String): Flow<List<Vinyl>> = vinylDao.searchVinyls(searchQuery)

    suspend fun insertVinyl(vinyl: Vinyl): Long = vinylDao.insertVinyl(vinyl)

    suspend fun updateVinyl(vinyl: Vinyl) = vinylDao.updateVinyl(vinyl)

    suspend fun deleteVinyl(vinyl: Vinyl) = vinylDao.deleteVinyl(vinyl)

    suspend fun deleteVinylById(vinylId: Long) = vinylDao.deleteVinylById(vinylId)
}

