package com.selayar.history.fragment.home

import android.graphics.text.LineBreaker
import android.os.Build
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.selayar.history.BuildConfig.TAG
import com.selayar.history.R
import com.selayar.history.Models.WisataSejarah
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.rv_layout_item.view.*

class WisataAdapter(private val list: ArrayList<WisataSejarah>, private val listener: WisataOnClickListener) :
    RecyclerView.Adapter<WisataAdapter.ViewHolder>() {
    class ViewHolder(val containerView: View) : RecyclerView.ViewHolder(containerView) {
        fun bind(wisataSejarah: WisataSejarah, position: Int, size : Int, listener: WisataOnClickListener) {
            with(itemView) {
                val layoutParams = FrameLayout.LayoutParams(root.layoutParams)
                if (position == size-1) {
                    layoutParams.setMargins(
                        0,
                        context.resources.getDimension(R.dimen._10dp).toInt(),
                        context.resources.getDimension(R.dimen._24dp).toInt(),
                        context.resources.getDimension(R.dimen._10dp).toInt()
                    )
                } else if (position == 0) {
                    layoutParams.setMargins(
                        context.resources.getDimension(R.dimen._24dp).toInt(),
                        context.resources.getDimension(R.dimen._10dp).toInt(),
                        0,
                        context.resources.getDimension(R.dimen._10dp).toInt()
                    )
                } else {
                    layoutParams.setMargins(
                        0,
                        context.resources.getDimension(R.dimen._10dp).toInt(),
                        0,
                        context.resources.getDimension(R.dimen._10dp).toInt()
                    )
                }
                root.layoutParams = layoutParams
                tv_layout_item_title.text = wisataSejarah.nama
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    tv_layout_item_ket.text = Html.fromHtml(wisataSejarah.deskripsi,Html.FROM_HTML_MODE_COMPACT)
                } else {
                    tv_layout_item_ket.text = Html.fromHtml(wisataSejarah.deskripsi)
                }

                Glide.with(itemView.context)
                    .load(wisataSejarah.bg)
                    .into(iv_layout_item_bg)

                ViewCompat.setTransitionName(iv_layout_item_bg,"fadeIn$position")
                ViewCompat.setTransitionName(tv_layout_item_title,"title$position")
                ViewCompat.setTransitionName(tv_layout_item_ket,"ket$position")

                root.setOnClickListener {
                    listener.WisataOnClickListener(wisataSejarah, position, this as CardView)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.rv_layout_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list.get(position), position, list.size, listener)
    }
}