package com.deval.event.Models

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.internal.Keep

@Keep
open class Unit (
    @field:SerializedName("id")
    open var id: String? = "",

    @field:SerializedName("name")
    open var nama: String? = ""
): RealmObject()