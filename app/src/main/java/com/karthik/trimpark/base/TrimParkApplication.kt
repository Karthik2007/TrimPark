package com.karthik.trimpark.base

import com.karthik.trimpark.base.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication


/**
 * created by Karthik A on 2019-08-01
 */
class TrimParkApplication: DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        val appComponent = DaggerAppComponent.builder().application(this).build()
        appComponent.inject(this)
        return appComponent
    }
}