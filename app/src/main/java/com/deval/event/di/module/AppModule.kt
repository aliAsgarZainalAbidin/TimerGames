package com.deval.event.di.module

import android.app.Application
import android.content.Context
import com.deval.event.Util.Session
import dagger.Module
import dagger.Provides
import io.realm.Realm
import io.realm.RealmConfiguration
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {
    @Singleton
    @Provides
    fun provideContext(application: Application): Context {
        return application
    }

    @Singleton
    @Provides
    fun provideSessionManager(context: Context): Session {
        return Session(context)
    }

    @Singleton
    @Provides
    fun provideDatabase(context: Context): Realm {
        Realm.init(context)

        val config = RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build()

        Realm.setDefaultConfiguration(config)
        return Realm.getDefaultInstance()
    }
}