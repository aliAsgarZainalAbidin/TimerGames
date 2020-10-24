package com.selayar.history.Model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
open class ExampeModel (
        @field:SerializedName("id")
        open var id: Int = 0,
        @field:SerializedName("nama")
        open var nama: String = ""
)