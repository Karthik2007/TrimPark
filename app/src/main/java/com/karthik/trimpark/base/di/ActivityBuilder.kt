package com.karthik.trimpark.base.di

import com.karthik.trimpark.parkingentry.di.ParkingModule
import com.karthik.trimpark.parkingentry.view.ParkingEntryActivity
import com.karthik.trimpark.parkingexit.view.ParkingExitActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 *
 * ActivityBuilder tells Dagger the list of activities which needs to be injected with dependencies
 *
 * created by Karthik A on 2019-07-08
 */
@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = [ParkingModule::class])
    abstract fun bindParkingEntryActivity(): ParkingEntryActivity

    @ContributesAndroidInjector(modules = [ParkingModule::class])
    abstract fun bindParkingExitActivity(): ParkingExitActivity

}