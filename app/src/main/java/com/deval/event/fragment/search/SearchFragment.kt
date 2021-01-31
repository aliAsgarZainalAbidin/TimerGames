package com.deval.event.fragment.search

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import com.jakewharton.rxbinding2.widget.RxTextView
import com.deval.event.BuildConfig
import com.deval.event.Model.ModelListWrapper
import com.deval.event.Models.Games
import com.deval.event.Models.WisataSejarah
import com.deval.event.R
import com.deval.event.Retrofit.ApiFactory
import com.deval.event.fragment.detail.DetailFragment
import com.deval.event.fragment.home.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_search.*
import java.util.concurrent.TimeUnit

class SearchFragment : Fragment(), GamesOnClickListener {

    private val restForeground by lazy { ApiFactory.create(false)}
    private var disposable: Disposable? = null
    private lateinit var searchItem : String
    private lateinit var adapter : GamesAdapter
    var listGames = ArrayList<Games>()


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
        adapter = GamesAdapter(listGames, this)
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
        //API WISATA KE API GAMES
        disposable = restForeground
            .getWisata(nama)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                onSuccessWisata(it)
            },{
                Log.d(BuildConfig.TAG, "getAllGames: $it")
            })
    }

    fun onSuccessWisata(data: ModelListWrapper<Games>){
        data.data?.let {
            listGames.clear()
            listGames.addAll(it)
            adapter.notifyDataSetChanged()
        }

        Log.d(BuildConfig.TAG, "onSuccessGames: ${data.data}")
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }


}