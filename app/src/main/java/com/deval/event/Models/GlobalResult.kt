package com.deval.event.Model

import androidx.annotation.Keep
import com.deval.event.Models.Peserta
import com.google.gson.annotations.SerializedName
import retrofit2.http.Field

@Keep
open class GlobalResult (
    @field:SerializedName("data")
    open var data : Peserta = Peserta(),

    @field:SerializedName("msg")
    open var msg : String? = ""
)