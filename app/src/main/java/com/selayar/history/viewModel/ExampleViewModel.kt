package com.selayar.history.viewModel

//import Penjualan2

import android.content.Context
import androidx.lifecycle.ViewModel
import com.selayar.history.Util.Session
import com.selayar.history.Retrofit.ApiInterface
import io.realm.Realm
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExampleViewModel @Inject constructor(private val context: Context,
                                           private val session: Session,
                                           private val api : ApiInterface,
                                           private val realm : Realm
): ViewModel(){


}
