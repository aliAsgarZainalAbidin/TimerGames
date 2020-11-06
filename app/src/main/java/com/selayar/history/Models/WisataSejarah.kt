package com.selayar.history.Models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

@Keep
open class WisataSejarah(
    @field:SerializedName("id_wisata")
    @PrimaryKey
    open var id_wisata: Int? = 0,

    @field:SerializedName("nama")
    open var nama: String? = "",

    @field:SerializedName("background")
    open var background: Int? = 0,

    @field:SerializedName("gambars")
    open var gambars: RealmList<Gambar>? = RealmList(),

    @field:SerializedName("keterangan")
    open var ket: String? = ""
) : RealmObject()