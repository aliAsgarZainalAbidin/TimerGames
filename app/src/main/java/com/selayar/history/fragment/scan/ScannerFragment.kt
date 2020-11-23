package com.selayar.history.fragment.scan

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
import com.selayar.history.BuildConfig.TAG
import com.selayar.history.Model.ModelListWrapper
import com.selayar.history.Models.WisataSejarah
import com.selayar.history.R
import com.selayar.history.Retrofit.ApiFactory
import com.selayar.history.fragment.detail.DetailFragment
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers.io
import kotlinx.android.synthetic.main.fragment_scanner.*

class ScannerFragment : Fragment() {

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

        codeScanner = CodeScanner(requireActivity(), scanner)

        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not

        codeScanner.decodeCallback = DecodeCallback {
            activity?.runOnUiThread {
                getQRCode(it.toString())
                Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
            }
        }
        scanner.setOnClickListener {
            codeScanner.startPreview()
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

    fun getQRCode(slug : String) {
        disposable = restForeground
            .getQR(slug)
            .subscribeOn(io())
            .observeOn(mainThread())
            .subscribe({
                codeScanner.releaseResources()
                onSuccessQR(it)
            }, {
                Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
                Log.d(TAG, "getQRCode: $it")
            })
    }

    fun onSuccessQR(data: ModelListWrapper<WisataSejarah>) {
        data.data?.let {
            val bundle = Bundle()
            for (data in it){
                bundle.putString(DetailFragment.NAMA,data.nama)
                bundle.putString(DetailFragment.DESCRIPTION,data.deskripsi)
                bundle.putString(DetailFragment.BG,data.bg)
                bundle.putString(DetailFragment.LOCATION,data.location)
                bundle.putString(DetailFragment.SLUG,data.slug)

                Toast.makeText(context, data.nama, Toast.LENGTH_SHORT).show()
            }

            (activity as AppCompatActivity).findNavController(R.id.nav_host_fragment_container)
                .navigate(R.id.action_scannerFragment_to_detailFragment, bundle)
        }
    }

}