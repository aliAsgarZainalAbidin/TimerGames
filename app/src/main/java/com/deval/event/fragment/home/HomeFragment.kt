package com.deval.event.fragment.home

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
import com.deval.event.BuildConfig.TAG
import com.deval.event.Model.ModelListWrapper
import com.deval.event.Models.Games
import com.deval.event.Models.WisataSejarah
import com.deval.event.R
import com.deval.event.Retrofit.ApiFactory
import com.deval.event.di.Injectable
import com.deval.event.fragment.detail.DetailFragment
import com.deval.event.fragment.scan.ScannerFragment
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers.io
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(), Injectable, GamesOnClickListener {

    var listGames = ArrayList<Games>()

    private val restForeground by lazy { ApiFactory.create(false) }
    private var disposable: Disposable? = null
    private var page = 1
    private lateinit var adapterGames: GamesAdapter
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

//        for (i in 1..3) {
//            var games = Games()
//            games.idGames = "${i}"
//            games.namaGames = "Permainan Pertama"
//            games.descGames = "Deskripsi Permainan Pertama"
//            games.bg = R.drawable.wisata.toString()
//            listGames.add(games)
//        }

        getAllGames()
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
//                fab_home_scan.isEnabled = true
            }
            else -> {
//                fab_home_scan.isEnabled = false
            }
        }

        val snapHelper = GravitySnapHelper(Gravity.CENTER)
        snapHelper.attachToRecyclerView(rv_home_item)

        var layoutManager = activity?.let { CenterZoomLinearLayoutManager(it) }
        adapterGames = GamesAdapter(listGames, this)
        adapterGames.notifyDataSetChanged()
        rv_home_item.layoutManager = layoutManager
        rv_home_item.adapter = adapterGames
        rv_home_item.setHasFixedSize(true)

        postponeEnterTransition()

        rv_home_item.apply {
            postponeEnterTransition()
            viewTreeObserver.addOnPreDrawListener {
                startPostponedEnterTransition()
                true
            }
        }

        rootNested?.viewTreeObserver?.addOnScrollChangedListener {
            var view: View? = rootNested?.getChildAt(rootNested?.childCount!! - 1)
            var diff = view?.right?.minus((rootNested?.width!! + rootNested?.scrollX!!))

            if (diff == 0 && finished == "false") {
                if (layoutManager?.findLastVisibleItemPosition() == layoutManager?.itemCount?.minus(
                        1
                    )
                ) {
                    finished = "waiting"
                    Handler().postDelayed({
                        page += 1
                        getAllGames()
                    }, 500)
                }
            }
        }
    }

    fun getAllGames() {
        disposable = restForeground
            .getAllGames()
            .subscribeOn(io())
            .observeOn(mainThread())
            .subscribe({
                onSuccessGames(it)
            }, {
                Log.d(TAG, "getAllGames: $it")
            })
    }

    fun onSuccessGames(data: ModelListWrapper<Games>) {
        data.data?.let {
            if (page == 1) {
                listGames.clear()
                listGames.addAll(it)
                adapterGames.notifyDataSetChanged()
            } else {
                val position = listGames.size
                listGames.clear()
                listGames.addAll(it)
                adapterGames.notifyItemInserted(position)
            }

            Log.d(TAG, "onSuccessGames: SIZE ${it.size}")
            if (it.size > 0) {
                finished = "false"
            } else {
                finished = "true"
                Log.d(TAG, "onSuccessGames: $finished")
            }
            adapterGames.notifyDataSetChanged()
        }

        Log.d(TAG, "onSuccessGames: ${data.data}")
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }

    override fun GameOnClickListener(games: Games, position: Int, root: CardView) {
        super.GameOnClickListener(games, position, root)
        val bundle = Bundle()
        bundle.putString(ScannerFragment.SLUG, games.idGames)

        (activity as AppCompatActivity).findNavController(R.id.nav_host_fragment_container)
            .navigate(R.id.action_homeFragment_to_scannerFragment, bundle)
    }
}