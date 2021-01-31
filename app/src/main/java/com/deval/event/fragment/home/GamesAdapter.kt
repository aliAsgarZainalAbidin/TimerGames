package com.deval.event.fragment.home

import android.os.Build
import android.text.Html
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.deval.event.Models.Games
import com.deval.event.R
import kotlinx.android.synthetic.main.rv_layout_item.view.*

class GamesAdapter(private val list: ArrayList<Games>, private val listener: GamesOnClickListener) :
    RecyclerView.Adapter<GamesAdapter.ViewHolder>() {
    class ViewHolder(val containerView: View) : RecyclerView.ViewHolder(containerView) {
        fun bind(games: Games, position: Int, size : Int, listener: GamesOnClickListener) {
            with(itemView) {
                val layoutParams = FrameLayout.LayoutParams(root.layoutParams)

                if (size>1){
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
                } else {
                    layoutParams.setMargins(
                        context.resources.getDimension(R.dimen._24dp).toInt(),
                        context.resources.getDimension(R.dimen._10dp).toInt(),
                        0,
                        context.resources.getDimension(R.dimen._10dp).toInt()
                    )
                    layoutParams.gravity = Gravity.CENTER
                }

                root.layoutParams = layoutParams
                tv_layout_item_title.text = games.namaGames
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    tv_layout_item_ket.text = Html.fromHtml(games.descGames,Html.FROM_HTML_MODE_COMPACT)
                } else {
                    tv_layout_item_ket.text = Html.fromHtml(games.descGames)
                }

                var requestOption = RequestOptions
                    .diskCacheStrategyOf(DiskCacheStrategy.ALL)

                Glide.with(itemView.context)
                    .load(games.bg?.toInt())
                    .thumbnail(Glide.with(context).load(R.mipmap.round_logo).thumbnail(0.25f))
                    .apply(requestOption)
                    .into(iv_layout_item_bg)

                ViewCompat.setTransitionName(iv_layout_item_bg,"fadeIn$position")
                ViewCompat.setTransitionName(tv_layout_item_title,"title$position")
                ViewCompat.setTransitionName(tv_layout_item_ket,"ket$position")

                root.setOnClickListener {
                    listener.GameOnClickListener(games, position, this as CardView)
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