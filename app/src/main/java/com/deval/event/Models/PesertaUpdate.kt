package com.deval.event.Models

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.internal.Keep

@Keep
open class PesertaUpdate(
    @field:SerializedName("name")
    open var nama: String? = "",

    @field:SerializedName("hp")
    open var hp: String? = "",

    @field:SerializedName("position_id")
    open var idJabatan: String? = ""
) : RealmObject()