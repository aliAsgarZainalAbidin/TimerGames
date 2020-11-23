package com.selayar.history.fragment.detail

import android.annotation.SuppressLint
import android.graphics.text.LineBreaker.JUSTIFICATION_MODE_INTER_WORD
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.transition.ChangeBounds
import android.transition.TransitionInflater
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.selayar.history.BuildConfig.TAG
import com.selayar.history.Featured.GlideApp
import com.selayar.history.Model.ModelListWrapper
import com.selayar.history.Model.ModelWrapper
import com.selayar.history.Models.WisataSejarah
import com.selayar.history.R
import com.selayar.history.Retrofit.ApiFactory
import com.selayar.history.fragment.detail.more.MoreFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.rv_layout_item.view.*
import java.util.concurrent.TimeUnit

class DetailFragment : Fragment() {

    companion object{
        val SLUG = "SLUG"
        val NAMA = "NAMA"
        val DESCRIPTION = "DESCRIPTION"
        val LOCATION = "LOCATION"
        val BG = "BG"
    }

    private val restForeground by lazy { ApiFactory.create(false) }
    private var disposable: Disposable? = null
    private lateinit var nama : String
    private lateinit var description : String
    private lateinit var location : String
    private lateinit var bg : String
    private lateinit var slug : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(R.transition.show_on_top)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nama = arguments?.getString(NAMA).toString()
        slug = arguments?.getString(SLUG).toString()
        description = arguments?.getString(DESCRIPTION).toString()
        location = arguments?.getString(LOCATION).toString()
        bg = arguments?.getString(BG).toString()

        var requestOption = RequestOptions
            .diskCacheStrategyOf(DiskCacheStrategy.ALL)

        Glide.with(this)
            .load(bg)
            .thumbnail(Glide.with(this).load(R.mipmap.round_logo).thumbnail(0.25f))
            .apply(requestOption)
            .into(iv_detail_bg)

        tv_detail_title.setText(nama)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tv_detail_ket.text = Html.fromHtml(description, Html.FROM_HTML_MODE_COMPACT)
        } else {
            tv_detail_ket.text = Html.fromHtml(description)
        }

        tv_detail_more.setOnClickListener {
            getWisata(slug)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }

    fun getWisata(nama : String) {
        disposable = restForeground
            .getQR(nama)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                onSuccess(it)
            }, {

            })
    }

    fun onSuccess(data: ModelListWrapper<WisataSejarah>) {
        data.data?.let {
            val bundle = Bundle()

            for (wisataSejarah in it){
                bundle.putString(MoreFragment.NAMA,wisataSejarah.nama)
                bundle.putString(MoreFragment.DESCRIPTION,wisataSejarah.deskripsi)
                bundle.putString(MoreFragment.BG,wisataSejarah.bg)
                bundle.putString(MoreFragment.LOCATION,wisataSejarah.location)
                bundle.putString(MoreFragment.SLUG,wisataSejarah.slug)
            }
            Log.d(TAG, "onSuccess: ${bundle.getString(NAMA)}")
            findNavController().navigate(R.id.action_detailFragment_to_moreFragment, bundle)
        }
    }

}