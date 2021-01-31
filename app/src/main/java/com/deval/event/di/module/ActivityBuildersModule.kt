package com.deval.event.di.module


import com.deval.event.activity.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Taruh disini semua activity yang mau pakai inject, untuk fragment dia bisa tidak ada atau dibuatkan modul fragment lain*/
@Module
abstract class ActivityBuildersModule {

    //contoh tanpa fragment
    @ContributesAndroidInjector()
    abstract fun bindMain2(): MainActivity

}
