package com.selayar.history.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Ini injector untuk Fragment, usahakan pakai single activity saja setelah halaman login
 * Untuk penamaan bind terserah, yang jelas ndak conflict*/
@Module
abstract class FragmentBuildersModule {

    //tambahkan fragment lainnya disini
//    @ContributesAndroidInjector
//    abstract fun bindFragOrder(): FragOrder

}
