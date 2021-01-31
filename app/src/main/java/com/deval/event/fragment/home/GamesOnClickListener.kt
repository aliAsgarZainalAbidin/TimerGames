package com.deval.event.fragment.home

import androidx.cardview.widget.CardView
import com.deval.event.Models.Games
import com.deval.event.Models.WisataSejarah

interface GamesOnClickListener {
    fun GameOnClickListener(games: Games, position : Int, root: CardView){}
}