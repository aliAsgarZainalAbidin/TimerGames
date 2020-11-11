package com.selayar.history.fragment.detail.more

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.selayar.history.Models.Gambar
import com.selayar.history.Models.WisataSejarah
import com.selayar.history.R
import kotlinx.android.synthetic.main.fragment_more.view.*
import kotlinx.android.synthetic.main.rv_image_item.view.*

class GambarAdapter(private val listGambar : List<WisataSejarah>) : RecyclerView.Adapter<GambarAdapter.GambarViewHolder>() {

    class GambarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(gambar: WisataSejarah, position: Int){
            with(itemView){
                Glide.with(itemView.context)
                    .load(gambar.background)
                    .into(iv_image_item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GambarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_image_item, parent, false)
        return GambarViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listGambar.size
    }

    override fun onBindViewHolder(holder: GambarViewHolder, position: Int) {
        holder.bind(listGambar.get(position), position)
    }
}