package com.selayar.history.Util

import java.text.NumberFormat
import java.util.*

open class GeneralHelper {
    companion object {
        fun convertRupiah(angka:Double):String{
            val localeID = Locale("in", "ID")
            val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
            return formatRupiah.format(angka).replace("Rp","Rp ").replace(",00","")
        }
    }
}