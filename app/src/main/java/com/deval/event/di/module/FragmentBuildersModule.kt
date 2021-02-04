package com.deval.event.di.module

import com.deval.event.fragment.detail.more.MoreFragment
import com.deval.event.fragment.home.HomeFragment
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

    @ContributesAndroidInjector
    abstract fun bindFragHome() : HomeFragment

    @ContributesAndroidInjector
    abstract fun bindFragMore() : MoreFragment

}
