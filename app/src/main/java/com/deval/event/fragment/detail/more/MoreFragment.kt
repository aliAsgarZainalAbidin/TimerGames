package com.deval.event.fragment.detail.more

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.deval.event.BuildConfig
import com.deval.event.BuildConfig.TAG
import com.deval.event.Featured.GlideApp
import com.deval.event.Models.Games
import com.deval.event.Models.Peserta
import com.deval.event.Models.PesertaFinish
import com.deval.event.R
import com.deval.event.Retrofit.ApiFactory
import com.deval.event.Util.BaseFragment
import com.deval.event.di.Injectable
import com.deval.event.fragment.detail.DetailFragment
import com.deval.event.fragment.scan.ScannerFragment
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.resolution
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.Schedulers.io
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_more.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap


class MoreFragment : BaseFragment(), Injectable {

    companion object {
        val NAMA = "NAMA"
        val ID = "ID"
        val ID_NAMA = "ID_NAMA"
        val SLUG = "SLUG"
        val TIME = "TIME"
        val DESCRIPTION = "DESCRIPTION"
        val LOCATION = "LOCATION"
        val BG = "BG"
    }

    @Inject
    lateinit var realm: Realm

    var currentPhotoPath: String = ""

    private val restForeground by lazy { ApiFactory.create(false) }
    private var disposable: Disposable? = null
    private var nama: String? = null
    private lateinit var id: String
    private lateinit var idNama: String
    private lateinit var time: String
    private lateinit var description: String
    private lateinit var location: String
    private lateinit var bg: String
    private lateinit var slug: String
    val REQUEST_IMAGE_CAPTURE = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_more, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        slug = arguments?.getString(SLUG).toString()
        idNama = arguments?.getString(ID_NAMA).toString()
        time = arguments?.getString(TIME).toString()
        getQRCode(idNama)
        getGameShow(slug)

        iv_more_pict.setOnClickListener {
            dispatchTakePictureIntent()
        }

        btn_more_upload.setOnClickListener {
            showLoading()
            val score = et_more_score.text.toString()
            if (!score.isBlank() && !score.isEmpty() && !currentPhotoPath.isEmpty() && !currentPhotoPath.isBlank()) {
                uploadScore()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Pastikan Score dan Foto terisi sebelum diupload",
                    Toast.LENGTH_SHORT
                ).show()
                dialogAnimation?.dismiss()
            }
        }
    }

    fun uploadFoto() {
        lifecycleScope.launch(Dispatchers.Main) {
            val fileFoto = File(currentPhotoPath)
            val compressedImageFile =
                activity?.let {

                    Compressor.compress(it, fileFoto, Dispatchers.Main) {
                        resolution(300, 300)
                    }
                }
            val requestBodyFoto =
                compressedImageFile?.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val foto =
                requestBodyFoto?.let { MultipartBody.Part.createFormData("img", fileFoto.name, it) }

            disposable = foto?.let {
                restForeground
                    .postImage(idNama.toInt(), it)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        dialogAnimation?.dismiss()
                        if (id.equals("6")){
                            checkScore(idNama)
                        } else {
                            val bundle = Bundle()
                            bundle.putString(ScannerFragment.TYPE, ScannerFragment.TYPE_OUT)
                            (activity as AppCompatActivity).findNavController(R.id.nav_host_fragment_container)
                                .navigate(R.id.action_moreFragment_to_scannerFragment, bundle)
                        }

//                        val bundle = Bundle()
//                        bundle.putString(ScannerFragment.TYPE, ScannerFragment.TYPE_OUT)
//                        (activity as AppCompatActivity).findNavController(R.id.nav_host_fragment_container)
//                            .navigate(R.id.action_moreFragment_to_scannerFragment, bundle)
                        //                Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
                    }, {
                        //                Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
                        Log.d(BuildConfig.TAG, "uploadFoto: ${it.message}")
                    })
            }
        }
    }

    fun test() {
        var exportRealmFile: File? = null
        try {
            // get or create an "export.realm" file
            exportRealmFile = File(activity?.externalCacheDir, "export.realm")

            // if "export.realm" already exists, delete
            exportRealmFile.delete()

            // copy current realm to "export.realm"
            realm.writeCopyTo(exportRealmFile)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        realm.close()
    }

    fun uploadScore() {
        val t = HashMap<String, Int>()
        t["stage$id"] = et_more_score.text.toString().toInt()
        t["time$id"] = et_more_waktu.text.toString().toInt()

        var exportRealmFile: File? = null
        try {
            // get or create an "export.realm" file
            exportRealmFile = File(activity?.externalCacheDir, "export.realm")

            // if "export.realm" already exists, delete
            exportRealmFile.delete()

            // copy current realm to "export.realm"
            realm.writeCopyTo(exportRealmFile)
        } catch (e: IOException) {
            e.printStackTrace()
        }


        var pesertaFinish = PesertaFinish()
        pesertaFinish.nama = nama
        when (id.toInt()) {
            1 -> {
                pesertaFinish.time1 = time
            }

            2 -> {
                pesertaFinish.time2 = time
            }

            3 -> {
                pesertaFinish.time3 = time
            }

            4 -> {
                pesertaFinish.time4 = time
            }

            5 -> {
                pesertaFinish.time5 = time
            }

            6 -> {
                pesertaFinish.time6 = time
            }
        }

        realm.executeTransaction {
            it.insertOrUpdate(pesertaFinish)
        }

        Log.d(TAG, "uploadScore: ID_NAMA $idNama => $t")

        disposable = restForeground
            .postScore(idNama.toInt(), t)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                uploadFoto()
//                Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
            }, {
//                Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
                Log.d(BuildConfig.TAG, "getGameShow: ${it.message}")
            })
    }

    fun checkScore(id: String) {
        disposable = restForeground
            .checkScore(id)
            .subscribeOn(io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d(TAG, "checkScore: ${it.data?.nama}")
                if (it.data?.stage1?.toInt() ?: 0 > 0 && it.data?.stage2?.toInt() ?: 0 > 0
                    && it.data?.stage3?.toInt() ?: 0 > 0 && it.data?.stage4?.toInt() ?: 0 > 0
                    && it.data?.stage5?.toInt() ?: 0 > 0 && it.data?.stage6?.toInt() ?: 0 > 0
                ) {
                    showLoadingTotal(
                        it.data?.stage1.toString(),
                        it.data?.stage2.toString(),
                        it.data?.stage3.toString(),
                        it.data?.stage4.toString(),
                        it.data?.stage5.toString(),
                        it.data?.stage6.toString()
                    )
                }
                dialogAnimationButon?.setOnClickListener {
                    dialogAnimation1?.dismiss()
                    val bundle = Bundle()
                    bundle.putString(ScannerFragment.TYPE, ScannerFragment.TYPE_OUT)
                    (activity as AppCompatActivity).findNavController(R.id.nav_host_fragment_container)
                        .navigate(R.id.action_moreFragment_to_scannerFragment, bundle)
                }
            }, {
                Log.d(TAG, "checkScore: ${it.toString()}")
            })
    }

    fun showLoadingTotal(
        game1: String,
        game2: String,
        game3: String,
        game4: String,
        game5: String,
        game6: String
    ) {
        dialogAnimation1()
        dialogAnimationImage?.visibility = View.GONE
        dialogAnimationProgress?.visibility = View.GONE
        dialogAnimationTitle?.text = "Total Score"
        dialogAnimationDesc?.text = "Game 1 : $game1 \n" +
                "Game 2 : $game2 \n" +
                "Game 3 : $game3 \n" +
                "Game 4 : $game4 \n" +
                "Game 5 : $game5 \n" +
                "Game 6 : $game6 "
    }

    fun getGameShow(slug: String) {
        disposable = restForeground
            .getGameShow(slug)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                it.data?.let { data -> onSuccessGame(data) }
            }, {
                Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
                Log.d(BuildConfig.TAG, "getGameShow: $it")
            })
    }

    fun onSuccessGame(data: Games) {
        data.let {
            id = data.id.toString()
            Toast.makeText(requireContext(), id, Toast.LENGTH_SHORT).show()
        }
    }

    fun getQRCode(id: String) {
        disposable = restForeground
            .getQR(id.toInt())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                it.data?.let { data -> onSuccessQR(data) }
            }, {
                Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
                Log.d(BuildConfig.TAG, "getQRCode: $it")
            })
    }

    fun onSuccessQR(data: Peserta) {
        data.let {
            et_more_nama.setText(it.nama)
            nama = it.nama
            et_more_waktu.setText(time)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === REQUEST_IMAGE_CAPTURE && resultCode === RESULT_OK) {
            val extras: Bundle? = data?.extras
            val imageBitmap = extras?.get("data") as Bitmap?
            iv_more_pict.setImageBitmap(imageBitmap)
            GlideApp.with(requireContext())
                .load(currentPhotoPath)
                .into(iv_more_pict)
            Toast.makeText(context, currentPhotoPath, Toast.LENGTH_SHORT).show()
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            activity?.let { activity ->
                activity.packageManager.let {
                    takePictureIntent.resolveActivity(it)?.also {
                        val photoFile: File? = try {
                            createImageFile()
                        } catch (e: IOException) {
                            null
                        }
                        photoFile?.also {
                            val photoURI: Uri = FileProvider.getUriForFile(
                                activity,
                                "com.deval.event.fileprovider",
                                it
                            )
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                        }
                    }
                }
            }
        }
    }

    protected fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    fun showLoading() {
        dialogAnimation()
        dialogAnimationImage?.visibility = View.GONE
        dialogAnimationTitle?.text = "Upload Score"
        dialogAnimationDesc?.text = "Silahkan menunggu sesaat"
    }

}