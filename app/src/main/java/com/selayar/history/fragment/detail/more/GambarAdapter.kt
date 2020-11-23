package com.selayar.history.fragment.detail.more

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.selayar.history.Models.Gambar
import com.selayar.history.Models.WisataSejarah
import com.selayar.history.R
import kotlinx.android.synthetic.main.fragment_more.view.*
import kotlinx.android.synthetic.main.rv_image_item.view.*

class GambarAdapter(private val listGambar : ArrayList<Gambar>?) : RecyclerView.Adapter<GambarAdapter.GambarViewHolder>() {

    class GambarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(gambar: Gambar?, position: Int){
            with(itemView){
                var requestOption = RequestOptions
                    .diskCacheStrategyOf(DiskCacheStrategy.ALL)

                Glide.with(itemView.context)
                    .load(gambar?.img)
                    .thumbnail(0.25f)
                    .thumbnail(Glide.with(context).load(R.mipmap.round_logo))
                    .apply(requestOption)
                    .into(iv_image_item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GambarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_image_item, parent, false)
        return GambarViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listGambar?.size ?: 0
    }

    override fun onBindViewHolder(holder: GambarViewHolder, position: Int) {
        holder.bind(listGambar?.get(position), position)
    }
}