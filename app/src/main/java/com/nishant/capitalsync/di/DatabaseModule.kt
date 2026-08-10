package com.nishant.capitalsync.di

import android.app.Application
import androidx.room.Room
import com.nishant.capitalsync.data.local.CapitalSyncDatabase
import com.nishant.capitalsync.data.local.dao.CapitalSyncDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.zetetic.database.sqlcipher.SupportOpenHelperFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideCapitalSyncDatabase(app: Application): CapitalSyncDatabase {
        // In production, securely retrieve/generate this key from the Android Keystore.
        val passphrase = "CapitalSyncSecureKey".toByteArray()
        val factory = SupportOpenHelperFactory(passphrase)

        return Room.databaseBuilder(
            app,
            CapitalSyncDatabase::class.java,
            CapitalSyncDatabase.DATABASE_NAME
        )
            .openHelperFactory(factory)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideCapitalSyncDao(db: CapitalSyncDatabase): CapitalSyncDao = db.dao
}