package com.selayar.history.Model

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*
import kotlin.collections.ArrayList

@Keep
@Parcelize
open class ModelWrapper<T> : Parcelable {
    //data menu
    @field:SerializedName("code")
    open var status: String? = ""
    @field:SerializedName("message")
    open var message: String? = ""
    @field:SerializedName("token")
    open var token: String? = ""
    @field:SerializedName("data")
    open var data: T? = null
//
//    @field:SerializedName("token")
//    open var token: String? = null
}