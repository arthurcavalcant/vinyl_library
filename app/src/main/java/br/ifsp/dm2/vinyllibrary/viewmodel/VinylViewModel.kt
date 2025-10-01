package br.ifsp.dm2.vinyllibrary.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import br.ifsp.dm2.vinyllibrary.data.model.Vinyl
import br.ifsp.dm2.vinyllibrary.data.repository.VinylRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class VinylViewModel(private val repository: VinylRepository) : ViewModel() {

    val allVinyls: Flow<List<Vinyl>> = repository.getAllVinyls()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    suspend fun getVinylById(vinylId: Long): Vinyl? {
        return try {
            _isLoading.value = true
            repository.getVinylById(vinylId)
        } catch (e: Exception) {
            _errorMessage.value = "Erro ao buscar vinil: ${e.message}"
            null
        } finally {
            _isLoading.value = false
        }
    }

    fun searchVinyls(query: String): Flow<List<Vinyl>> {
        _searchQuery.value = query
        return repository.searchVinyls(query)
    }

    fun insertVinyl(vinyl: Vinyl) = viewModelScope.launch {
        try {
            _isLoading.value = true
            repository.insertVinyl(vinyl)
            _errorMessage.value = null
        } catch (e: Exception) {
            _errorMessage.value = "Erro ao inserir vinil: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }

    fun updateVinyl(vinyl: Vinyl) = viewModelScope.launch {
        try {
            _isLoading.value = true
            repository.updateVinyl(vinyl)
            _errorMessage.value = null
        } catch (e: Exception) {
            _errorMessage.value = "Erro ao atualizar vinil: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }

    fun deleteVinyl(vinyl: Vinyl) = viewModelScope.launch {
        try {
            _isLoading.value = true
            repository.deleteVinyl(vinyl)
            _errorMessage.value = null
        } catch (e: Exception) {
            _errorMessage.value = "Erro ao deletar vinil: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }

    fun deleteVinylById(vinylId: Long) = viewModelScope.launch {
        try {
            _isLoading.value = true
            repository.deleteVinylById(vinylId)
            _errorMessage.value = null
        } catch (e: Exception) {
            _errorMessage.value = "Erro ao deletar vinil: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}

class VinylViewModelFactory(private val repository: VinylRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VinylViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return VinylViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
