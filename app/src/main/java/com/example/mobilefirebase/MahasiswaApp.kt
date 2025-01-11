package com.example.mobilefirebase

import android.app.Application
import com.example.mobilefirebase.di.MahasiswaContainer

class MahasiswaApp : Application(){
    // Fungsinya untuk menyimpan instance Container
    lateinit var containerApp: MahasiswaContainer

    override fun onCreate() {
        super.onCreate()
        // Membuat instance ContainerApp
        containerApp = MahasiswaContainer(this)
        //Instance adalah object yang dibuat dari kelas
    }
}