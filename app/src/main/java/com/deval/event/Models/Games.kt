package com.deval.event.Models

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.internal.Keep

@Keep
open class Games (

    @field:SerializedName("id")
    open var idGames: String? = "",

    @field:SerializedName("nama")
    open var namaGames: String? = "",

    @field:SerializedName("ket")
    open var descGames: String? = "",

    @field:SerializedName("bg")
    open var bg: String? = ""

) : RealmObject()