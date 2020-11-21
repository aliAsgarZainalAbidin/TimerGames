package com.selayar.history.fragment.detail

import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.selayar.history.Featured.GlideApp
import com.selayar.history.Model.ModelWrapper
import com.selayar.history.Models.WisataSejarah
import com.selayar.history.R
import com.selayar.history.Retrofit.ApiFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_detail.*
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
        description = arguments?.getString(DESCRIPTION).toString()
        location = arguments?.getString(LOCATION).toString()
        bg = arguments?.getString(BG).toString()

        Glide.with(this)
            .load(bg)
            .into(iv_detail_bg)

        tv_detail_title.setText(nama)
        tv_detail_ket.setText(description)

        tv_detail_more.setOnClickListener {
            findNavController().navigate(R.id.action_detailFragment_to_moreFragment)
        }
    }

//    fun getQRCode() {
//        disposable = restForeground
//            .getQR()
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({
//
//            }, {
//
//            })
//    }
//
//    fun onSuccessQR(data: ModelWrapper<WisataSejarah>) {
//        data.data?.let {
//
//        }
//    }

}