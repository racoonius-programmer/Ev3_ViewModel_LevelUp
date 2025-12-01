package com.example.viewmodela

import android.app.Application
import com.example.viewmodela.db.AppDatabase

class MyApplication : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
}