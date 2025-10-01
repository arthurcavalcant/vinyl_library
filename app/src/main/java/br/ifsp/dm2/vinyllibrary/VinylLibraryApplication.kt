package br.ifsp.dm2.vinyllibrary

import android.app.Application
import br.ifsp.dm2.vinyllibrary.data.AppDatabase
import br.ifsp.dm2.vinyllibrary.data.repository.VinylRepository

class VinylLibraryApplication : Application() {
    // Usar lazy para que o banco de dados e o repositório sejam criados apenas quando necessários
    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { VinylRepository(database.vinylDao()) }
}

