package com.emmm.mobv.util

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.emmm.mobv.data.DataRepository
import com.emmm.mobv.data.api.WebApi
import com.emmm.mobv.data.db.AppRoomDatabase
import com.emmm.mobv.data.db.LocalCache

/**
 * Class that handles object creation.
 * Like this, objects can be passed as parameters in the constructors and then replaced for
 * testing, where needed.
 */
object Injection {

    private fun provideCache(context: Context): LocalCache {
        val database = AppRoomDatabase.getInstance(context)
        return LocalCache(database.appDao())
    }

    private fun provideWebApi(context: Context): WebApi {
        return WebApi.create(context)
    }

    private fun provideDataRepository(context: Context): DataRepository {
        return DataRepository.getInstance(provideWebApi(context), provideCache(context))
    }

    fun provideViewModelFactory(context: Context): ViewModelProvider.Factory {
        return ViewModelFactory(provideDataRepository(context))
    }
}