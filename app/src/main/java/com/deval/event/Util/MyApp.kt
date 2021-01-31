package com.deval.event.Util

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.deval.event.di.AppComponent
import com.deval.event.di.AppInjector
import com.deval.event.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import io.realm.Realm
import io.realm.RealmConfiguration
import javax.inject.Inject

class MyApp : Application(),HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        instance = this

        Realm.init(this)

        /*val config = RealmConfiguration.Builder()
                .name("realmprefix.realm").build()*/

        val config = RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
//            .encryptionKey(getKey())
//            .modules(MyModule())
//            .migration(MyMigration())
                .build()

        Realm.setDefaultConfiguration(config)

        appComponent = DaggerAppComponent.builder()
                .application(this)
                .build()
        appComponent.inject(this)
        AppInjector.init(this)

    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    companion object {
        @get:Synchronized
        var instance: MyApp? = null
            private set
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return dispatchingAndroidInjector
    }
}