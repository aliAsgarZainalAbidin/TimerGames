package com.deval.event.Models

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.Required
import io.realm.internal.Keep

@Keep
open class PesertaFinish(
    @field:SerializedName("id")
    open var id: Int? = -1,

    @field:SerializedName("name")
    open var nama: String? = "",

    @field:SerializedName("hp")
    open var hp: String? = "",

    @field:SerializedName("position_id")
    open var idJabatan: String? = "",

    @field:SerializedName("stage1")
    open var stage1: String? = "",

    @field:SerializedName("time1")
    open var time1: String? = "",

    @field:SerializedName("stage2")
    open var stage2: String? = "",

    @field:SerializedName("time2")
    open var time2: String? = "",

    @field:SerializedName("stage3")
    open var stage3: String? = "",

    @field:SerializedName("time3")
    open var time3: String? = "",

    @field:SerializedName("stage4")
    open var stage4: String? = "",

    @field:SerializedName("time4")
    open var time4: String? = "",

    @field:SerializedName("stage5")
    open var stage5: String? = "",

    @field:SerializedName("time5")
    open var time5: String? = "",

    @field:SerializedName("stage6")
    open var stage6: String? = "",

    @field:SerializedName("time6")
    open var time6: String? = ""

) : RealmObject()