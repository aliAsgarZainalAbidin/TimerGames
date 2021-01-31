package com.deval.event.di

import android.app.Application
import com.deval.event.Util.Session
import com.deval.event.Util.MyApp
import com.deval.event.di.module.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import retrofit2.Retrofit
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidSupportInjectionModule::class, AppModule::class, ActivityBuildersModule::class, NetworkModule::class, FragmentBuildersModule::class])
interface AppComponent {
    fun inject(myApp: MyApp)

    //provide untuk dipakai di modul lain
    val sessionCore : Session
    val retrofit : Retrofit

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }
}
