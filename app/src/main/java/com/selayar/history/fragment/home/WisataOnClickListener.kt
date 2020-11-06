package com.selayar.history.fragment.home

import androidx.cardview.widget.CardView
import com.selayar.history.Models.WisataSejarah

interface WisataOnClickListener {
    fun WisataOnClickListener(wisataSejarah: WisataSejarah, position : Int, root: CardView){}
}