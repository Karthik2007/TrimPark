package com.karthik.trimpark.base.di

import android.content.Context
import com.karthik.trimpark.base.TrimParkApplication
import com.karthik.trimpark.base.db.TrimParkDatabase
import com.karthik.trimpark.base.navigation.AppNavigator
import com.karthik.trimpark.base.util.CoroutineDispatcherProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


/**
 *
 * App level module which supplies root level dependencies
 *
 * created by Karthik A on 2019-07-08
 */
@Module
class AppModule {

    @Provides
    @Singleton
    fun provideContext(application: TrimParkApplication): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun providesDispatcherProvider() = CoroutineDispatcherProvider()

    @Singleton
    @Provides
    fun providesAppNavigator(): AppNavigator = AppNavigator()

    @Singleton
    @Provides
    fun providesDatabase(baseApplication: TrimParkApplication): TrimParkDatabase = TrimParkDatabase.getDatabase(baseApplication)



}