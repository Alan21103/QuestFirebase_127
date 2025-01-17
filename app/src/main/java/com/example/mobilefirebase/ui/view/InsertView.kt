package com.example.mobilefirebase.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobilefirebase.navigation.DestinasiHome
import com.example.mobilefirebase.navigation.DestinasiInsert
import com.example.mobilefirebase.ui.customwidget.CostumeTopAppBar
import com.example.mobilefirebase.ui.viewmodel.FormErrorState
import com.example.mobilefirebase.ui.viewmodel.FormState
import com.example.mobilefirebase.ui.viewmodel.InsertUiState
import com.example.mobilefirebase.ui.viewmodel.InsertViewModel
import com.example.mobilefirebase.ui.viewmodel.MahasiswaEvent
import com.example.mobilefirebase.ui.viewmodel.PenyediaViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsertMhsView(
    navigateBack: () -> Unit,
    onNavigate: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InsertViewModel = viewModel(factory = PenyediaViewModel.Factory)
){
    val uiState = viewModel.uiState // State utama untuk loading, success, error
    val uiEvent = viewModel.uiEvent
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

// Observe changes in state for snackbar and navigation
    LaunchedEffect(uiState) {
        when (uiState) {
            is FormState.Success -> {
                println("InsertMhsView: uiState is FormState.Success, navigate to home " + uiState.message)
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(uiState.message) // Show snackbar
                    delay(700) // Delay before navigating
                    onNavigate() // Navigate to home
                    viewModel.resetSnackBarMessage() // Reset snackbar state
                }
            }
            is FormState.Error -> {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(uiState.message) // Show error snackbar
                }
            }
            else -> Unit // No action for other states
        }
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            CostumeTopAppBar(
                title = DestinasiInsert.titleRes,
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                navigateUp = navigateBack
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(10.dp)
        ) {
            InsertBodyMhs(
                uiState = uiEvent,
                homeUiState = uiState,
                onValueChange = { updatedEvent -> viewModel.updateState(updatedEvent) },
                onClick = {
                    if (viewModel.validateFields()) {
                        viewModel.insertMhs() // Insert student
                        // onNavigate() // Uncomment if you want to navigate immediately after insert
                    }
                }
            )
        }
    }
}


@Composable
fun InsertBodyMhs(
    modifier: Modifier = Modifier,
    onValueChange: (MahasiswaEvent) -> Unit,
    uiState: InsertUiState,
    onClick: () -> Unit,
    homeUiState: FormState
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Form input untuk Mahasiswa
        FormMahasiswa(
            mahasiswaEvent = uiState.insertUiEvent,
            onValueChange = onValueChange,
            errorState = uiState.isEntryValid,
            modifier = Modifier.fillMaxWidth()
        )

        // Tombol untuk menambah data
        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth(),
            enabled = homeUiState !is FormState.Loading,
        ) {
            if (homeUiState is FormState.Loading) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier
                            .size(20.dp)
                            .padding(end = 2.dp)
                    )
                    Text("Loading...")
                }
            } else {
                Text("Add")
            }
        }
    }
}

@Composable
fun FormMahasiswa(
    mahasiswaEvent: MahasiswaEvent = MahasiswaEvent(),
    onValueChange: (MahasiswaEvent) -> Unit,
    errorState: FormErrorState = FormErrorState(),
    modifier: Modifier = Modifier
){
    val gender = listOf("Laki-laki", "Perempuan")
    val kelas = listOf("A", "B", "C", "D", "E")

    Column(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = mahasiswaEvent.nama,
            onValueChange = { onValueChange(mahasiswaEvent.copy(nama = it)) },
            label = { Text("Nama") },
            isError = errorState.nama != null,
            placeholder = { Text("Masukkan nama") }
        )
        Text(text = errorState.nama ?: "", color = Color.Red)

        // NIM Field
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = mahasiswaEvent.nim,
            onValueChange = { onValueChange(mahasiswaEvent.copy(nim = it)) },
            label = { Text("NIM") },
            isError = errorState.nim != null,
            placeholder = { Text("Masukkan NIM") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Text(text = errorState.nim ?: "", color = Color.Red)

        // Jenis Kelamin
        Text(text = "Jenis Kelamin")
        Row(modifier = Modifier.fillMaxWidth()) {
            gender.forEach { gd ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = mahasiswaEvent.gender == gd,
                        onClick = { onValueChange(mahasiswaEvent.copy(gender = gd)) }
                    )
                    Text(text = gd)
                }
            }
        }
        Text(text = errorState.gender ?: "", color = Color.Red)
        // Alamat Field
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = mahasiswaEvent.alamat,
            onValueChange = { onValueChange(mahasiswaEvent.copy(alamat = it)) },
            label = { Text("Alamat") },
            isError = errorState.alamat != null,
            placeholder = { Text("Masukkan alamat") }
        )
        Text(text = errorState.alamat ?: "", color = Color.Red)

        // Kelas
        Text(text = "Kelas")
        Row {
            kelas.forEach { kls ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = mahasiswaEvent.kelas == kls,
                        onClick = { onValueChange(mahasiswaEvent.copy(kelas = kls)) }
                    )
                    Text(text = kls)
                }
            }
        }
        Text(text = errorState.kelas ?: "", color = Color.Red)

        // Angkatan Field
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = mahasiswaEvent.angkatan,
            onValueChange = { onValueChange(mahasiswaEvent.copy(angkatan = it)) },
            label = { Text("Angkatan") },
            isError = errorState.angkatan != null,
            placeholder = { Text("Masukkan angkatan") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Text(text = errorState.angkatan ?: "", color = Color.Red)

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = mahasiswaEvent.judulSkripsi,
            onValueChange = { onValueChange(mahasiswaEvent.copy(judulSkripsi = it)) },
            label = { Text("Judul Skripsi") },
            isError = errorState.judulSkripsi != null,
            placeholder = { Text("Masukkan Judul Skripsi") }
        )
        Text(text = errorState.judulSkripsi ?: "", color = Color.Red)

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = mahasiswaEvent.dosBim1,
            onValueChange = { onValueChange(mahasiswaEvent.copy(dosBim1 = it)) },
            label = { Text("Dosen Pembimbing 1") },
            isError = errorState.dosBim1 != null,
            placeholder = { Text("Masukkan Dosen Pembimbing 1") }
        )
        Text(text = errorState.dosBim1 ?: "", color = Color.Red)

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = mahasiswaEvent.dosBim2,
            onValueChange = { onValueChange(mahasiswaEvent.copy(dosBim2 = it)) },
            label = { Text("Dosen Pembimbing 2") },
            isError = errorState.dosBim2 != null,
            placeholder = { Text("Masukkan Dosen Pembimbing 2") }
        )
        Text(text = errorState.dosBim2 ?: "", color = Color.Red)

    }
}


