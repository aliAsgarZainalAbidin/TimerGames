package com.selayar.history.Models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject

@Keep
open class Gambar(
    @field:SerializedName("img")
    open var img: String? = ""
) : RealmObject()