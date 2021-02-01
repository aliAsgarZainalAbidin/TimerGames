package com.deval.event.Models

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.Required
import io.realm.internal.Keep

@Keep
open class PesertaFinish(
    @field:SerializedName("id")
    open var id: String? = "",

    @field:SerializedName("name")
    open var nama: String? = "",

    @field:SerializedName("hp")
    open var hp: String? = "",

    @field:SerializedName("position_id")
    open var idJabatan: String? = "",

    @field:SerializedName("stage1")
    open var stage1: String? = "",

    @field:SerializedName("time_game1")
    open var timeGame1: String? = "",

    @field:SerializedName("stage2")
    open var stage2: String? = "",

    @field:SerializedName("time_game2")
    open var timeGame2: String? = "",

    @field:SerializedName("stage3")
    open var stage3: String? = "",

    @field:SerializedName("time_game3")
    open var timeGame3: String? = "",

    @field:SerializedName("stage4")
    open var stage4: String? = "",

    @field:SerializedName("time_game4")
    open var timeGame4: String? = "",

    @field:SerializedName("stage5")
    open var stage5: String? = "",

    @field:SerializedName("time_game5")
    open var timeGame5: String? = "",

    @field:SerializedName("stage6")
    open var stage6: String? = "",

    @field:SerializedName("time_game6")
    open var timeGame6: String? = ""

) : RealmObject()