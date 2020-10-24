package com.selayar.history.di.module

import androidx.lifecycle.ViewModelProvider
import com.selayar.history.di.ViewModelKey
import com.selayar.history.di.ViewModelFactory
import com.selayar.history.viewModel.ExampleViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Taruh disini semua view model yang mau dipakai biar langsung bisa diinject */
@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ExampleViewModel::class)
    abstract fun bindExampleVM(exampleViewModel: ExampleViewModel): ExampleViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
