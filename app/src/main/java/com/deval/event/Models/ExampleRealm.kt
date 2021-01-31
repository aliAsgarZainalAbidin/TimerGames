package com.deval.event.Model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject

@Keep
open class ExampleRealm (
    @field:SerializedName("img")
    open var img: String? = ""
): RealmObject(){
}