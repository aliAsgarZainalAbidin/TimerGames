package com.deval.event.fragment.scan

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ScanMode
import com.deval.event.BuildConfig
import com.deval.event.BuildConfig.TAG
import com.deval.event.Featured.GlideApp
import com.deval.event.Model.ModelListWrapper
import com.deval.event.Models.Games
import com.deval.event.Models.ModelWrapper
import com.deval.event.Models.Peserta
import com.deval.event.Models.WisataSejarah
import com.deval.event.R
import com.deval.event.Retrofit.ApiFactory
import com.deval.event.Util.BaseFragment
import com.deval.event.fragment.detail.DetailFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.Schedulers.io
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.fragment_scanner.*

class ScannerFragment : BaseFragment() {

    companion object {
        val ID = "ID"
        val TYPE = "TYPE"
        val TYPE_OUT = "TYPE_OUT"
        val SLUG = "SLUG"
        val NAMA = "NAMA"
        val DESC = "DESC"
        val BG = "BG"
    }

    private lateinit var nama: String
    private lateinit var idGames: String
    private lateinit var slug: String
    private lateinit var type: String
    private lateinit var description: String
    private lateinit var bg: String
    var namaPeserta : String? = null
    var id : String? = null

    lateinit var codeScanner: CodeScanner

    private val restForeground by lazy { ApiFactory.create(false) }
    private var disposable: Disposable? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scanner, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        slug = arguments?.getString(SLUG).toString()
        type = arguments?.getString(TYPE).toString()
        codeScanner = CodeScanner(requireActivity(), scanner)
        getGameShow(slug)

        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not

        codeScanner.decodeCallback = DecodeCallback {
            activity?.runOnUiThread {
                Log.d(TAG, "onViewCreated: $type")
                if (!type.equals(TYPE_OUT)) {
                    getQRCode(it.toString())
                } else {
                    scanOut(it.toString())
                }
//                Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }

    fun showDialog(){
        dialogAnimation()
        dialogAnimationImage?.visibility = View.GONE
        dialogAnimationTitle?.text = ""
        dialogAnimationDesc?.text = "Anda telah menyelesaikan game ini, Silahkan lanjut ke stage berikutnya"
        dialogAnimationProgress?.visibility = View.INVISIBLE
        dialogAnimationImage?.visibility = View.VISIBLE
        dialogAnimationImage?.let {
            context?.let { it1 ->
                GlideApp.with(it1)
                    .load(R.drawable.ic_baseline_error_outline_24)
                    .into(it)
            }
        }
    }

    fun showDialogFull(){
        dialogAnimation()
        dialogAnimationImage?.visibility = View.GONE
        dialogAnimationTitle?.text = ""
        dialogAnimationDesc?.text = "Anda telah menyelesaikan semua permainan"
        dialogAnimationProgress?.visibility = View.INVISIBLE
        dialogAnimationImage?.visibility = View.VISIBLE
        dialogAnimationImage?.let {
            context?.let { it1 ->
                GlideApp.with(it1)
                    .load(R.drawable.ic_baseline_error_outline_24)
                    .into(it)
            }
        }
    }

    fun getGameShow(slug: String) {
        disposable = restForeground
            .getGameShow(slug)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                it.data?.let { data -> onSuccessGame(data) }
            }, {
//                Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
                Log.d(BuildConfig.TAG, "getGameShow: $it")
            })
    }

    fun onSuccessGame(data: Games) {
        data.let {
            idGames = data.id.toString()
        }
    }

    fun scanOut(id: String) {
        disposable = restForeground
            .scanOut(id)
            .subscribeOn(io())
            .observeOn(mainThread())
            .subscribe({
                (activity as AppCompatActivity).findNavController(R.id.nav_host_fragment_container)
                    .navigate(R.id.action_scannerFragment_to_homeFragment)
            }, {
//                Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
                Log.d(TAG, "scanOut: $it")
            })
    }

    fun checkScore(id: String) {
        disposable = restForeground
            .checkScore(id)
            .subscribeOn(io())
            .observeOn(mainThread())
            .subscribe({
                Log.d(TAG, "checkScore: ${it.data?.nama}")
                if (idGames.equals("1") && it.data?.stage1?.toInt() ?:0 > 0){
                    showDialog()
                    Log.d(TAG, "checkScore: 6")
                } else if (idGames.equals("2") && it.data?.stage2?.toInt()?:0 > 0){
                    showDialog()
                    Log.d(TAG, "checkScore: 5")
                }else if (idGames.equals("3") && it.data?.stage3?.toInt()?:0 > 0){
                    showDialog()
                    Log.d(TAG, "checkScore: 4")
                }else if (idGames.equals("4") && it.data?.stage4?.toInt()?:0 > 0){
                    showDialog()
                    Log.d(TAG, "checkScore: 3")
                }else if (idGames.equals("5") && it.data?.stage5?.toInt()?:0 > 0){
                    showDialog()
                    Log.d(TAG, "checkScore: 2")
                }else if (idGames.equals("6") && it.data?.stage6?.toInt()?:0 > 0){
                    showDialogFull()
                    Log.d(TAG, "checkScore: 1")
                }else {
                    val bundle = Bundle()
                    bundle.putString(DetailFragment.SLUG, slug)
                    bundle.putString(DetailFragment.ID_NAMA, id)
                    bundle.putString(DetailFragment.NAMA, namaPeserta)

                    (activity as AppCompatActivity).findNavController(R.id.nav_host_fragment_container)
                        .navigate(R.id.action_scannerFragment_to_detailFragment, bundle)
                }
            }, {
                Log.d(TAG, "checkScore: ${it.toString()}")
            })
    }

    fun getQRCode(id: String) {
        Log.d(TAG, "getQRCode: $id")
        disposable = restForeground
            .getQR(id.toInt())
            .subscribeOn(io())
            .observeOn(mainThread())
            .subscribe({
                codeScanner.releaseResources()
                it.data?.let { data -> onSuccessQR(data, id) }
            }, {
//                Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
                Log.d(TAG, "getQRCode: $it")
            })
    }

    fun onSuccessQR(data: Peserta, idPeserta : String) {
        data.let {
            namaPeserta = it.nama
            id = data.id
            Toast.makeText(requireContext(), namaPeserta, Toast.LENGTH_SHORT).show()
            val bundle = Bundle()
            bundle.putString(DetailFragment.SLUG, slug)
            bundle.putString(DetailFragment.ID_NAMA, id)

            if (namaPeserta.isNullOrEmpty()) {
                //Arahkan Ke Update data akun
                Log.d(TAG, "onSuccessQR: NULL NAME")
                (activity as AppCompatActivity).findNavController(R.id.nav_host_fragment_container)
                    .navigate(R.id.action_scannerFragment_to_regisFragment, bundle)
            } else {
                checkScore(idPeserta)
            }
        }
    }

}