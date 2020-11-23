package com.selayar.history.fragment.detail.more

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import com.selayar.history.BuildConfig.TAG
import com.selayar.history.Model.ModelListWrapper
import com.selayar.history.Models.Gambar
import com.selayar.history.Models.WisataSejarah
import com.selayar.history.R
import com.selayar.history.Retrofit.ApiFactory
import com.selayar.history.fragment.detail.DetailFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_more.*

class MoreFragment : Fragment() {

    companion object{
        val NAMA = "NAMA"
        val SLUG = "SLUG"
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
    private var listGambar  = ArrayList<Gambar>()
    private lateinit var adapter  : GambarAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_more, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nama = arguments?.getString(NAMA).toString()
        slug = arguments?.getString(DetailFragment.SLUG).toString()
        description = arguments?.getString(DESCRIPTION).toString()
        location = arguments?.getString(LOCATION).toString()
        bg = arguments?.getString(BG).toString()

        Log.d(TAG, "onViewCreated: $nama")
        getWisata(slug)
        val snapHelper = GravitySnapHelper(Gravity.START)
        snapHelper.attachToRecyclerView(rv_more_gambars)

        var layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        adapter = GambarAdapter(listGambar)
        rv_more_gambars.layoutManager = layoutManager
        rv_more_gambars.adapter = adapter
        rv_more_gambars.setHasFixedSize(true)

        tv_more_nama.text = nama
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tv_more_ket.text = Html.fromHtml(description, Html.FROM_HTML_MODE_COMPACT)
        } else {
            tv_more_ket.text = Html.fromHtml(description)
        }
        tv_more_lokasi.text = location
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
            for (wisataSejarah in it){
                if (wisataSejarah.gambars?.size ==0){
                    tv_more_title_gambar.visibility = View.INVISIBLE
                }
                wisataSejarah.gambars?.let { gambar -> listGambar.addAll(gambar) }
                adapter.notifyDataSetChanged()
            }
        }
    }

}