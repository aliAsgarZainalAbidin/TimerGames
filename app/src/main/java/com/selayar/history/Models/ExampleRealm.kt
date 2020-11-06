package com.selayar.history.Model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

@Keep
open class ExampleRealm (
        @field:SerializedName("id_example")
        @PrimaryKey
        open var id_example: Int? = 0,
        @field:SerializedName("nama_example")
        open var nama_example: String? = ""
) : RealmObject(){
    var isSelected = false
}