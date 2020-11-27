package com.selayar.history.fragment.home

import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import com.selayar.history.BuildConfig.TAG
import com.selayar.history.Model.ModelListWrapper
import com.selayar.history.Models.WisataSejarah
import com.selayar.history.R
import com.selayar.history.Retrofit.ApiFactory
import com.selayar.history.di.Injectable
import com.selayar.history.fragment.detail.DetailFragment
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers.io
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

class HomeFragment : Fragment(), Injectable, WisataOnClickListener {

    var listWisata = ArrayList<WisataSejarah>()

    private val restForeground by lazy {ApiFactory.create(false)}
    private var disposable: Disposable? = null
    private var page = 1
    private lateinit var adapter : WisataAdapter
    var finished = "false"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getAllWisata()
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                fab_home_scan.isEnabled = true
            }
            else -> {
                fab_home_scan.isEnabled = false
            }
        }

        val snapHelper = GravitySnapHelper(Gravity.CENTER)
        snapHelper.attachToRecyclerView(rv_home_item)

        //listWisata = ExampleData().getExampleData()
        var layoutManager = activity?.let { CenterZoomLinearLayoutManager(it) }
//        var layoutManager = activity?.let { LinearLayoutManager(it,LinearLayoutManager.VERTICAL,false) }
        adapter = WisataAdapter(listWisata, this)
        adapter.notifyDataSetChanged()
        rv_home_item.layoutManager = layoutManager
        rv_home_item.adapter = adapter
        rv_home_item.setHasFixedSize(true)

        postponeEnterTransition()

        rv_home_item.apply {
            postponeEnterTransition()
            viewTreeObserver.addOnPreDrawListener {
                startPostponedEnterTransition()
                true
            }
        }

        fab_home_scan.setOnClickListener {
            (activity as AppCompatActivity).findNavController(R.id.nav_host_fragment_container)
                .navigate(R.id.action_homeFragment_to_scannerFragment)
        }

        iv_home_search.setOnClickListener {
            (activity as AppCompatActivity).findNavController(R.id.nav_host_fragment_container)
                .navigate(R.id.action_homeFragment_to_searchFragment)
        }

        rootNested?.viewTreeObserver?.addOnScrollChangedListener {
            var view : View? = rootNested?.getChildAt(rootNested?.childCount!! - 1)
            var diff = view?.right?.minus((rootNested?.width!! + rootNested?.scrollX!!))

            if (diff == 0 && finished == "false") {
                if (layoutManager?.findLastVisibleItemPosition() == layoutManager?.itemCount?.minus(
                        1
                    )) {
                    finished = "waiting"
                    Handler().postDelayed({
                        page += 1
                        getAllWisata()
                    }, 500)
                }
            }
        }
    }

    fun getAllWisata(){
        disposable = restForeground
            .getAllWisata(page)
            .subscribeOn(io())
            .observeOn(mainThread())
            .subscribe({
                onSuccessWisata(it)
            },{
                Log.d(TAG, "getAllWisata: $it")
            })
        Log.d(TAG, "getAllWisata: REQUEST $page")
    }

    fun onSuccessWisata(data: ModelListWrapper<WisataSejarah>){
        data.data?.let {
            if (page == 1){
                listWisata.clear()
                listWisata.addAll(it)
                adapter.notifyDataSetChanged()
            } else {
                val position = listWisata.size
                listWisata.addAll(it)
                adapter.notifyItemInserted(position)
            }

            Log.d(TAG, "onSuccessWisata: SIZE ${it.size}")
            if (it.size > 0){
                finished = "false"
            } else {
                finished = "true"
                Log.d(TAG, "onSuccessWisata: $finished")
            }
            adapter.notifyDataSetChanged()
        }

        Log.d(TAG, "onSuccessWisata: ${data.data}")
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
            .navigate(R.id.action_homeFragment_to_detailFragment, bundle, null, extras)
    }
}