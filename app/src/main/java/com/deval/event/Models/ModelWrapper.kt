package com.deval.event.Models

import com.google.gson.annotations.SerializedName
import io.realm.internal.Keep

@Keep
open class ModelWrapper<T>{
    @field:SerializedName("data")
    open var data: T? = null
}