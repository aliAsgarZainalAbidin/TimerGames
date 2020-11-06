package com.selayar.history.Model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlin.collections.ArrayList

@Keep
open class ModelListWrapper<T>{
    //data menu
    @field:SerializedName("status")
    open var status: String? = ""
    @field:SerializedName("data")
    open var data: MutableList<T>? = ArrayList()
}
