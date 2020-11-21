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

    @field:SerializedName("background")
    open var background: Int? = 0,

    @field:SerializedName("name")
    open var nama: String? = "",

    @field:SerializedName("description")
    open var deskripsi: String? = "",

    @field:SerializedName("location")
    open var location: String? = "",

    @field:SerializedName("bg")
    open var bg: String? = "",

    @field:SerializedName("slug")
    open var slug: String? = "",

    @field:SerializedName("author")
    open var author: String? = "",

    @field:SerializedName("travel_images")
    open var gambars: RealmList<Gambar>? = RealmList(),

    @field:SerializedName("created_at")
    open var time: String? = "",

    @field:SerializedName("keterangan")
    open var ket: String? = ""

) : RealmObject()