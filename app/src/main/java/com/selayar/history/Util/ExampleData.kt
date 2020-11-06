package com.selayar.history.Util

import com.selayar.history.R
import com.selayar.history.Models.Gambar
import com.selayar.history.Models.WisataSejarah

class ExampleData {
    fun getExampleData() : ArrayList<WisataSejarah>{
        var namaWisata = arrayListOf<WisataSejarah>()
        var wisataSejarah = WisataSejarah()
        val gambar = Gambar(1, R.drawable.wisata)
        var listGambar = arrayListOf(gambar,gambar,gambar,gambar)
        for(x in 0 until 4){
            wisataSejarah.nama = "Gong Nekara"
            wisataSejarah.background = R.drawable.wisata
            wisataSejarah.gambars?.addAll(listGambar)
            wisataSejarah.ket = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut " +
                    "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut " +
                    "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore " +
                    "eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt " +
                    "mollit anim id est laborum."
            namaWisata.add(wisataSejarah)
        }
        return namaWisata
    }
}