package br.ifsp.dm2.vinyllibrary.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.ifsp.dm2.vinyllibrary.data.dao.VinylDao
import br.ifsp.dm2.vinyllibrary.data.model.Vinyl

@Database(entities = [Vinyl::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun vinylDao(): VinylDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "vinyl_library_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

