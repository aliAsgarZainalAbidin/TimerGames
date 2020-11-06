package com.selayar.history.Models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject

@Keep
open class Gambar(
    @field:SerializedName("id_gambar")
    open var idGambar: Int = -1,

    @field:SerializedName("gambar")
    open var urlGambar: Int? = 0
) : RealmObject(){

}