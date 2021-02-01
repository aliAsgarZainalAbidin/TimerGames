package com.deval.event.Models

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.internal.Keep

@Keep
open class Games (

    @field:SerializedName("slug")
    open var idGames: String? = "",

    @field:SerializedName("name")
    open var namaGames: String? = "",

    @field:SerializedName("description")
    open var descGames: String? = "",

    @field:SerializedName("duration")
    open var duration: String? = "",

    @field:SerializedName("img")
    open var bg: String? = ""

) : RealmObject()