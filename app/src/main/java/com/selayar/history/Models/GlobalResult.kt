package com.selayar.history.Model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
open class GlobalResult {
    @field:SerializedName("code")
    open var code : String = ""

    @field:SerializedName("message")
    open var message : String = ""

    @field:SerializedName("token")
    open var token: String? = ""

    @field:SerializedName("ongkir")
    open var ongkir: Int? = 0
}