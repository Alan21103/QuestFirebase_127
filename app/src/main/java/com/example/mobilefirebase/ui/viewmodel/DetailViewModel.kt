package com.example.mobilefirebase.ui.viewmodel

import com.example.mobilefirebase.model.Mahasiswa

package com.example.praktikum12.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.praktikum12.model.Mahasiswa
import com.example.praktikum12.repository.MahasiswaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class DetailUiState {
    object Loading : DetailUiState()
    data class Success(val mahasiswa: Mahasiswa) : DetailUiState()
    data class Error(val message: String) : DetailUiState()
}

class DetailViewModel(private val mhs: ) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    fun getMahasiswaById(nim: String) {
        viewModelScope.launch {
            try {
                val mahasiswa = mhs.getMahasiswaById(nim)
                _uiState.value = DetailUiState.Success(mahasiswa)
            } catch (e: Exception) {
                _uiState.value = DetailUiState.Error(e.localizedMessage ?: "Terjadi kesalahan.")
            }
        }
    }

    fun deleteMhs(nim: String) {
        viewModelScope.launch {
            try {
                mhs.deleteMahasiswa(nim)
                _uiState.value = DetailUiState.Error("Data Mahasiswa telah dihapus.")
            } catch (e: Exception) {
                _uiState.value = DetailUiState.Error(e.localizedMessage ?: "Gagal menghapus data.")
            }
        }
    }
}