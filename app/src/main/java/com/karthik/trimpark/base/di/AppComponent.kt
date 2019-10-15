package com.karthik.trimpark.base.di

import com.karthik.trimpark.base.TrimParkApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import javax.inject.Singleton


/**
 *
 * Root component of the dagger dependency graph
 *
 * created by Karthik A on 2019-07-08
 */
@Singleton
@Component(modules = [ActivityBuilder::class, AndroidInjectionModule::class,
    AppModule::class])
interface AppComponent: AndroidInjector<DaggerApplication>
{
    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(app: TrimParkApplication): Builder

        fun build(): AppComponent
    }


    fun inject(app: TrimParkApplication)

    override fun inject(instance: DaggerApplication?)
}