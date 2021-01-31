package com.deval.event.Model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlin.collections.ArrayList

@Keep
open class ModelListWrapper<T>{
    @field:SerializedName("data")
    open var data: MutableList<T>? = ArrayList()
}
