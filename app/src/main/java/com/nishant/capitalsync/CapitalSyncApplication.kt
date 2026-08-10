package com.nishant.capitalsync

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import com.nishant.capitalsync.utils.CategoryUtils

@HiltAndroidApp
class CapitalSyncApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        System.loadLibrary("sqlcipher")
        CategoryUtils.loadCategories(this)
    }
}