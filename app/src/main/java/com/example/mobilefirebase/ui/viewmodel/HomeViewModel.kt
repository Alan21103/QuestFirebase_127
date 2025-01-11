package com.example.mobilefirebase.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilefirebase.model.Mahasiswa
import com.example.mobilefirebase.repository.RepositoryMhs
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class HomeViewModel (
    private val repoMhs: RepositoryMhs
) : ViewModel(){

    var mhsUIState: HomeUiState by mutableStateOf(HomeUiState.Loading)

    init {
        getMhs()
    }

    fun getMhs(){
        viewModelScope.launch {
            repoMhs.getAllMhs()
                .onStart {
                mhsUIState = HomeUiState.Loading
            }
                .catch {
                    mhsUIState = HomeUiState.Error(e = it)
                }
                .collect{
                    mhsUIState = if (it.isEmpty()){
                        HomeUiState.Error(Exception("Belum ada data mahasiswa"))
                    }else{
                        HomeUiState.Success(it)
                    }
                }
        }
    }
}
sealed class HomeUiState{
    //Loading
    object Loading : HomeUiState()
    //SUKSESS
    data class Success(val data: List<Mahasiswa>) : HomeUiState()
    //ERROR
    data class Error(val e: Throwable) : HomeUiState()
}