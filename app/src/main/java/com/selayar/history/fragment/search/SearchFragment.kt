package com.selayar.history.fragment.search

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import com.jakewharton.rxbinding2.widget.RxTextView
import com.selayar.history.BuildConfig
import com.selayar.history.Model.ModelListWrapper
import com.selayar.history.Models.WisataSejarah
import com.selayar.history.R
import com.selayar.history.Retrofit.ApiFactory
import com.selayar.history.fragment.detail.DetailFragment
import com.selayar.history.fragment.home.CenterZoomLinearLayoutManager
import com.selayar.history.fragment.home.WisataAdapter
import com.selayar.history.fragment.home.WisataOnClickListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_search.*
import java.util.concurrent.TimeUnit

class SearchFragment : Fragment(), WisataOnClickListener {

    private val restForeground by lazy { ApiFactory.create(false)}
    private var disposable: Disposable? = null
    private lateinit var searchItem : String
    private lateinit var adapter : WisataAdapter
    var listWisata = ArrayList<WisataSejarah>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val snapHelper = GravitySnapHelper(Gravity.CENTER)
        snapHelper.attachToRecyclerView(rv_search_item)

        var layoutManager = activity?.let { CenterZoomLinearLayoutManager(it) }
        adapter = WisataAdapter(listWisata, this)
        adapter.notifyDataSetChanged()
        rv_search_item.layoutManager = layoutManager
        rv_search_item.adapter = adapter
        rv_search_item.setHasFixedSize(true)

        et_search_item?.requestFocus()
        showSoftKeyboard(et_search_item)
        et_search_item.let {
            RxTextView.textChanges(it)
                .debounce(100, TimeUnit.MILLISECONDS)
//            .filter { t -> t.length > 1 }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { text ->
                    searchItem = text.toString()
                    if (!searchItem.isNullOrBlank()){
                        getWisata(searchItem)
                    }
                }
        }


        root?.viewTreeObserver?.addOnScrollChangedListener {
            var view : View? = root?.getChildAt(root?.childCount!! - 1)
            var diff = view?.right?.minus((root?.width!! + root?.scrollX!!))

            if (diff == 0) {
                if (layoutManager?.findLastVisibleItemPosition() == layoutManager?.itemCount?.minus(
                        1
                    )) {
                    Handler().postDelayed({
                        getWisata(searchItem)
                    }, 500)
                }
            }
        }
    }

    fun showSoftKeyboard(view: View?) {
        if (view?.requestFocus()!!) {
            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }


    fun getWisata(nama : String){
        disposable = restForeground
            .getWisata(nama)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                onSuccessWisata(it)
            },{
                Log.d(BuildConfig.TAG, "getAllWisata: $it")
            })
    }

    fun onSuccessWisata(data: ModelListWrapper<WisataSejarah>){
        data.data?.let {
            listWisata.clear()
            listWisata.addAll(it)
            adapter.notifyDataSetChanged()
        }

        Log.d(BuildConfig.TAG, "onSuccessWisata: ${data.data}")
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }

    override fun WisataOnClickListener(
        wisataSejarah: WisataSejarah,
        position: Int,
        root: CardView
    ) {
        super.WisataOnClickListener(wisataSejarah, position, root)
        val extras = FragmentNavigatorExtras(
//            root.iv_layout_item_bg to "fadeIn",
//            root.tv_layout_item_title to "title",
//            root.tv_layout_item_ket to "ket"
        )

        val bundle = Bundle()
        bundle.putString(DetailFragment.NAMA,wisataSejarah.nama)
        bundle.putString(DetailFragment.DESCRIPTION,wisataSejarah.deskripsi)
        bundle.putString(DetailFragment.BG,wisataSejarah.bg)
        bundle.putString(DetailFragment.LOCATION,wisataSejarah.location)
        bundle.putString(DetailFragment.SLUG,wisataSejarah.slug)

        (activity as AppCompatActivity).findNavController(R.id.nav_host_fragment_container)
            .navigate(R.id.action_searchFragment_to_detailFragment, bundle, null, extras)
    }

}