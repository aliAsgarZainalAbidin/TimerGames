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
import com.deval.event.BuildConfig.TAG
import com.deval.event.Model.ModelListWrapper
import com.deval.event.Models.Games
import com.deval.event.Models.ModelWrapper
import com.deval.event.Models.Peserta
import com.deval.event.Models.WisataSejarah
import com.deval.event.R
import com.deval.event.Retrofit.ApiFactory
import com.deval.event.fragment.detail.DetailFragment
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers.io
import kotlinx.android.synthetic.main.fragment_scanner.*

class ScannerFragment : Fragment() {

    companion object{
        val ID = "ID"
        val NAMA = "NAMA"
        val DESC = "DESC"
        val BG = "BG"
    }

    private lateinit var nama : String
    private lateinit var id : String
    private lateinit var description : String
    private lateinit var bg : String

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

        id = arguments?.getString(ID).toString()
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

    fun getQRCode(id : String) {
        disposable = restForeground
            .getQR(id)
            .subscribeOn(io())
            .observeOn(mainThread())
            .subscribe({
                codeScanner.releaseResources()
                it.data?.let { data -> onSuccessQR(data) }
            }, {
                Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
                Log.d(TAG, "getQRCode: $it")
            })
    }

    fun onSuccessQR(data: Peserta) {
        data.let {
            val namaPeserta :String? = it.nama
            Toast.makeText(requireContext(), namaPeserta, Toast.LENGTH_SHORT).show()
            val bundle = Bundle()
            bundle.putString(DetailFragment.ID, id)

            if (namaPeserta.isNullOrEmpty()){
                //Arahkan Ke Update data akun
                Log.d(TAG, "onSuccessQR: NULL NAME")
                (activity as AppCompatActivity).findNavController(R.id.nav_host_fragment_container)
                    .navigate(R.id.action_scannerFragment_to_regisFragment, bundle)
            } else {
                bundle.putString(DetailFragment.NAMA, namaPeserta)
                Log.d(TAG, "onSuccessQR: $namaPeserta")
                //Arahkan Ke Timer
                (activity as AppCompatActivity).findNavController(R.id.nav_host_fragment_container)
                    .navigate(R.id.action_scannerFragment_to_detailFragment, bundle)
            }
        }
    }

}