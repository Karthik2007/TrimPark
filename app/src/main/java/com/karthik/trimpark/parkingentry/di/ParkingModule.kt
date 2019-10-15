package com.karthik.trimpark.parkingentry.di

import com.karthik.trimpark.base.db.TrimParkDatabase
import com.karthik.trimpark.base.util.CoroutineDispatcherProvider
import com.karthik.trimpark.parkingentry.repository.ParkingRepository
import dagger.Module
import dagger.Provides


/**
 * created by Karthik A on 2019-08-01
 */
@Module
class ParkingModule {

    @Provides
    @ParkingScope
    fun providesParkingRepo(
        trimParkDatabase: TrimParkDatabase,
        coroutineDispatcherProvider: CoroutineDispatcherProvider
    ): ParkingRepository {
        return ParkingRepository(trimParkDatabase, coroutineDispatcherProvider)
    }
}