package com.deval.event.Models

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.internal.Keep

@Keep
open class Peserta(
    @field:SerializedName("id")
    open var id: String? = "",

    @field:SerializedName("nama")
    open var nama: String? = "",

    @field:SerializedName("ket")
    open var hp: String? = "",

    @field:SerializedName("jabatan")
    open var jabatan: String? = "",

    @field:SerializedName("score_game1")
    open var scoreGame1: String? = "",

    @field:SerializedName("time_game1")
    open var timeGame1: String? = "",

    @field:SerializedName("score_game2")
    open var scoreGame2: String? = "",

    @field:SerializedName("time_game2")
    open var timeGame2: String? = "",

    @field:SerializedName("score_game3")
    open var scoreGame3: String? = "",

    @field:SerializedName("time_game3")
    open var timeGame3: String? = "",

    @field:SerializedName("score_game4")
    open var scoreGame4: String? = "",

    @field:SerializedName("time_game4")
    open var timeGame4: String? = "",

    @field:SerializedName("score_game5")
    open var scoreGame5: String? = "",

    @field:SerializedName("time_game5")
    open var timeGame5: String? = "",

    @field:SerializedName("score_game6")
    open var scoreGame6: String? = "",

    @field:SerializedName("time_game6")
    open var timeGame6: String? = ""

) : RealmObject()