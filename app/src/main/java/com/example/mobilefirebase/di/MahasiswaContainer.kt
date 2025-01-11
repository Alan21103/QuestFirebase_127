package com.example.mobilefirebase.di

import android.content.Context
import com.example.mobilefirebase.repository.NetworkRepositoryMhs
import com.example.mobilefirebase.repository.RepositoryMhs
import com.google.firebase.firestore.FirebaseFirestore

interface InterfaceContainerApp{
    val repositoryMhs: RepositoryMhs
}

class MahasiswaContainer(private val context: Context) : InterfaceContainerApp{
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    override val repositoryMhs: RepositoryMhs by lazy {
        NetworkRepositoryMhs(firestore)
    }
}