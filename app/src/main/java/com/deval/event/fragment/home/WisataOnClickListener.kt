package com.deval.event.fragment.home

import androidx.cardview.widget.CardView
import com.deval.event.Models.WisataSejarah

interface WisataOnClickListener {
    fun WisataOnClickListener(wisataSejarah: WisataSejarah, position : Int, root: CardView){}
}