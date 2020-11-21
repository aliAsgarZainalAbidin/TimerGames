package com.selayar.history.fragment.home

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import com.selayar.history.BuildConfig.TAG
import com.selayar.history.R
import com.selayar.history.Models.WisataSejarah
import com.selayar.history.Util.ExampleData
import com.selayar.history.di.Injectable
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.rv_layout_item.*
import kotlinx.android.synthetic.main.rv_layout_item.view.*
import kotlinx.android.synthetic.main.rv_layout_item.view.iv_layout_item_bg
import java.util.jar.Manifest
import javax.inject.Inject

class HomeFragment : Fragment(), Injectable, WisataOnClickListener {

    var listWisata = ArrayList<WisataSejarah>()

    @Inject
    lateinit var realm: Realm

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


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

        listWisata = ExampleData().getExampleData()
        var layoutManager = activity?.let { CenterZoomLinearLayoutManager(it) }
        val adapter = WisataAdapter(listWisata, this)
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
    }

    override fun WisataOnClickListener(
        wisataSejarah: WisataSejarah,
        position: Int,
        root: CardView
    ) {
        super.WisataOnClickListener(wisataSejarah, position, root)
        val extras = FragmentNavigatorExtras(
            root.iv_layout_item_bg to "fadeIn",
            root.tv_layout_item_title to "title",
            root.tv_layout_item_ket to "ket"
        )
        (activity as AppCompatActivity).findNavController(R.id.nav_host_fragment_container)
            .navigate(R.id.action_homeFragment_to_detailFragment, null, null, extras)
    }
}